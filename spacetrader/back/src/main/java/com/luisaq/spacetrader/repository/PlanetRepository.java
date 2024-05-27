package com.luisaq.spacetrader.repository;

import com.luisaq.spacetrader.model.world.Planet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlanetRepository extends JpaRepository<Planet, Long> {
}
