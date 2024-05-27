package com.luisaq.spacetrader.repository;

import com.luisaq.spacetrader.model.player.Spaceship;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SpaceShipRepository extends JpaRepository<Spaceship, Long> {

    @Query(value = "SELECT * FROM SPACESHIP ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Spaceship getRandomSpaceShip();

}
