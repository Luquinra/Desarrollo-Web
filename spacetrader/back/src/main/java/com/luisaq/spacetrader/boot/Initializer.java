package com.luisaq.spacetrader.boot;

import com.github.javafaker.Faker;
import com.luisaq.spacetrader.mapper.UserMapper;
import com.luisaq.spacetrader.model.economy.PlanetaryMarket;
import com.luisaq.spacetrader.model.economy.PlanetaryProduct;
import com.luisaq.spacetrader.model.economy.Product;
import com.luisaq.spacetrader.model.player.Crew;
import com.luisaq.spacetrader.model.player.Player;
import com.luisaq.spacetrader.model.player.PlayerRole;
import com.luisaq.spacetrader.model.player.Spaceship;
import com.luisaq.spacetrader.model.travel.WormHole;
import com.luisaq.spacetrader.model.user.User;
import com.luisaq.spacetrader.model.world.Planet;
import com.luisaq.spacetrader.model.world.Star;
import com.luisaq.spacetrader.repository.*;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
@Slf4j
@Profile("!integration")
public class Initializer implements ApplicationRunner {

    //STAR CONSTANTS
    private static final Integer MAX_STAR_GENERATION = 40_000;
    private static final Double INHABITED_PLANETS_PROBABILITY = 0.01;
    private static final Integer MAX_COORDINATE = 1_000_000;
    private static final Integer MAX_PLANETS_PER_STAR = 3;

    //TRAVEL CONSTANTS
    private static final Integer MAX_WORMHOLE_CONNECTION = 5;
    private static final Integer MAX_WORMHOLE_TRAVEL_TIME = 10_000;

    //MARKET
    private static final Integer MAX_DEMAND_FACTOR = 1_000_000;
    private static final Integer MAX_OFFER_FACTOR = 1_000_000;
    private static final Integer MIN_PRODUCTS_PER_PLANET = 5;
    private static final Integer MAX_PRODUCTS_PER_PLANET = 20;
    private static final Integer MAX_PLANETARY_STOCK = 1_000_000;

    //MAX PLAYER
    private static final Integer MAX_USER_GENERATION = 100;
    private static final Integer MAX_CREW_GENERATION = 10;
    private static final Integer MIN_PLAYERS_PER_CREW = 3;
    private static final Integer MAX_PLAYERS_PER_CREW = MAX_USER_GENERATION / MAX_CREW_GENERATION;
    private static final Integer MAX_SPACESHIP_GENERATION = 20;
    private static final String USERS_PASSWORD = "123456";
    private static final Integer CREDIT_PER_CREW = 1_000_000;

    //MAX PRODUCT
    private static final Integer MAX_PRODUCT_GENERATION = 500;
    private static final Integer MAX_PRODUCT_VOLUME_GENERATION = 5000;

    //MAX DATABASE CONTROL
    private static final Integer MAX_ENTITY_FLUSH = 1_000;


    private final Faker faker = new Faker();
    private final Random random = new Random();

    @Autowired
    private PasswordEncoder passwordEncoder;


    //Access directly to repository to avoid services only for this class
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StarRepository starRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private WormHoleRepository wormHoleRepository;

    @Autowired
    private SpaceShipRepository spaceShipRepository;

    @Autowired
    private CrewRepository crewRepository;

    @Autowired
    private UserMapper userMapper;

    //Multithreading stuff
    private final ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

    private static final List<String> planetImgs = List.of(
            "assets/planets/1116035287.gif",
            "assets/planets/1158715960.gif",
            "assets/planets/1541041687.gif",
            "assets/planets/1817929103.gif",
            "assets/planets/1938912016.gif",
            "assets/planets/2198851932.gif",
            "assets/planets/2233708166.gif",
            "assets/planets/2305275193.gif",
            "assets/planets/2379702541.gif",
            "assets/planets/2440416149.gif",
            "assets/planets/2628007628.gif",
            "assets/planets/2909608145.gif",
            "assets/planets/3004521324.gif",
            "assets/planets/3141349658.gif",
            "assets/planets/3450553906.gif",
            "assets/planets/3590846384.gif",
            "assets/planets/37952588379.gif",
            "assets/planets/37956588379.gif",
            "assets/planets/3795658837.gif",
            "assets/planets/3795658838.gif",
            "assets/planets/404908008.gif",
            "assets/planets/4164700164.gif",
            "assets/planets/4164808936.gif"
    );

    private static final List<String> starImgs = List.of(
            "assets/stars/148197341.gif",
            "assets/stars/1517759234.gif",
            "assets/stars/1690261463.gif",
            "assets/stars/3095869067.gif",
            "assets/stars/3157880428.gif",
            "assets/stars/3185909642.gif",
            "assets/stars/3601712666.gif",
            "assets/stars/3648760689.gif",
            "assets/stars/3663477929.gif",
            "assets/stars/3877521693.gif",
            "assets/stars/4213315666.gif",
            "assets/stars/546101511.gif"
    );

