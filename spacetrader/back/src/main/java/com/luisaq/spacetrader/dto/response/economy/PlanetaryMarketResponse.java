package com.luisaq.spacetrader.dto.response.economy;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class PlanetaryMarketResponse {
    private Set<PlanetaryProductResponse> products;
    private Double demandFactor;
    private Double offerFactor;
}
