package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.travel.WormHoleResponse;
import com.luisaq.spacetrader.model.world.Star;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        StarMapper.class
})
public interface WormHoleMapper {

    @Mapping(target = "travelTime", source = "accumulativeTime")
    @Mapping(target = "destinationStar", source = "star")
    WormHoleResponse starModelToResponse(Star star, Double accumulativeTime);
}
