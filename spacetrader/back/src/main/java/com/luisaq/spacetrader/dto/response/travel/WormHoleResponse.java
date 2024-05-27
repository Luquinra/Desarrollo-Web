package com.luisaq.spacetrader.dto.response.travel;

import com.luisaq.spacetrader.dto.response.world.StarResponse;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class WormHoleResponse {
    private Double travelTime;
    private StarResponse destinationStar;
}
