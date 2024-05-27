package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.player.CrewResponse;
import com.luisaq.spacetrader.dto.response.player.PlayerResponse;
import com.luisaq.spacetrader.dto.response.world.OtherCrewResponse;
import com.luisaq.spacetrader.model.player.Crew;
import com.luisaq.spacetrader.model.player.Player;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ProductMapper.class, PlanetaryProductMapper.class
})
public interface CrewMapper {

    CrewResponse modelToResponse(Crew crew);

    @Mapping(source = "player.user.username", target = "username")
    PlayerResponse userToPlayerResponse(Player player);

    @Mapping(target = "captain", source = "crewCaptain")
    OtherCrewResponse crewToOtherCrewResponse(Crew crew, Player crewCaptain);

}
