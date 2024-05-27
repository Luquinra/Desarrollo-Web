package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.travel.WormHoleResponse;
import com.luisaq.spacetrader.model.world.Star;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-26T19:00:04-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class WormHoleMapperImpl implements WormHoleMapper {

    @Autowired
    private StarMapper starMapper;

    @Override
    public WormHoleResponse starModelToResponse(Star star, Double accumulativeTime) {
        if ( star == null && accumulativeTime == null ) {
            return null;
        }

        WormHoleResponse.WormHoleResponseBuilder wormHoleResponse = WormHoleResponse.builder();

        wormHoleResponse.destinationStar( starMapper.modelToResponse( star ) );
        wormHoleResponse.travelTime( accumulativeTime );

        return wormHoleResponse.build();
    }
}
