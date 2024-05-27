package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.player.CargoItemResponse;
import com.luisaq.spacetrader.dto.response.player.CrewResponse;
import com.luisaq.spacetrader.dto.response.player.PlayerResponse;
import com.luisaq.spacetrader.dto.response.player.SpaceshipResponse;
import com.luisaq.spacetrader.dto.response.world.OtherCrewResponse;
import com.luisaq.spacetrader.dto.response.world.PlanetResponse;
import com.luisaq.spacetrader.dto.response.world.StarResponse;
import com.luisaq.spacetrader.model.player.CargoItem;
import com.luisaq.spacetrader.model.player.Crew;
import com.luisaq.spacetrader.model.player.Player;
import com.luisaq.spacetrader.model.player.Spaceship;
import com.luisaq.spacetrader.model.user.User;
import com.luisaq.spacetrader.model.world.Planet;
import com.luisaq.spacetrader.model.world.Star;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2024-05-26T19:00:03-0500",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.38.0.v20240417-1011, environment: Java 17.0.10 (Eclipse Adoptium)"
)
@Component
public class CrewMapperImpl implements CrewMapper {

    @Autowired
    private ProductMapper productMapper;

    @Override
    public CrewResponse modelToResponse(Crew crew) {
        if ( crew == null ) {
            return null;
        }

        CrewResponse.CrewResponseBuilder crewResponse = CrewResponse.builder();

        crewResponse.actualCargoVolume( crew.getActualCargoVolume() );
        crewResponse.cargo( cargoItemSetToCargoItemResponseSet( crew.getCargo() ) );
        crewResponse.credits( crew.getCredits() );
        crewResponse.crewMembers( playerSetToPlayerResponseSet( crew.getCrewMembers() ) );
        crewResponse.id( crew.getId() );
        crewResponse.planet( planetToPlanetResponse( crew.getPlanet() ) );
        crewResponse.spaceship( spaceshipToSpaceshipResponse( crew.getSpaceship() ) );
        crewResponse.star( starToStarResponse( crew.getStar() ) );

        return crewResponse.build();
    }

    @Override
    public PlayerResponse userToPlayerResponse(Player player) {
        if ( player == null ) {
            return null;
        }

        PlayerResponse.PlayerResponseBuilder playerResponse = PlayerResponse.builder();

        playerResponse.username( playerUserUsername( player ) );
        playerResponse.id( player.getId() );
        playerResponse.img( player.getImg() );
        playerResponse.role( player.getRole() );

        return playerResponse.build();
    }

    @Override
    public OtherCrewResponse crewToOtherCrewResponse(Crew crew, Player crewCaptain) {
        if ( crew == null && crewCaptain == null ) {
            return null;
        }

        OtherCrewResponse.OtherCrewResponseBuilder otherCrewResponse = OtherCrewResponse.builder();

        if ( crew != null ) {
            otherCrewResponse.spaceship( spaceshipToSpaceshipResponse( crew.getSpaceship() ) );
        }
        otherCrewResponse.captain( userToPlayerResponse( crewCaptain ) );

        return otherCrewResponse.build();
    }

    protected CargoItemResponse cargoItemToCargoItemResponse(CargoItem cargoItem) {
        if ( cargoItem == null ) {
            return null;
        }

        CargoItemResponse.CargoItemResponseBuilder cargoItemResponse = CargoItemResponse.builder();

        cargoItemResponse.id( cargoItem.getId() );
        cargoItemResponse.product( productMapper.modelToResponse( cargoItem.getProduct() ) );
        cargoItemResponse.quantity( cargoItem.getQuantity() );

        return cargoItemResponse.build();
    }

    protected Set<CargoItemResponse> cargoItemSetToCargoItemResponseSet(Set<CargoItem> set) {
        if ( set == null ) {
            return null;
        }

        Set<CargoItemResponse> set1 = new LinkedHashSet<CargoItemResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( CargoItem cargoItem : set ) {
            set1.add( cargoItemToCargoItemResponse( cargoItem ) );
        }

        return set1;
    }

    protected Set<PlayerResponse> playerSetToPlayerResponseSet(Set<Player> set) {
        if ( set == null ) {
            return null;
        }

        Set<PlayerResponse> set1 = new LinkedHashSet<PlayerResponse>( Math.max( (int) ( set.size() / .75f ) + 1, 16 ) );
        for ( Player player : set ) {
            set1.add( userToPlayerResponse( player ) );
        }

        return set1;
    }

    protected PlanetResponse planetToPlanetResponse(Planet planet) {
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

    protected SpaceshipResponse spaceshipToSpaceshipResponse(Spaceship spaceship) {
        if ( spaceship == null ) {
            return null;
        }

        SpaceshipResponse.SpaceshipResponseBuilder spaceshipResponse = SpaceshipResponse.builder();

        spaceshipResponse.cargoCapacity( spaceship.getCargoCapacity() );
        spaceshipResponse.img( spaceship.getImg() );
        spaceshipResponse.maxSpeed( spaceship.getMaxSpeed() );
        spaceshipResponse.name( spaceship.getName() );

        return spaceshipResponse.build();
    }

    protected StarResponse starToStarResponse(Star star) {
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

    private String playerUserUsername(Player player) {
        if ( player == null ) {
            return null;
        }
        User user = player.getUser();
        if ( user == null ) {
            return null;
        }
        String username = user.getUsername();
        if ( username == null ) {
            return null;
        }
        return username;
    }
}
