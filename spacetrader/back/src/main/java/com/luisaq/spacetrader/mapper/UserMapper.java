package com.luisaq.spacetrader.mapper;

import com.luisaq.spacetrader.dto.response.player.UserResponse;
import com.luisaq.spacetrader.model.user.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserResponse modelToResponse(User user);
}
