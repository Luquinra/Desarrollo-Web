package com.luisaq.spacetrader.model.travel;

import com.luisaq.spacetrader.model.world.Star;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(exclude = {"sourceStar", "destinationStar"})
public class WormHole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Double travelTime; //Time and not length due to all ships going at c so any calculations are simply useless

    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private Star sourceStar;

    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private Star destinationStar;

    
}
