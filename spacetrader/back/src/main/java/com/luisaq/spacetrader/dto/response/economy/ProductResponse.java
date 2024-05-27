package com.luisaq.spacetrader.dto.response.economy;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductResponse {
    private Long id;
    private String name;
    private Double volume;
    private String img;
}
