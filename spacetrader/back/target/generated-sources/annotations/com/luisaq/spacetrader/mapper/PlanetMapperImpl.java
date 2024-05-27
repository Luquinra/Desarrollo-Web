package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.world.PlanetResponse;
import com.luisaq.spacetrader.model.world.Planet;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-26T19:00:04-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class PlanetMapperImpl implements PlanetMapper {

    @Override
    public PlanetResponse modelToResponse(Planet planet) {
        if ( planet == null ) {
            return null;
        }

        PlanetResponse.PlanetResponseBuilder planetResponse = PlanetResponse.builder();

        planetResponse.id( planet.getId() );
        planetResponse.img( planet.getImg() );
        planetResponse.inhabited( planet.getInhabited() );
        planetResponse.name( planet.getName() );

        return planetResponse.build();
    }
}
