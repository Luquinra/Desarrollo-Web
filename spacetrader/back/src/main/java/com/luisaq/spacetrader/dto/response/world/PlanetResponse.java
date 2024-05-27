package com.luisaq.spacetrader.dto.response.world;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PlanetResponse {
    private Long id;
    private String name;
    private Boolean inhabited;
    private String img;
    //private PlanetaryMarketResponse market;
}
