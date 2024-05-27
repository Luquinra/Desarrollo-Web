package com.luisaq.spacetrader.controller;

import com.luisaq.spacetrader.controller.internal.AuthExtractor;
import com.luisaq.spacetrader.dto.request.travel.TravelRequest;
import com.luisaq.spacetrader.dto.response.world.OtherCrewResponse;
import com.luisaq.spacetrader.dto.response.world.PlanetResponse;
import com.luisaq.spacetrader.dto.response.travel.WormHoleResponse;
import com.luisaq.spacetrader.service.NavService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/nav")
@Validated
public class NavController {

    @Autowired
    private NavService navService;

    @Autowired
    private AuthExtractor authExtractor;

    @GetMapping("/status")
    public Iterable<WormHoleResponse> getClosestStars(){
        return navService.getClosestStars(authExtractor.extractAuth().getUsername());
    }

    @GetMapping("/star_planets")
    public Iterable<PlanetResponse> getPlanetsStar(){
        return navService.getStarPlanets(authExtractor.extractAuth().getUsername());
    }

    @GetMapping("/star_status")
    public Iterable<OtherCrewResponse> getCrewsOnTheSameStar(){
        return navService.getCrewsOnStar(authExtractor.extractAuth().getUsername());
    }

    @PreAuthorize("hasAnyRole('CAPTAIN', 'PILOT')")
    @PostMapping("/travel")
    public ResponseEntity<Object> navigate(@Valid @RequestBody TravelRequest request){
        navService.navigate(authExtractor.extractAuth().getUsername(), request);
        return ResponseEntity.accepted().build();
    }
}
