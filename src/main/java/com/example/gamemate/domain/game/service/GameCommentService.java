package com.example.gamemate.domain.game.service;

import com.example.gamemate.domain.auth.dto.CustomUserDetailsDTO;
import com.example.gamemate.domain.game.dto.GameCommentDto;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import com.example.gamemate.global.exception.GameExceptionCode;
import com.example.gamemate.global.exception.RestApiException;
import com.example.gamemate.domain.game.mapper.GameCommentMapper;
import com.example.gamemate.domain.game.entity.Game;
import com.example.gamemate.domain.game.entity.GameComment;
import com.example.gamemate.domain.game.repository.GameCommentRepository;
import com.example.gamemate.domain.game.repository.GameRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class GameCommentService {

    private final GameCommentRepository gameCommentRepository;
    private final GameCommentMapper gameCommentMapper;
    private final UserRepository userRepository;
    private final GameRepository gameRepository;

    // 생성자에서 UserRepository 주입 추가
    public GameCommentService(GameCommentRepository gameCommentRepository, GameCommentMapper gameCommentMapper, GameRepository gameRepository, UserRepository userRepository) {
        this.gameCommentRepository = gameCommentRepository;
        this.gameCommentMapper = gameCommentMapper;
        this.gameRepository = gameRepository;
        this.userRepository = userRepository;
    }

    // 댓글 리스트 조회 메서드
    public Page<GameCommentDto> getAllComments(Long gameId, Pageable pageable) {
        if (pageable.isUnpaged()) {
            List<GameComment> comments = gameCommentRepository.findByGameIdAndDeletedDateIsNull(gameId);
            return new PageImpl<>(comments.stream().map(gameCommentMapper::toDto).collect(Collectors.toList()));
        } else {
            Page<GameComment> comments = gameCommentRepository.findByGameIdAndDeletedDateIsNull(gameId, pageable);
            return comments.map(gameCommentMapper::toDto);
        }
    }

    // 댓글 단일 조회 메서드
    public GameCommentDto getCommentById(Long gameId, Long commentId) {
        GameComment comment = gameCommentRepository.findByGameIdAndIdAndDeletedDateIsNull(gameId, commentId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_COMMENT_NOT_FOUND));
        return gameCommentMapper.toDto(comment);
    }

    // 댓글 생성 메서드
    @Transactional
    public GameCommentDto createUserCommentForGame(CustomUserDetailsDTO customUserDetailsDTO, Long gameId, GameCommentDto commentDto) {
        // 현재 로그인된 사용자의 정보를 가져옵니다.
        String username = customUserDetailsDTO.getUsername();

        // UserRepository를 통해 사용자를 조회합니다.
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RestApiException(GameExceptionCode.USER_NOT_FOUND);
        }

        // 게임을 ID로 조회합니다.
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_NOT_FOUND));

        // DTO를 엔티티로 변환하고 사용자 및 게임 정보를 설정합니다.
        GameComment comment = gameCommentMapper.toEntity(commentDto);
        comment.setUser(user); // User 설정
        comment.setGame(game); // Game 설정

        // 댓글을 저장하고, DTO로 변환하여 반환합니다.
        GameComment savedComment = gameCommentRepository.save(comment);
        return gameCommentMapper.toDto(savedComment);
    }

    // 댓글 업데이트 메서드
    @Transactional
    public GameCommentDto updateComment(Long gameId, Long commentId, String username, GameCommentDto commentDto) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new RestApiException(GameExceptionCode.USER_NOT_FOUND);
        }

        GameComment comment = gameCommentRepository.findByGameIdAndIdAndDeletedDateIsNull(gameId, commentId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_COMMENT_NOT_FOUND));

        // username이 일치하는지 확인
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RestApiException(GameExceptionCode.UNAUTHORIZED_ACTION);
        }

        // 필요한 필드만 업데이트
        if (commentDto.getContent() != null && !commentDto.getContent().isEmpty()) {
            comment.setContent(commentDto.getContent());
        }

        GameComment updatedComment = gameCommentRepository.save(comment);
        return gameCommentMapper.toDto(updatedComment);
    }

    // 댓글 삭제 메서드
    public void deleteComment(Long gameId, Long commentId, String username) {
        GameComment comment = gameCommentRepository.findByGameIdAndIdAndDeletedDateIsNull(gameId, commentId)
                .orElseThrow(() -> new RestApiException(GameExceptionCode.GAME_COMMENT_NOT_FOUND));

        // 댓글이 현재 사용자의 것인지 확인
        if (!comment.getUser().getUsername().equals(username)) {
            throw new RestApiException(GameExceptionCode.UNAUTHORIZED_ACTION);
        }

        // Soft delete 처리
        comment.setDeletedDate(LocalDateTime.now());
        gameCommentRepository.save(comment);
    }

    // 회원탈퇴 시 댓글 삭제 메서드
    @Transactional
    public void deleteCommentsByUsername(String username) {
        // 사용자 조회
        User user = userRepository.findByUsername(username);
        log.info(username);

        if (user == null) {
            throw new RestApiException(GameExceptionCode.USER_NOT_FOUND);
        }

        // 해당 username을 가진 유저의 모든 댓글을 조회
        List<GameComment> comments = gameCommentRepository.findCommentByUserId(
            user.getId());
        log.info("findCommentByUserId");

        // 모든 댓글에 대해 soft delete 처리
        for (GameComment comment : comments) {
            comment.setDeletedDate(LocalDateTime.now());
            gameCommentRepository.save(comment);
        }
        log.info("setDeletedComment");
    }
}

