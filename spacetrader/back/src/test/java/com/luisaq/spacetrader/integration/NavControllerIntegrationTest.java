package com.luisaq.spacetrader.integration;

import com.luisaq.spacetrader.IntegrationTest;
import com.luisaq.spacetrader.dto.request.auth.LoginRequest;
import com.luisaq.spacetrader.dto.request.travel.TravelRequest;
import com.luisaq.spacetrader.dto.response.auth.LoginResponse;
import com.luisaq.spacetrader.dto.response.travel.WormHoleResponse;
import com.luisaq.spacetrader.dto.response.world.PlanetResponse;
import com.luisaq.spacetrader.dto.validation.TravelType;
import com.luisaq.spacetrader.model.player.Crew;
import com.luisaq.spacetrader.model.player.Player;
import com.luisaq.spacetrader.model.player.PlayerRole;
import com.luisaq.spacetrader.model.travel.WormHole;
import com.luisaq.spacetrader.model.user.User;
import com.luisaq.spacetrader.model.world.Planet;
import com.luisaq.spacetrader.model.world.Star;
import com.luisaq.spacetrader.repository.*;
import lombok.val;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class NavControllerIntegrationTest {

    @LocalServerPort
    private int port ;

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private WormHoleRepository wormHoleRepository;


    private static String jwtToken;
    private WebTestClient webTestClient;

    private static final ParameterizedTypeReference<List<PlanetResponse>> parameterizedTypeReferencePlanetResponse =
            new ParameterizedTypeReference<>() {
            };

    private static final ParameterizedTypeReference<List<WormHoleResponse>> parameterizedTypeReferenceWormholeResponse =
            new ParameterizedTypeReference<>() {
            };


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

        val otherCrew = crewRepository.save(Crew.builder().credits(BigDecimal.TWO).actualCargoVolume(5.5).build());
        val otherUser = userRepository.save(User.builder().username("otherUser")
                .password(passwordEncoder.encode("123"))
                .build());
        val otherPlayer = playerRepository.save(Player.builder().role(PlayerRole.CAPTAIN)
                .crew(otherCrew).user(otherUser).img("img").build());
        otherUser.setPlayer(otherPlayer);


        val star = this.starRepository.save(Star.builder()
                .z(1.0).y(1.0).x(1.0).img("none").name("testStar").build());
        val otherStar = this.starRepository.save(Star.builder()
                .z(2.0).y(2.0).x(2.0).img("none").name("testStar2").build());

        val planet1 = this.planetRepository.save(Planet.builder().star(star).name("planet1").img("none").inhabited(true).build());
        val planet2 = this.planetRepository.save(Planet.builder().star(star).name("planet2").img("none").inhabited(true).build());
        val planet3 = this.planetRepository.save(Planet.builder().star(otherStar).name("planet3").img("none").inhabited(true).build());

        crew.setStar(star);
        crew.setPlanet(planet1);
        this.crewRepository.save(crew);
        otherCrew.setStar(star);
        otherCrew.setPlanet(planet2);
        this.crewRepository.save(otherCrew);

        val wormHole = WormHole.builder().sourceStar(star).destinationStar(otherStar).travelTime(100.0).build();
        this.wormHoleRepository.save(wormHole);

        webTestClient =  WebTestClient.bindToServer().baseUrl("http://127.0.0.1:" + port).build();
        this.getAuthToken();
        webTestClient =  WebTestClient.bindToServer().baseUrl("http://127.0.0.1:" + port)
                .defaultHeader("Authorization", "Bearer "+ jwtToken)
                .build();
    }

    @AfterEach
    void reset(){
        val crew = this.crewRepository.findById(1L).orElseThrow();
        val star = this.starRepository.findById(1L).orElseThrow();
        val planet1 = this.planetRepository.findById(1L).orElseThrow();
        crew.setStar(star);
        crew.setPlanet(planet1);
        this.crewRepository.save(crew);
    }

    private void getAuthToken(){
        val loginRequest = LoginRequest.builder().username("user").password("123").build();
        this.webTestClient.post().uri("/auth/login").bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .consumeWith(entityExchangeResult -> jwtToken = entityExchangeResult.getResponseBody().getToken());
    }

    @Test
    void getStarPlanetSize(){
        this.webTestClient.get().uri("/nav/star_planets")
                .exchange()
                .expectBody(parameterizedTypeReferencePlanetResponse)
                .consumeWith(iterableEntityExchangeResult -> {
                    val response = iterableEntityExchangeResult.getResponseBody();
                    assertNotNull(response);
                    assertEquals(2, response.size());
                });
    }

    @Test
    void travelStar(){
        val travelRest = TravelRequest.builder().starOrPlanetId(2L).type(TravelType.STAR).build();
        this.webTestClient.post().uri("/nav/travel")
                .bodyValue(travelRest)
                .exchange()
                .expectStatus().isAccepted();

        this.crewRepository.findById(1L).ifPresentOrElse(crew -> {
            assertEquals("testStar2",crew.getStar().getName());
        }, Assertions::fail);
    }

    @Test
    void travelPlanet(){
        val travelRest = TravelRequest.builder().starOrPlanetId(2L).type(TravelType.PLANET).build();
        this.webTestClient.post().uri("/nav/travel")
                .bodyValue(travelRest)
                .exchange()
                .expectStatus().isAccepted();

        this.crewRepository.findById(1L).ifPresentOrElse(crew -> {
            assertEquals("planet2",crew.getPlanet().getName());
        }, Assertions::fail);
    }

    @Test
    void getClosestStars(){
        this.webTestClient.get().uri("/nav/status")
                .exchange()
                .expectStatus().isOk()
                .expectBody(parameterizedTypeReferenceWormholeResponse)
                .consumeWith(listEntityExchangeResult -> {
                    val wormholes = listEntityExchangeResult.getResponseBody();
                    assertNotNull(wormholes);

                    assertEquals("testStar2", wormholes.getLast().getDestinationStar().getName());
                    assertEquals(100.0, wormholes.getLast().getTravelTime());
                });
    }

}
