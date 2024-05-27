package com.luisaq.spacetrader.service.auth;

import com.luisaq.spacetrader.dto.request.auth.LoginRequest;
import com.luisaq.spacetrader.dto.response.auth.LoginResponse;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userService;


    public LoginResponse login(LoginRequest request){
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        val user = userService.getUserByUsername(request.getUsername());
        return LoginResponse.builder()
                .token(this.jwtService.generateToken(
                        Map.of("ROLE", user.getPlayer().getRole().name()),
                        user
                ))
                .build();
    }
}
