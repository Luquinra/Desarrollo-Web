package com.luisaq.spacetrader.dto.response.player;

import com.luisaq.spacetrader.model.player.PlayerRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlayerResponse {
    private Long id;
    private PlayerRole role;
    private String username;
    private String img;
}
