package com.luisaq.spacetrader.repository;

import com.luisaq.spacetrader.model.player.CargoItem;
import com.luisaq.spacetrader.model.player.Crew;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CrewRepository extends JpaRepository<Crew, Long> {

    @Query("SELECT c FROM Crew c JOIN c.crewMembers p JOIN p.user u WHERE u.username = :username")
    Crew findCrewsByUserUsername(@Param("username") String username);

    @Query("SELECT ci FROM Crew c JOIN c.cargo ci WHERE c.id = :crew_id AND ci.id = :cargo_item_id")
    Optional<CargoItem> getCargoByIdFromCrew(@Param("crew_id") Long crewId, @Param("cargo_item_id") Long cargoItemId);
}
