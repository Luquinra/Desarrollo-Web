package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.world.StarResponse;
import com.luisaq.spacetrader.model.world.Star;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-26T19:00:04-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class StarMapperImpl implements StarMapper {

    @Override
    public StarResponse modelToResponse(Star star) {
        if ( star == null ) {
            return null;
        }

        StarResponse.StarResponseBuilder starResponse = StarResponse.builder();

        starResponse.id( star.getId() );
        starResponse.img( star.getImg() );
        starResponse.name( star.getName() );
        starResponse.x( star.getX() );
        starResponse.y( star.getY() );
        starResponse.z( star.getZ() );

        return starResponse.build();
    }
}
