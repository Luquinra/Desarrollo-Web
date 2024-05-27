package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.player.CargoItemResponse;
import com.luisaq.spacetrader.model.player.CargoItem;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = ProductMapper.class)
public interface CargoItemMapper {

    CargoItemResponse modelToResponse(CargoItem cargoItem);

}
