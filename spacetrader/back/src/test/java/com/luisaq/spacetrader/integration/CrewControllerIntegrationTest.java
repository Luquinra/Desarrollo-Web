package com.luisaq.spacetrader.integration;

import com.luisaq.spacetrader.IntegrationTest;
import com.luisaq.spacetrader.dto.request.auth.LoginRequest;
import com.luisaq.spacetrader.dto.response.auth.LoginResponse;
import com.luisaq.spacetrader.dto.response.player.CrewResponse;
import com.luisaq.spacetrader.model.player.Crew;
import com.luisaq.spacetrader.model.player.Player;
import com.luisaq.spacetrader.model.player.PlayerRole;
import com.luisaq.spacetrader.model.user.User;
import com.luisaq.spacetrader.model.world.Star;
import com.luisaq.spacetrader.repository.CrewRepository;
import com.luisaq.spacetrader.repository.PlayerRepository;
import com.luisaq.spacetrader.repository.StarRepository;
import com.luisaq.spacetrader.repository.UserRepository;
import lombok.val;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class CrewControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private StarRepository starRepository;


    private WebTestClient webTestClient;
    private String jwtToken;

    @BeforeAll
    void init(){
        val passwordEncoder = new BCryptPasswordEncoder();
        val crew = crewRepository.save(Crew.builder().credits(BigDecimal.TWO).actualCargoVolume(5.5).build());
        val user = userRepository.save(User.builder().username("user")
                .password(passwordEncoder.encode("123"))
                .build());
        val player = playerRepository.save(Player.builder().role(PlayerRole.CAPTAIN)
                .crew(crew).user(user).img("img").build());
        user.setPlayer(player);
        val star = this.starRepository.save(Star.builder().z(1.0).y(1.0).x(1.0).name("testStar")
                        .img("img")
                .build());
        crew.setStar(star);
        crewRepository.save(crew);
        webTestClient =  WebTestClient.bindToServer().baseUrl("http://127.0.0.1:" + port).build();
        this.getAuthToken();
    }

    private void getAuthToken(){
        val loginRequest = LoginRequest.builder().username("user").password("123").build();
        this.webTestClient.post().uri("/auth/login").bodyValue(loginRequest)
                .exchange()
                .expectBody(LoginResponse.class)
                .consumeWith(entityExchangeResult -> this.jwtToken = entityExchangeResult.getResponseBody().getToken());
    }

    @Test
    void getCrewByUsernameStatus(){
        this.webTestClient.get().uri("/crew/mycrew")
                .header("Authorization", "Bearer "+this.jwtToken)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void getCrewByUsername(){
        this.webTestClient.get().uri("/crew/mycrew")
                .header("Authorization", "Bearer "+this.jwtToken)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CrewResponse.class)
                .consumeWith(crewResponseEntityExchangeResult -> {
                    val crew = crewResponseEntityExchangeResult.getResponseBody();
                    assertNotNull(crew);
                    crew.getCrewMembers().stream().findFirst().ifPresentOrElse(playerResponse -> {
                        assertEquals( "user", playerResponse.getUsername());
                    }, Assertions::fail);
                });
    }

    @Test
    void getStarFromCrew(){
        this.webTestClient.get().uri("/crew/mycrew")
                .header("Authorization", "Bearer "+this.jwtToken)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CrewResponse.class)
                .consumeWith(crewResponseEntityExchangeResult -> {
                    val crew = crewResponseEntityExchangeResult.getResponseBody();
                    assertNotNull(crew);
                    assertEquals("testStar", crew.getStar().getName());

                });
    }

    @Test
    void getCreditsFromCrew(){
        this.webTestClient.get().uri("/crew/mycrew")
                .header("Authorization", "Bearer "+this.jwtToken)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CrewResponse.class)
                .consumeWith(crewResponseEntityExchangeResult -> {
                    val crew = crewResponseEntityExchangeResult.getResponseBody();
                    assertNotNull(crew);
                    assertEquals(0, crew.getCredits().compareTo(BigDecimal.TWO));
                });
    }

    @Test
    void getActualCargoFromCrew(){
        this.webTestClient.get().uri("/crew/mycrew")
                .header("Authorization", "Bearer "+this.jwtToken)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(CrewResponse.class)
                .consumeWith(crewResponseEntityExchangeResult -> {
                    val crew = crewResponseEntityExchangeResult.getResponseBody();
                    assertNotNull(crew);
                    assertEquals(5.5, crew.getActualCargoVolume());

                });
    }
}
