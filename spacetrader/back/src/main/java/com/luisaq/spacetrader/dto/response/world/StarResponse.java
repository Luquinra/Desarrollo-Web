package com.luisaq.spacetrader.dto.response.world;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StarResponse {
    private Long id;
    private String name;
    private Double x;
    private Double y;
    private Double z;
    private String img;
    //private Set<PlanetResponse> planets;
}
