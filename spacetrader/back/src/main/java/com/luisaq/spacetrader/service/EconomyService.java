package com.luisaq.spacetrader.service;

import com.luisaq.spacetrader.dto.request.economy.BuyRequest;
import com.luisaq.spacetrader.dto.request.economy.SellRequest;
import com.luisaq.spacetrader.dto.response.economy.PlanetaryMarketResponse;
import com.luisaq.spacetrader.dto.response.player.CargoItemResponse;
import com.luisaq.spacetrader.mapper.CargoItemMapper;
import com.luisaq.spacetrader.mapper.PlanetaryMarketMapper;
import com.luisaq.spacetrader.model.economy.PlanetaryProduct;
import com.luisaq.spacetrader.model.player.CargoItem;
import com.luisaq.spacetrader.repository.CargoItemRepository;
import com.luisaq.spacetrader.repository.PlanetRepository;
import com.luisaq.spacetrader.repository.PlanetaryProductRepository;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;

@Service
public class EconomyService {

    @Autowired
    private CrewService crewService;

    @Autowired
    private PlanetaryMarketMapper planetaryMarketMapper;

    @Autowired
    private PlanetaryProductRepository planetaryProductRepository;

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private CargoItemRepository cargoItemRepository;

    @Autowired
    private CargoItemMapper cargoItemMapper;

    public PlanetaryMarketResponse getPlanetaryMarket(String username) {
        val crew = this.crewService.getCrewFromUsername(username);
        val current_planet = Optional.ofNullable(crew.getPlanet());
        return current_planet.map(planet -> planetaryMarketMapper
                .modelToResponse(planet.getMarket())).orElse(null);
    }


    @Transactional
    public void buyProduct(String username, BuyRequest request) {
        val crew = this.crewService.getCrewFromUsername(username);
        val current_planet = Optional.ofNullable(crew.getPlanet());
        if (current_planet.isEmpty()) {
            //Failsafe, should not be possible tho
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The crew is not on an inhabited planet");
        }

        val planetaryProduct = planetaryProductRepository.findById(request.getProductMarketId());
        if (planetaryProduct.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The product not exists in that market");
        }

        val price = BigDecimal.valueOf(current_planet.get().getMarket()
                .getOfferFactor() / (1 + planetaryProduct.get().getStock()) * request.getQuantity());

        if (crew.getCredits().compareTo(price) < 0) { // If it is less
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Not enough credits");
        }

        if (planetaryProduct.get().getProduct().getVolume() * request.getQuantity() > crew.getSpaceship().getCargoCapacity() - crew.getActualCargoVolume()) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Not enough space in spaceship");
        }

        if (planetaryProduct.get().getStock() < request.getQuantity()) {
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Not enough stock in planet");
        }


        val newCredits = crew.getCredits().subtract(price);
        val product = planetaryProduct.get().getProduct();
        crew.setCredits(newCredits);

        val cargoItem = this.cargoItemRepository.getCargoItemByProductId(product.getId());

        if (cargoItem.isEmpty()){
            val ci = this.cargoItemRepository.save(CargoItem.builder()
                    .product(product).quantity(request.getQuantity()).crew(crew).build());

            crew.getCargo().add(ci);
        }else{
            cargoItem.get().setQuantity(cargoItem.get().getQuantity() + request.getQuantity());
            this.cargoItemRepository.save(cargoItem.get());
        }
        crew.setActualCargoVolume(crew.getActualCargoVolume() + product.getVolume() * request.getQuantity());
        this.crewService.updateCrew(crew);


        if(Objects.equals(request.getQuantity(), planetaryProduct.get().getStock())){
            current_planet.get().getMarket().getProducts().remove(planetaryProduct.get());
            this.planetRepository.save(current_planet.get());
        }else{
            planetaryProduct.get().setStock(planetaryProduct.get().getStock() - request.getQuantity());
            this.planetaryProductRepository.save(planetaryProduct.get());
        }
    }

    @Transactional
    public void sellProduct(String username, SellRequest request) {
        val crew = this.crewService.getCrewFromUsername(username);
        val current_planet = Optional.ofNullable(crew.getPlanet());
        if (current_planet.isEmpty()) {
            //Failsafe, should not be possible tho
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The crew is not on an inhabited planet");
        }

        val cargoItem = this.crewService.getProductFromCrewCargo(crew.getId(), request.getCargoItemId());

        if(request.getQuantity() > cargoItem.getQuantity()){
            throw new ResponseStatusException(HttpStatus.PRECONDITION_FAILED, "Quantity not possible");
        }

        val planetProduct = this.planetaryProductRepository
                .getPlanetaryProductByProductIdAndMarketId(
                        cargoItem.getProduct().getId(), current_planet.get().getMarket().getId());


        val stock = planetProduct.isEmpty() ? 0: planetProduct.get().getStock();

        val price = BigDecimal.valueOf(current_planet.get().getMarket()
                .getOfferFactor() / (1 + stock) * request.getQuantity());

        val newCredits = crew.getCredits().add(price);

        if(planetProduct.isEmpty()){
            val newPlanetaryProduct = PlanetaryProduct.builder()
                    .product(cargoItem.getProduct())
                    .stock(request.getQuantity())
                    .market(current_planet.get().getMarket())
                    .build();
            val pro = this.planetaryProductRepository.save(newPlanetaryProduct);
            current_planet.get().getMarket().getProducts().add(pro);
            this.planetRepository.save(current_planet.get());
        }else{
            planetProduct.get().setStock(planetProduct.get().getStock() + request.getQuantity());
            this.planetaryProductRepository.save(planetProduct.get());
        }

        if (Objects.equals(cargoItem.getQuantity(), request.getQuantity())){
            crew.getCargo().remove(cargoItem);
            this.cargoItemRepository.delete(cargoItem);
            this.crewService.updateCrew(crew);
        }else{
            cargoItem.setQuantity(cargoItem.getQuantity() - request.getQuantity());
            this.cargoItemRepository.save(cargoItem);
        }

        crew.setCredits(newCredits);
        crew.setActualCargoVolume(crew.getActualCargoVolume() - cargoItem.getProduct().getVolume() * request.getQuantity());
        this.crewService.updateCrew(crew);
    }

    public Iterable<CargoItemResponse> calculateCargoPrices(String username) {
        val crew = this.crewService.getCrewFromUsername(username);
        val current_planet = Optional.ofNullable(crew.getPlanet());
        if (current_planet.isEmpty()) {
            //Failsafe, should not be possible tho
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The crew is not on an inhabited planet");
        }

        return crew.getCargo().stream().map(cargoItem -> {
            val planetaryProduct = this.planetaryProductRepository
                    .getPlanetaryProductByProductIdAndMarketId(cargoItem.getProduct().getId(),
                            current_planet.get().getMarket().getId());

            val cargoItemResponse = cargoItemMapper.modelToResponse(cargoItem);

            val stock = planetaryProduct.isEmpty() ? 0 : planetaryProduct.get().getStock();
            cargoItemResponse.setSellPrice(BigDecimal
                    .valueOf(current_planet.get().getMarket().getDemandFactor() / (1 + stock)));

            return cargoItemResponse;
        }).toList();
    }
}
