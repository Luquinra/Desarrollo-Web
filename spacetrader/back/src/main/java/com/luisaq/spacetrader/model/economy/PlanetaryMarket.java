package com.luisaq.spacetrader.model.economy;

import com.luisaq.spacetrader.model.world.Planet;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.util.Set;

@Entity
@Data
@Builder
@EqualsAndHashCode(exclude = {"planet", "products"})
@AllArgsConstructor
@NoArgsConstructor
public class PlanetaryMarket {

    private static final long MAX_PLANETARY_DEMAND_FACTOR = 1_000_000L;
    private static final long MAX_PLANETARY_OFFER_FACTOR = 1_000_000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn
    @ToString.Exclude
    private Planet planet;

    @OneToMany(cascade = CascadeType.ALL)
    private Set<PlanetaryProduct> products;

    @Column(nullable = false)
    @Min(value = 0, message = "Demand factor cannot be under 0")
    @Max(value = MAX_PLANETARY_DEMAND_FACTOR, message = "Demand factor cannot be greater than 1.000.000")
    private Double demandFactor;

    @Column(nullable = false)
    @Min(value = 0, message = "Offer factor cannot be under 0")
    @Max(value = MAX_PLANETARY_OFFER_FACTOR, message = "Offer factor cannot be greater than 1.000.000")
    private Double offerFactor;


}
