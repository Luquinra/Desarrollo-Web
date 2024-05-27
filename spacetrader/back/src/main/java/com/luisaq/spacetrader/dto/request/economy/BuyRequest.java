package com.luisaq.spacetrader.dto.request.economy;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BuyRequest {
    @NotNull
    @Min(value = 0)
    private Long productMarketId;
    @NotNull
    @Min(value = 0)
    private Long quantity;
}
