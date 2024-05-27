package com.luisaq.spacetrader.controller;

import com.luisaq.spacetrader.controller.internal.AuthExtractor;
import com.luisaq.spacetrader.dto.response.player.CrewResponse;
import com.luisaq.spacetrader.service.CrewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/crew")
public class CrewController {

    @Autowired
    private CrewService crewService;

    @Autowired
    private AuthExtractor authExtractor;

    @GetMapping("/mycrew")
    public CrewResponse getCrewByUser(){
        return crewService.getCrewResponseFromUsername(authExtractor.extractAuth().getUsername());
    }
}
