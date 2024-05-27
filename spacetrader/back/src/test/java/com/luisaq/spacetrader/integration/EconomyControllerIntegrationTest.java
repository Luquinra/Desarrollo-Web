package com.luisaq.spacetrader.integration;

import com.luisaq.spacetrader.IntegrationTest;
import com.luisaq.spacetrader.dto.request.auth.LoginRequest;
import com.luisaq.spacetrader.dto.request.economy.BuyRequest;
import com.luisaq.spacetrader.dto.response.auth.LoginResponse;
import com.luisaq.spacetrader.dto.response.economy.PlanetaryMarketResponse;
import com.luisaq.spacetrader.model.economy.PlanetaryMarket;
import com.luisaq.spacetrader.model.economy.PlanetaryProduct;
import com.luisaq.spacetrader.model.economy.Product;
import com.luisaq.spacetrader.model.player.*;
import com.luisaq.spacetrader.model.user.User;
import com.luisaq.spacetrader.model.world.Planet;
import com.luisaq.spacetrader.model.world.Star;
import com.luisaq.spacetrader.repository.*;
import jakarta.transaction.Transactional;
import lombok.val;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.math.BigDecimal;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@IntegrationTest
public class EconomyControllerIntegrationTest {

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

    @Autowired
    private PlanetRepository planetRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PlanetaryProductRepository planetaryProductRepository;

    @Autowired
    private SpaceShipRepository spaceShipRepository;



    private WebTestClient webTestClient;
    private String jwtToken;

    @BeforeAll
    void init(){
        val passwordEncoder = new BCryptPasswordEncoder();
        val crew = crewRepository.save(Crew.builder().credits(BigDecimal.valueOf(100)).actualCargoVolume(0.0).build());
        val user = userRepository.save(User.builder().username("user")
                .password(passwordEncoder.encode("123"))
                .build());
        val player = playerRepository.save(Player.builder().role(PlayerRole.CAPTAIN)
                .crew(crew).user(user).img("img").build());
        user.setPlayer(player);

        val star = this.starRepository.save(Star.builder()
                .z(1.0).y(1.0).x(1.0).img("none").name("testStar").build());

        val product1 = this.productRepository.save(Product.builder().img("none").name("testProduct").volume(1.0).build());
        val product2 = this.productRepository.save(Product.builder().img("none").name("testProduct2").volume(1.0).build());
        val market = PlanetaryMarket.builder().demandFactor(10.0).offerFactor(10.0).build();
        val planet1 = Planet.builder().star(star).name("planet1").img("none")
                .market(market)
                .inhabited(true).build();

        market.setPlanet(planet1);
        market.setProducts(Set.of(
                PlanetaryProduct.builder().product(product1).stock(100L).market(market).build(),
                PlanetaryProduct.builder().product(product2).stock(10L).market(market).build()
        ));

        this.planetRepository.save(planet1);

        crew.setStar(star);
        crew.setPlanet(planet1);
        val spaceship = this.spaceShipRepository.save(Spaceship.builder()
                .name("testShip").img("none").cargoCapacity(50.0).maxSpeed(1.0).build());
        crew.setSpaceship(spaceship);
        this.crewRepository.save(crew);


        webTestClient =  WebTestClient.bindToServer().baseUrl("http://127.0.0.1:" + port).build();
        this.getAuthToken();
        webTestClient =  WebTestClient.bindToServer().baseUrl("http://127.0.0.1:" + port)
                .defaultHeader("Authorization", "Bearer "+ jwtToken)
                .build();
    }

    private void getAuthToken(){
        val loginRequest = LoginRequest.builder().username("user").password("123").build();
        this.webTestClient.post().uri("/auth/login").bodyValue(loginRequest)
                .exchange()
                .expectStatus().isOk()
                .expectBody(LoginResponse.class)
                .consumeWith(entityExchangeResult -> jwtToken = entityExchangeResult.getResponseBody().getToken());
    }

    @AfterEach
    void reset(){
        val crew = this.crewRepository.findById(1L).orElseThrow();
        crew.setCredits(BigDecimal.valueOf(100));
        this.crewRepository.save(crew);
    }

    @Test
    void getPlanetaryMarket(){
        this.webTestClient.get().uri("/economy/planet_market")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlanetaryMarketResponse.class)
                .consumeWith(objectEntityExchangeResult -> {
                    val market = objectEntityExchangeResult.getResponseBody();
                    assertNotNull(market);
                    assertEquals(2, market.getProducts().size());
                });
    }

    @Test
    void checkPlanetaryMarketProductNames(){
        this.webTestClient.get().uri("/economy/planet_market")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectBody(PlanetaryMarketResponse.class)
                .consumeWith(objectEntityExchangeResult -> {
                    val market = objectEntityExchangeResult.getResponseBody();
                    assertNotNull(market);
                    var search = market.getProducts().stream()
                            .filter(planetaryProductResponse -> planetaryProductResponse.getProduct()
                                    .getName().equals("testProduct")).toList();
                    assertEquals(1, search.size());
                    search = market.getProducts().stream()
                            .filter(planetaryProductResponse -> planetaryProductResponse.getProduct()
                                    .getName().equals("testProduct2")).toList();

                    assertEquals(1, search.size());
                });
    }

    @Test
    @Transactional
    void buyProduct(){
        val buyRequest = BuyRequest.builder().quantity(4L).productMarketId(1L).build();
        this.webTestClient.post().uri("/economy/buy")
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(buyRequest)
                .exchange()
                .expectStatus().isAccepted();

        val crew = this.crewRepository.findById(1L).orElseThrow();
        val item = crew.getCargo().stream().filter(cargoItem -> cargoItem.getId().equals(1L)).findFirst().orElseThrow();
        assertEquals(4, item.getQuantity());
        assertEquals(4, crew.getActualCargoVolume());
    }
}
