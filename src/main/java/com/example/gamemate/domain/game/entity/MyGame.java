package com.example.gamemate.domain.game.entity;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.global.audit.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "my_game", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "game_id"})
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MyGame extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(name = "game_id", nullable = false)
    private Game game;

}
