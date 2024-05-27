package com.luisaq.spacetrader.model.player;

import com.luisaq.spacetrader.model.economy.Product;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Spaceship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name;

    @Column(nullable = false)
    private String img;

    @Column(nullable = false)
    @Min(value = 0, message = "Min vol value is 0")
    private Double cargoCapacity;

    @Column(nullable = false)
    @Min(value = 0, message = "Min vel is 0")
    private Double maxSpeed;

}