    private static final List<String> spaceshipImgs = List.of(
            "assets/spaceships/vehicle-1.png",
            "assets/spaceships/vehicle-2.png",
            "assets/spaceships/vehicle-3.png",
            "assets/spaceships/vehicle-4.png",
            "assets/spaceships/vehicle-5.png",
            "assets/spaceships/vehicle-6.png",
            "assets/spaceships/vehicle-7.png",
            "assets/spaceships/vehicle-8.png"
    );

    @Override
    public void run(ApplicationArguments args) {

        log.info("Starting database population...");
        log.info("Starting user population...");
        this.initUsers();
        log.info("Starting product population...");
        this.initProducts();
        log.info("Starting star population...");
        this.initStars();
        log.info("Generating SpaceShips...");
        this.initSpaceShips();
        log.info("Generating crews...");
        this.initCrew();
        log.info("Generating wormholes...");
        this.initWormholes();
        log.info("Database population finished");
        this.logUsers();
    }

    private void initUsers() {
        IntStream.range(0, MAX_USER_GENERATION).parallel().forEach(_ -> {
            val user = User.builder().username(this.faker.name().firstName() + "_" + this.faker.ancient().hero())
                    .password(passwordEncoder.encode(USERS_PASSWORD))
                    .build();
            this.userRepository.save(user);
        });

    }

    private void initProducts() {
        val possible_names = new Supplier[]{
                this.faker.commerce()::productName,
                this.faker.beer()::name,
                this.faker.beer()::yeast,
                this.faker.book()::title,
                this.faker.food()::ingredient,
                this.faker.food()::dish,
                this.faker.food()::sushi,
                this.faker.food()::vegetable,
                this.faker.food()::fruit,
                this.faker.food()::spice,
                this.faker.medical()::medicineName
        };

        for (int i = 0; i < MAX_PRODUCT_GENERATION; i++) {
            try {
                val imgIndex = random.nextInt(20)+1;
                val product = Product.builder()
                        .name(possible_names[random.nextInt(possible_names.length)].get().toString())
                        .volume(random.nextDouble(1, MAX_PRODUCT_VOLUME_GENERATION))
                        .img("assets/products/"+imgIndex+".png")
                        .build();
                this.productRepository.save(product);
            } catch (DataIntegrityViolationException e) {
                //Make all the products unique without too much problem...
                i--;
            }
        }
    }

    private void initStars() {
        val possible_names = new Supplier[]{
                this.faker.ancient()::titan,
                this.faker.ancient()::god,
                this.faker.ancient()::primordial,
                this.faker.ancient()::hero
        };

        IntStream.range(0, MAX_STAR_GENERATION).parallel().forEach(_ -> {
            val star = Star.builder()
                    .name(possible_names[random.nextInt(possible_names.length)].get() + "#" + random.nextInt(10, 10_000_000))
                    .x(random.nextDouble(MAX_COORDINATE))
                    .y(random.nextDouble(MAX_COORDINATE))
                    .z(random.nextDouble(MAX_COORDINATE))
                    .img(starImgs.get(random.nextInt(starImgs.size())))
                    .build();

            if (random.nextDouble() <= INHABITED_PLANETS_PROBABILITY) { // Star with inhabited planets probability
                star.setPlanets(createPlanets(star, random.nextInt(1, MAX_PLANETS_PER_STAR)));
            }
            starRepository.save(star);
        });
    }


    private Set<Planet> createPlanets(Star star, Integer num) {
        val possible_names = new Supplier[]{
                this.faker.dune()::planet,
                this.faker.elderScrolls()::city,
                this.faker.elderScrolls()::firstName,
                this.faker.leagueOfLegends()::champion,
                this.faker.leagueOfLegends()::location,
                this.faker.pokemon()::name,
        };
        val planets = new HashSet<Planet>();
        for (int i = 0; i < num; i++) {
            val productsIter = this.productRepository.getRandomProducts(random.nextLong(MIN_PRODUCTS_PER_PLANET, MAX_PRODUCTS_PER_PLANET));
            val products = productsIter.stream().map(product -> PlanetaryProduct.builder()
                    .product(product)
                    .stock(random.nextLong(1, MAX_PLANETARY_STOCK))
                    .build()
            ).collect(Collectors.toSet());
            val market = PlanetaryMarket.builder()
                    .demandFactor(random.nextDouble(MAX_DEMAND_FACTOR))
                    .offerFactor(random.nextDouble(MAX_OFFER_FACTOR))
                    .products(products)
                    .build();
            val planet = Planet.builder()
                    .name(possible_names[random.nextInt(possible_names.length)].get() + "#" + random.nextInt(1, 100))
                    .market(market)
                    .inhabited(true)
                    .star(star)
                    .crews(new HashSet<>())
                    .img(planetImgs.get(random.nextInt(planetImgs.size())))
                    .build();

            //Link entities
            products.forEach(planetaryProduct -> planetaryProduct.setMarket(market));
            market.setPlanet(planet);

            planets.add(planet);
        }
        return planets;
    }

