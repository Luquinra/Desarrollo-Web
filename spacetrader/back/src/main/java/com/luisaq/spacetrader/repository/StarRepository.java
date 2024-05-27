package com.luisaq.spacetrader.repository;

import com.luisaq.spacetrader.model.player.Crew;
import com.luisaq.spacetrader.model.world.Star;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface StarRepository extends JpaRepository <Star, Long> {

    //Initializer use
    @Query(value = "SELECT * FROM STAR WHERE id <> :id ORDER BY RAND() LIMIT :limit", nativeQuery = true)
    List<Star> findRandomStarsExceptId(@Param("id") Long id, @Param("limit") Long limit);

    //Random star with inhabited planets
    @Query(value = "SELECT s.* FROM STAR s INNER JOIN PLANET p on s.id = p.star_id ORDER BY RAND() LIMIT 1", nativeQuery = true)
    Star getRandomStar();

    @Query("SELECT c FROM Crew c JOIN c.star s WHERE s.id = :star_id AND c.id != :crew_id")
    Collection<Crew> getAnchoredCrewsOnStarExceptId(@Param("star_id") Long starId, @Param("crew_id")Long crewId);
}
