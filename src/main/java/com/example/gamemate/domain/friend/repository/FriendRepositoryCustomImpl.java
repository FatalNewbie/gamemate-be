package com.example.gamemate.domain.friend.repository;

import com.example.gamemate.domain.friend.entity.Friend;
import com.example.gamemate.domain.friend.entity.QFriend;
import com.example.gamemate.domain.user.entity.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Repository
public class FriendRepositoryCustomImpl implements FriendRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Autowired
    public FriendRepositoryCustomImpl(EntityManager entityManager) {
        this.queryFactory = new JPAQueryFactory(entityManager);
    }

    @Override
    public List<Friend> findFriendsByStatus(Long userId, Friend.Status status) {
        QFriend friend = QFriend.friend;

        return queryFactory.selectFrom(friend)
                .where(friend.status.eq(status)
                        .and(friend.requester.id.eq(userId).or(friend.receiver.id.eq(userId))))
                .fetch();
    }

    @Override
    public List<User> findFriendUsersByUserId(Long userId, Friend.Status status) {
        QFriend friend = QFriend.friend;

        List<User> friendsAsRequester = queryFactory.select(friend.receiver)
                .from(friend)
                .where(friend.status.eq(status)
                        .and(friend.requester.id.eq(userId)))
                .fetch();

        List<User> friendsAsReceiver = queryFactory.select(friend.requester)
                .from(friend)
                .where(friend.status.eq(status)
                        .and(friend.receiver.id.eq(userId)))
                .fetch();

        return Stream.concat(friendsAsRequester.stream(), friendsAsReceiver.stream())
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Friend> findFriendRelationship(Long requesterId, Long receiverId) {
        QFriend friend = QFriend.friend;

        return Optional.ofNullable(queryFactory.selectFrom(friend)
                .where(friend.requester.id.eq(requesterId).and(friend.receiver.id.eq(receiverId))
                        .or(friend.requester.id.eq(receiverId).and(friend.receiver.id.eq(requesterId))))
                .fetchOne());
    }

    @Override
    public List<Friend> findPendingRequestsByReceiverId(Long receiverId) {
        QFriend friend = QFriend.friend;

        return queryFactory.selectFrom(friend)
                .where(friend.receiver.id.eq(receiverId).and(friend.status.eq(Friend.Status.PENDING)))
                .fetch();
    }
}

