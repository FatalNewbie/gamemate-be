package com.example.gamemate.domain.friend.entity;

import com.example.gamemate.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "friend")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@IdClass(FriendId.class)
public class Friend {

    @Id
    @ManyToOne
    @JoinColumn(name = "requester_id", nullable = false)
    private User requester;

    @Id
    @ManyToOne
    @JoinColumn(name = "receiver_id", nullable = false)
    private User receiver;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    @Column(name = "accepted_date")
    private LocalDateTime acceptedDate;

    public enum Status {
        PENDING, ACCEPTED, REJECTED, CANCELED
    }
}
