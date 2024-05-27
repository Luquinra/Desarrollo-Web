package com.luisaq.spacetrader.controller;

import com.luisaq.spacetrader.controller.internal.AuthExtractor;
import com.luisaq.spacetrader.dto.request.economy.BuyRequest;
import com.luisaq.spacetrader.dto.request.economy.SellRequest;
import com.luisaq.spacetrader.dto.response.economy.PlanetaryMarketResponse;
import com.luisaq.spacetrader.dto.response.player.CargoItemResponse;
import com.luisaq.spacetrader.service.EconomyService;
import jakarta.validation.Valid;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.html.parser.Entity;

@RestController
@RequestMapping("/economy")
@Validated
public class EconomyController {

    @Autowired
    private EconomyService economyService;

    @Autowired
    private AuthExtractor authExtractor;

    @GetMapping("/planet_market")
    public PlanetaryMarketResponse getPlanetaryMarket() {
        return economyService.getPlanetaryMarket(authExtractor.extractAuth().getUsername());
    }

    @GetMapping("/calculate_cargo")
    public Iterable<CargoItemResponse> calculateCargoPrinces(){
        return economyService.calculateCargoPrices(authExtractor.extractAuth().getUsername());
    }

    @PreAuthorize("hasAnyRole('CAPTAIN', 'TRADER')")
    @PostMapping("/buy")
    public ResponseEntity<Object> buyProduct(@Valid @RequestBody BuyRequest request){
        this.economyService.buyProduct(authExtractor.extractAuth().getUsername(), request);
        return ResponseEntity.accepted().build();
    }

    @PreAuthorize("hasAnyRole('CAPTAIN', 'TRADER')")
    @PostMapping("/sell")
    public ResponseEntity<Object> sellProduct(@Valid @RequestBody SellRequest request){
        this.economyService.sellProduct(authExtractor.extractAuth().getUsername(), request);
        return ResponseEntity.accepted().build();
    }
}
