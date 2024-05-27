package com.luisaq.spacetrader.service;

import com.luisaq.spacetrader.dto.request.travel.TravelRequest;
import com.luisaq.spacetrader.dto.response.world.OtherCrewResponse;
import com.luisaq.spacetrader.dto.response.world.PlanetResponse;
import com.luisaq.spacetrader.dto.response.travel.WormHoleResponse;
import com.luisaq.spacetrader.dto.validation.TravelType;
import com.luisaq.spacetrader.mapper.CrewMapper;
import com.luisaq.spacetrader.mapper.PlanetMapper;
import com.luisaq.spacetrader.mapper.WormHoleMapper;
import com.luisaq.spacetrader.model.player.Crew;
import com.luisaq.spacetrader.model.world.Star;
import com.luisaq.spacetrader.repository.PlanetRepository;
import com.luisaq.spacetrader.repository.StarRepository;
import jakarta.transaction.Transactional;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class NavService {

    private static final Integer WORMHOLE_STATUS_SIZE = 10;
    private static final Integer MAX_RECURSION_WORMHOLE_SEARCH = 40;

    @Autowired
    private CrewService crewService;

    @Autowired
    private WormHoleMapper wormHoleMapper;

    @Autowired
    private PlanetMapper planetMapper;

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private CrewMapper crewMapper;

    public Iterable<PlanetResponse> getStarPlanets(String username) {
        val crew = this.crewService.getCrewFromUsername(username);
        return crew.getStar().getPlanets().stream().map(planetMapper::modelToResponse)
                .collect(Collectors.toUnmodifiableSet());
    }

    public Iterable<WormHoleResponse> getClosestStars(String username) {
        val crew = this.crewService.getCrewFromUsername(username);

        val times = new HashMap<Star, Double>();
        times.put(crew.getStar(), 0.0);
        explore(crew.getStar(), times, 0);

        val info = times.entrySet().stream().limit(WORMHOLE_STATUS_SIZE)
                .map(starDoubleEntry ->
                    wormHoleMapper.starModelToResponse(starDoubleEntry.getKey(), starDoubleEntry.getValue())
                )
                .sorted(Comparator.comparingDouble(WormHoleResponse::getTravelTime))
                .toList();

        return info;
    }

    private void explore(Star current, Map<Star, Double> times, int depth) {
        if (depth >= MAX_RECURSION_WORMHOLE_SEARCH) {
            return;
        }
        val currentTime = times.getOrDefault(current, 0.0);
        for (val wormHole : current.getWormHoles()) {
            val destination = wormHole.getDestinationStar();
            val time = wormHole.getTravelTime();
            val newTime = currentTime + time;

            if (!times.containsKey(destination) || newTime < times.get(destination)) {
                times.put(destination, newTime);
                explore(destination, times, depth + 1);
            }
        }
    }

    @Transactional
    public void navigate(String username, TravelRequest request) {
        val crew = this.crewService.getCrewFromUsername(username);
        if (request.getType().equals(TravelType.PLANET)){
            planetNavigation(crew, request);
        }else if (request.getType().equals(TravelType.STAR)){
            starNavigation(crew, request);
        }else {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The travel type requested is invalid");
        }
    }

    @Transactional
    private void planetNavigation(Crew crew, TravelRequest request){
        val destinationPlanet = this.planetRepository.findById(request.getStarOrPlanetId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The destination planet could not be found"));
        val planet = crew.getPlanet();
        if(planet != null){
            planet.getCrews().remove(crew);
            this.planetRepository.save(planet);
        }
        destinationPlanet.getCrews().add(crew);
        this.planetRepository.save(destinationPlanet);
        crew.setPlanet(destinationPlanet);
        this.crewService.updateCrew(crew);
    }

    @Transactional
    private void starNavigation(Crew crew, TravelRequest request){
        val destination_star = this.starRepository.findById(request.getStarOrPlanetId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "The destination star could not be found"));
        val star = crew.getStar();
        star.getAnchoredSpaceShips().remove(crew);
        destination_star.getAnchoredSpaceShips().add(crew);
        this.starRepository.save(star);
        this.starRepository.save(destination_star);
        crew.setStar(destination_star);
        if(crew.getPlanet() != null){
            crew.getPlanet().getCrews().remove(crew);
            crew.setPlanet(null);
        }
        this.crewService.updateCrew(crew);
    }

    public Iterable<OtherCrewResponse> getCrewsOnStar(String username) {
        val crew = this.crewService.getCrewFromUsername(username);
        val star = crew.getStar();
        val crews = this.starRepository.getAnchoredCrewsOnStarExceptId(star.getId(), crew.getId());
        return crews.stream().map(otherCrew -> {
            val captain = this.crewService.getCaptainFromCrew(otherCrew.getId());
            return crewMapper.crewToOtherCrewResponse(otherCrew, captain);
        }).collect(Collectors.toUnmodifiableSet());
    }
}
