package com.luisaq.spacetrader.service;

import com.luisaq.spacetrader.model.world.Planet;
import com.luisaq.spacetrader.repository.PlanetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PlanetService {

    @Autowired
    private PlanetRepository planetRepository;

    protected void updatePlanet(Planet planet){
        this.planetRepository.save(planet);
    }
}
