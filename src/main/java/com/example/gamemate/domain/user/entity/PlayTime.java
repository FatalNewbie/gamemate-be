package com.example.gamemate.domain.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Table(name = "playtime")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class PlayTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique=true)
    private String timeSlot;

    @ManyToMany(mappedBy = "playTimes")
    private List<User> users;
}
