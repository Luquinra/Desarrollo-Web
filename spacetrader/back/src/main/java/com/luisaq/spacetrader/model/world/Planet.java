package com.luisaq.spacetrader.model.world;

import com.luisaq.spacetrader.model.economy.PlanetaryMarket;
import com.luisaq.spacetrader.model.player.Crew;
import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@EqualsAndHashCode(exclude = {"star", "market", "crews"})
@AllArgsConstructor
@NoArgsConstructor
public class Planet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private Boolean inhabited;

    @Column(nullable = false)
    private String img;

    @ManyToOne
    @JoinColumn(name = "star_id", nullable = false)
    @ToString.Exclude
    private Star star;

    @OneToOne(cascade = CascadeType.ALL)
    private PlanetaryMarket market;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "planet", fetch = FetchType.EAGER)
    @ToString.Exclude
    private Set<Crew> crews;

}
