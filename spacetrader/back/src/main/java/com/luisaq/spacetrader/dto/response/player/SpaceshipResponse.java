package com.luisaq.spacetrader.dto.response.player;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SpaceshipResponse {
    private String name;
    private Double cargoCapacity;
    private Double maxSpeed;
    private String img;
}
