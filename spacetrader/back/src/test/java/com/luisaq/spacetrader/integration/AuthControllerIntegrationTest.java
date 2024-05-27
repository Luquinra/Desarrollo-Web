package com.luisaq.spacetrader.integration;

import com.luisaq.spacetrader.IntegrationTest;
import com.luisaq.spacetrader.dto.request.auth.LoginRequest;
import com.luisaq.spacetrader.dto.response.auth.LoginResponse;
import com.luisaq.spacetrader.model.player.Crew;
import com.luisaq.spacetrader.model.player.Player;
import com.luisaq.spacetrader.model.player.PlayerRole;
import com.luisaq.spacetrader.model.user.User;
import com.luisaq.spacetrader.repository.CrewRepository;
import com.luisaq.spacetrader.repository.PlayerRepository;
import com.luisaq.spacetrader.repository.UserRepository;
import com.nimbusds.jose.JWSObject;
import com.nimbusds.jwt.JWTClaimsSet;
import lombok.val;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;


import java.math.BigDecimal;
import java.text.ParseException;

import static org.junit.jupiter.api.Assertions.*;


@IntegrationTest
public class AuthControllerIntegrationTest {

    @LocalServerPort
    private int port;


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private CrewRepository crewRepository;

    private WebTestClient webTestClient;

    @BeforeAll
    void init(){
        this.userRepository.findAll().forEach(System.out::println);
        val passwordEncoder = new BCryptPasswordEncoder();
        val crew = crewRepository.save(Crew.builder().credits(BigDecimal.ONE).build());
        val user = userRepository.save(User.builder().username("user")
                .password(passwordEncoder.encode("123"))
                .build());
        val player = playerRepository.save(Player.builder().role(PlayerRole.CAPTAIN)
                .crew(crew).user(user).img("img").build());
        user.setPlayer(player);
        webTestClient =  WebTestClient.bindToServer().baseUrl("http://127.0.0.1:" + port).build();;
    }

    @Test
    void badLogin(){
        webTestClient.post().uri("/auth/login")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isBadRequest();
    }

    @Test
    void invalidLogin(){
        val loginRequest = LoginRequest.builder().username("user").password("123456").build();
        webTestClient.post().uri("/auth/login")
                .bodyValue(loginRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isForbidden();
    }

    @Test
    void validLogin(){
        val loginRequest = LoginRequest.builder().username("user").password("123").build();
        webTestClient.post().uri("/auth/login")
                .bodyValue(loginRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void checkToken(){
        val loginRequest = LoginRequest.builder().username("user").password("123").build();
        webTestClient.post().uri("/auth/login")
                .bodyValue(loginRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .consumeWith(response -> {
                    val loginResponse = response.getResponseBody();
                    assertNotNull(loginResponse.getToken());
                });
    }

    @Test
    void extractUsernameFromToken() {
        val loginRequest = LoginRequest.builder().username("user").password("123").build();
        webTestClient.post().uri("/auth/login")
                .bodyValue(loginRequest)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .consumeWith(response -> {
                    val loginResponse = response.getResponseBody();
                    try {
                        val jwtObject = JWSObject.parse(loginResponse.getToken());
                        val claims = JWTClaimsSet.parse(jwtObject.getPayload().toJSONObject());
                        assertEquals(claims.getClaim("sub"), "user");
                    } catch (ParseException e) {
                        fail();
                    }
                });
    }
}
