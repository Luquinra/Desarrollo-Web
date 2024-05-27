package com.luisaq.spacetrader.model.economy;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

@Builder
@Data
@Entity
@EqualsAndHashCode(exclude = {"product", "market"})
@AllArgsConstructor
@NoArgsConstructor
public class PlanetaryProduct {
    private static final long MAX_PLANETARY_STOCK = 1_000_000L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    private Product product;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private PlanetaryMarket market;

    @Column(nullable = false)
    @Min(value = 0, message = "Min value for planetary stock is 0, no debs")
    @Max(value = MAX_PLANETARY_STOCK, message = "Max storage size for planetary stock is 1.000.000")
    private Long stock;
}
