package com.example.gamemate.domain.friend.entity;

import java.io.Serializable;
import java.util.Objects;

public class FriendId implements Serializable {
    private Long requester;
    private Long receiver;

    public FriendId() {
    }

    public FriendId(Long requester, Long receiver) {
        this.requester = requester;
        this.receiver = receiver;
    }

    public Long getRequester() {
        return requester;
    }

    public void setRequester(Long requester) {
        this.requester = requester;
    }

    public Long getReceiver() {
        return receiver;
    }

    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendId friendId = (FriendId) o;
        return Objects.equals(requester, friendId.requester) && Objects.equals(receiver, friendId.receiver);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requester, receiver);
    }
}
