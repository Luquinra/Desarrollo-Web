package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.player.CargoItemResponse;
import com.luisaq.spacetrader.model.player.CargoItem;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-26T19:00:04-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class CargoItemMapperImpl implements CargoItemMapper {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public CargoItemResponse modelToResponse(CargoItem cargoItem) {
        if ( cargoItem == null ) {
            return null;
        }

        CargoItemResponse.CargoItemResponseBuilder cargoItemResponse = CargoItemResponse.builder();

        cargoItemResponse.id( cargoItem.getId() );
        cargoItemResponse.product( productMapper.modelToResponse( cargoItem.getProduct() ) );
        cargoItemResponse.quantity( cargoItem.getQuantity() );

        return cargoItemResponse.build();
    }
}
