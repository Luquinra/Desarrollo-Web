package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.world.StarResponse;
import com.luisaq.spacetrader.model.world.Star;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        ProductMapper.class
})
public interface StarMapper {

    StarResponse modelToResponse(Star star);
}
