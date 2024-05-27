package com.luisaq.spacetrader.model.player;

import com.luisaq.spacetrader.model.economy.Product;
import com.luisaq.spacetrader.model.world.Planet;
import com.luisaq.spacetrader.model.world.Star;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Set;

@Entity
@Data
@Builder
@EqualsAndHashCode(exclude = {"crewMembers", "spaceship", "cargo", "star"})
@AllArgsConstructor
@NoArgsConstructor
public class Crew {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "crew")
    private Set<Player> crewMembers;

    @ManyToOne
    private Spaceship spaceship;

    @Column(nullable = false)
    private BigDecimal credits;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "crew")
    private Set<CargoItem> cargo;

    //Actual star
    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private Star star;

    //Actual planet (if the star has inhabited planets, else, null)
    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private Planet planet;

    //To avoid calculating the total cargo on each request
    @Column
    private Double actualCargoVolume;

}