    private void initSpaceShips() {
        val possible_names = new Supplier[]{
                this.faker.harryPotter()::house,
                this.faker.harryPotter()::spell,
                this.faker.starTrek()::specie
        };

        for (int i = 0; i < MAX_SPACESHIP_GENERATION; i++) {
            try {
                val spaceShip = Spaceship.builder()
                        .name(possible_names[random.nextInt(possible_names.length)].get().toString())
                        .cargoCapacity((double) random.nextLong(1, 500_000))
                        .maxSpeed(random.nextDouble(1, 1_000_000))
                        .img(spaceshipImgs.get(random.nextInt(spaceshipImgs.size())))
                        .build();

                this.spaceShipRepository.save(spaceShip);
            } catch (DataIntegrityViolationException e) {
                //Unique names without problem
                i--;
            }
        }
    }

    private void initCrew() {
        for (int i = 0; i < MAX_CREW_GENERATION; i++) {
            val crewPlayers = new HashSet<Player>();
            val users = this.userRepository.getRandomUserSampleWithoutCrew(10L);

            //Base crew, no players
            val crew = Crew.builder()
                    .credits(BigDecimal.valueOf(CREDIT_PER_CREW))
                    .spaceship(this.spaceShipRepository.getRandomSpaceShip())
                    .build();

            //Portraits
            val availablePortraits = IntStream.rangeClosed(1, 30)
                    .boxed()
                    .collect(Collectors.toList());
            Collections.shuffle(availablePortraits);


            val portrait = availablePortraits.get(random.nextInt(availablePortraits.size()));
            //Captain
            users.stream().limit(1).forEach(user -> crewPlayers.add(Player.builder()
                    .user(user)
                    .crew(crew)
                    .role(PlayerRole.CAPTAIN)
                    .img("assets/portraits/portrait-" + portrait + ".jpg")
                    .build()));
            availablePortraits.remove(portrait);

            val pilotNumber = users.size() / 2;

            //Pilots
            users.stream().skip(1).limit(pilotNumber).forEach(user -> {
                        val internalPortrait = availablePortraits.get(random.nextInt(availablePortraits.size()));
                        crewPlayers.add(Player.builder()
                                .role(PlayerRole.PILOT)
                                .user(user)
                                .crew(crew)
                                .img("assets/portraits/portrait-" + internalPortrait + ".jpg")
                                .build());
                        availablePortraits.remove(internalPortrait);
                    }
            );

            //Traders
            users.stream().skip(1 + pilotNumber).forEach(user -> {
                        val internalPortrait = availablePortraits.get(random.nextInt(availablePortraits.size()));
                        crewPlayers.add(Player.builder()
                                .role(PlayerRole.TRADER)
                                .user(user)
                                .crew(crew)
                                .img("assets/portraits/portrait-" + internalPortrait + ".jpg")
                                .build());
                        availablePortraits.remove(internalPortrait);
                    }
            );

            crew.setCrewMembers(crewPlayers);
            val star = this.starRepository.getRandomStar();
            star.getAnchoredSpaceShips().add(crew);
            crew.setStar(star);
            /*
             * Due to the amount of stars 40_000 vs the stars with planets 400 the prob of random chance of any crew
             * to randomly pick in a star with an inhabited planet is so low that for facility, all crews start in
             * an inhabited planet
             */
            val planet = star.getPlanets().stream().skip(random.nextInt(star.getPlanets().size())).findFirst().orElseThrow();
            planet.getCrews().add(crew);
            crew.setPlanet(planet);
            crew.setActualCargoVolume(0.0);
            crew.setCargo(new HashSet<>());
            this.crewRepository.save(crew);
        }
    }

    private void initWormholes() {
        val pageSize = MAX_STAR_GENERATION / 10;
        var pageNumber = 0;
        var pageable = PageRequest.of(pageNumber, pageSize);
        Page<Star> page;

        do {
            page = this.starRepository.findAll(pageable);

            Page<Star> finalPage = page;
            //Without executors, it would take ages to populate...
            executorService.submit(() -> {
                finalPage.stream().parallel().forEach(star -> {
                    val connections = this.starRepository.findRandomStarsExceptId(star.getId(), random.nextLong(2, MAX_WORMHOLE_CONNECTION));
                    connections.forEach(destinationStar -> this.wormHoleRepository.save(
                                    WormHole.builder()
                                            .sourceStar(star)
                                            .destinationStar(destinationStar)
                                            .travelTime(random.nextDouble(100, MAX_WORMHOLE_TRAVEL_TIME))
                                            .build()
                            )
                    );
                });
            });

            pageNumber++;
            pageable = PageRequest.of(pageNumber, pageSize);
        } while (page.hasNext());
    }

    private void logUsers() {
        this.userRepository.getCaptains(Pageable.ofSize(10)).stream().map(userMapper::modelToResponse).forEach(user -> log.info("User: {}", user));
    }
}
