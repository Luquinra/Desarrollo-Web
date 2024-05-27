package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.economy.PlanetaryProductResponse;
import com.luisaq.spacetrader.model.economy.PlanetaryProduct;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.math.BigDecimal;

@Mapper(componentModel = "spring", uses = {
        ProductMapper.class
})
public interface PlanetaryProductMapper {


    @Mapping(target = "buyPrice", expression = "java(calculatePrices(planetaryProduct.getMarket().getOfferFactor(), planetaryProduct.getStock() ))")
    PlanetaryProductResponse modelToResponse(PlanetaryProduct planetaryProduct);


    default BigDecimal calculatePrices(Double factor, Long stock){
        return BigDecimal.valueOf(factor/(1+stock));
    }
}
