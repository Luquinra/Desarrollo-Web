package com.luisaq.spacetrader.model.player;

import com.luisaq.spacetrader.model.user.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String img;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Crew crew;

    @Column(nullable = false)
    private PlayerRole role;

}
