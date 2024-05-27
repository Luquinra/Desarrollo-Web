package com.luisaq.spacetrader.dto.response.player;

import com.luisaq.spacetrader.dto.response.economy.ProductResponse;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Builder
@Data
public class CargoItemResponse {
    private Long id;
    private ProductResponse product;
    private Long quantity;
    private BigDecimal sellPrice; //Sell price per planet
}
