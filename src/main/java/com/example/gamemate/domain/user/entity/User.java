package com.example.gamemate.domain.user.entity;

import com.example.gamemate.global.audit.BaseEntity;
import com.example.gamemate.global.converter.JsonConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    @Email(message = "이메일 형식이 유효하지 않습니다.")
    private String username;

    @Column(nullable = false, length = 100)
    private String password;

    @Column(nullable = false, length = 20)
    private String nickname;

    @Enumerated(EnumType.STRING)
    private RoleType role = RoleType.ROLE_USER;

    public enum RoleType {
        ROLE_ADMIN, ROLE_USER
    }

    @Column(name = "deleted")
    private boolean deleted = false;

    @ManyToMany
    @JoinTable(
            name = "user_genres",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id")
    )
    private List<Genre> preferredGenres;

    @ManyToMany
    @JoinTable(
            name = "user_playtimes",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "playtime_id")
    )
    private List<PlayTime> playTimes;
}
