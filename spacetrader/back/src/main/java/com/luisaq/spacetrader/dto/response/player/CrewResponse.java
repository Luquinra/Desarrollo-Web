package com.luisaq.spacetrader.dto.response.player;

import com.luisaq.spacetrader.dto.response.world.PlanetResponse;
import com.luisaq.spacetrader.dto.response.world.StarResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Set;

@Data
@Builder
public class CrewResponse {
    private Long id;
    private Set<PlayerResponse> crewMembers;
    private SpaceshipResponse spaceship;
    private BigDecimal credits;
    private Set<CargoItemResponse> cargo;
    private StarResponse star;
    private PlanetResponse planet;
    private Double actualCargoVolume;
}
