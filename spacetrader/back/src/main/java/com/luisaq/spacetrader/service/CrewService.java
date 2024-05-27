package com.luisaq.spacetrader.service;

import com.luisaq.spacetrader.dto.response.player.CargoItemResponse;
import com.luisaq.spacetrader.dto.response.player.CrewResponse;
import com.luisaq.spacetrader.mapper.CrewMapper;
import com.luisaq.spacetrader.model.player.CargoItem;
import com.luisaq.spacetrader.model.player.Crew;
import com.luisaq.spacetrader.model.player.Player;
import com.luisaq.spacetrader.model.player.PlayerRole;
import com.luisaq.spacetrader.repository.CrewRepository;
import com.luisaq.spacetrader.repository.PlayerRepository;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CrewService {

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CrewMapper crewMapper;


    public CrewResponse getCrewResponseFromUsername(String username){
        return crewMapper.modelToResponse(
                this.getCrewFromUsername(username)
        );
    }

    protected Crew getCrewFromUsername(String username){
        return crewRepository.findCrewsByUserUsername(username);
    }

    @Transactional
    protected void updateCrew(@NonNull Crew crew){
        this.crewRepository.save(crew);
    }

    protected Player getCaptainFromCrew(Long id){
        return playerRepository.getOnePlayerFromCrewWithRole(id, PlayerRole.CAPTAIN);
    }

    protected CargoItem getProductFromCrewCargo(Long crewId, Long cargoItemId){
        return this.crewRepository.getCargoByIdFromCrew(crewId, cargoItemId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }
}
