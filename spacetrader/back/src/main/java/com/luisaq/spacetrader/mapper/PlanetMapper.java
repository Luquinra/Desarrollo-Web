package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.world.PlanetResponse;
import com.luisaq.spacetrader.model.world.Planet;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        ProductMapper.class
})
public interface PlanetMapper {

    PlanetResponse modelToResponse(Planet planet);
}
