package com.luisaq.spacetrader.dto.response.world;

import com.luisaq.spacetrader.dto.response.player.PlayerResponse;
import com.luisaq.spacetrader.dto.response.player.SpaceshipResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OtherCrewResponse {
    private SpaceshipResponse spaceship;
    private PlayerResponse captain;
}
