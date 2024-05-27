package com.luisaq.spacetrader.repository;

import com.luisaq.spacetrader.model.economy.PlanetaryProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PlanetaryProductRepository  extends JpaRepository<PlanetaryProduct, Long> {

    @Query("SELECT p FROM PlanetaryProduct p WHERE p.product.id = :product_id AND p.market.id = :market_id")
    Optional<PlanetaryProduct> getPlanetaryProductByProductIdAndMarketId(@Param("product_id") Long productId, @Param("market_id") Long marketId);
}
