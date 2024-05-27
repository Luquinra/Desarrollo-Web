package com.luisaq.spacetrader.model.world;

import com.luisaq.spacetrader.model.player.Crew;
import com.luisaq.spacetrader.model.travel.WormHole;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Entity
@Table(uniqueConstraints = {
        @UniqueConstraint(columnNames = {"x", "y", "z"}) //2 planets cannot overlap itself
})
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"planets", "wormHoles", "anchoredSpaceShips"})
public class Star {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, precision = 2)
    private Double x;

    @Column(nullable = false, precision = 2)
    private Double y;

    @Column(nullable = false, precision = 2)
    private Double z;

    @Column(nullable = false)
    private String img;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "star", fetch = FetchType.EAGER)
    private Set<Planet> planets;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "sourceStar")
    private Set<WormHole> wormHoles;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "star", fetch = FetchType.EAGER)
    private Set<Crew> anchoredSpaceShips ;
}
