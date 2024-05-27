package com.luisaq.spacetrader.dto.request.economy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class SellRequest {
    @NotNull
    @Min(value = 0)
    private Long cargoItemId;
    @NotNull
    @Min(value = 0)
    private Long quantity;
}
