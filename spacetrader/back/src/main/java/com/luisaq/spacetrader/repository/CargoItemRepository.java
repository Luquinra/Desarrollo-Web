package com.luisaq.spacetrader.repository;

import com.luisaq.spacetrader.model.player.CargoItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CargoItemRepository extends JpaRepository<CargoItem, Long> {

    @Query("SELECT ci FROM CargoItem ci WHERE ci.product.id = :product_id")
    Optional<CargoItem> getCargoItemByProductId( @Param("product_id") Long id);
}
