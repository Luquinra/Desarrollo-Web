package com.luisaq.spacetrader.model.player;

import com.luisaq.spacetrader.model.economy.Product;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"product", ""})
public class CargoItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(nullable = false)
    @ManyToOne
    @ToString.Exclude
    private Product product;

    @Column(nullable = false)
    private Long quantity;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Crew crew;

}
