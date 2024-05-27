package com.luisaq.spacetrader.dto.request.travel;

import com.luisaq.spacetrader.dto.validation.TravelType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TravelRequest {
    @NotNull
    @Min(value = 0)
    private Long starOrPlanetId;

    @NotNull
    private TravelType type;
}
