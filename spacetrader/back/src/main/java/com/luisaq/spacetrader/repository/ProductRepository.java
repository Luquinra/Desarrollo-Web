package com.luisaq.spacetrader.repository;

import com.luisaq.spacetrader.model.economy.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query(value = "SELECT * FROM PRODUCT ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    Collection<Product> getRandomProducts(@Param("limit") Long limit);
}
