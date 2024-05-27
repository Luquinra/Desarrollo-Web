package com.luisaq.spacetrader.repository;

import com.luisaq.spacetrader.model.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    //INITIALIZER USE
    @Query(value = "SELECT u.* FROM _USER as u LEFT JOIN PLAYER as p ON u.id = p.user_id WHERE p.id IS NULL ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    Collection<User> getRandomUserSampleWithoutCrew(@Param("limit") Long limit);

    @Query(value = "SELECT u FROM User u JOIN u.player p WHERE p.role = 2")
    List<User> getCaptains(Pageable page);

    Optional<User> findByUsername(String username);

}
