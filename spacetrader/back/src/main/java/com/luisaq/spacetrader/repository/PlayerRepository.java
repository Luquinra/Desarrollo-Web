package com.luisaq.spacetrader.repository;

import com.luisaq.spacetrader.model.player.Player;
import com.luisaq.spacetrader.model.player.PlayerRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PlayerRepository extends JpaRepository<Player, Long> {

    @Query(value = "SELECT p FROM Player p JOIN p.crew c WHERE c.id = :crew_id AND p.role = :role")
    Player getOnePlayerFromCrewWithRole(@Param("crew_id")Long id, @Param("role")PlayerRole role);
}
