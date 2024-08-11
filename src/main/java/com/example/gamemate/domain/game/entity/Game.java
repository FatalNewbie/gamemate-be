package com.example.gamemate.domain.game.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import com.example.gamemate.global.audit.BaseEntity;

import java.time.LocalDateTime;
import java.util.List;

@Table(name = "game")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
public class Game extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 100)
    private String developer;

    @Column(length = 100)
    private String genre;

    @Column(length = 100)
    private String platform;

    @Column(length = 100)
    private String classes;

    @Column(columnDefinition = "TEXT")
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column
    private LocalDateTime deletedDate;

    @OneToMany(mappedBy = "game", orphanRemoval = true)
    private List<GameComment> comments;

    @OneToMany(mappedBy = "game", orphanRemoval = true)
    private List<GameRating> ratings;
}
