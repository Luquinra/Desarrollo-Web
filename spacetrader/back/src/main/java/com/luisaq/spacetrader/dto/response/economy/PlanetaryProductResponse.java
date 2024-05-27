package com.luisaq.spacetrader.dto.response.economy;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PlanetaryProductResponse {
    private Long id;
    private ProductResponse product;
    private Long stock;
    private BigDecimal buyPrice;
}
