package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.economy.PlanetaryMarketResponse;
import com.luisaq.spacetrader.model.economy.PlanetaryMarket;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {
        ProductMapper.class, PlanetaryProductMapper.class
})
public interface PlanetaryMarketMapper {

    PlanetaryMarketResponse modelToResponse(PlanetaryMarket planetaryMarket);
}