package com.example.gamemate.domain.game.service;

import com.example.gamemate.domain.game.dto.GameCommentDto;
import com.example.gamemate.domain.game.mapper.GameCommentMapper;
import com.example.gamemate.domain.game.entity.Game;
import com.example.gamemate.domain.game.entity.GameComment;
import com.example.gamemate.domain.game.repository.GameCommentRepository;
import com.example.gamemate.domain.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GameCommentService {

    @Autowired
    private GameCommentRepository gameCommentRepository;

    @Autowired
    private GameCommentMapper gameCommentMapper;

    @Autowired
    private GameRepository gameRepository;

    public List<GameCommentDto> getAllComments(Long gameId) {
        List<GameComment> comments = gameCommentRepository.findByGameIdAndDeletedDateIsNull(gameId);
        return comments.stream().map(gameCommentMapper::toDto).collect(Collectors.toList());
    }

    public GameCommentDto getCommentById(Long gameId, Long commentId) {
        GameComment comment = gameCommentRepository.findByGameIdAndIdAndDeletedDateIsNull(gameId, commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        return gameCommentMapper.toDto(comment);
    }

    public GameCommentDto createComment(Long gameId, GameCommentDto commentDto) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new RuntimeException("Game not found"));

        GameComment comment = gameCommentMapper.toEntity(commentDto);
        comment.setGame(game);
        GameComment savedComment = gameCommentRepository.save(comment);
        return gameCommentMapper.toDto(savedComment);
    }

    public GameCommentDto updateComment(Long gameId, Long commentId, GameCommentDto commentDto) {
        GameComment comment = gameCommentRepository.findByGameIdAndIdAndDeletedDateIsNull(gameId, commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        gameCommentMapper.updateEntityFromDto(commentDto, comment);
        GameComment updatedComment = gameCommentRepository.save(comment);
        return gameCommentMapper.toDto(updatedComment);
    }

    public void deleteComment(Long gameId, Long commentId) {
        GameComment comment = gameCommentRepository.findByGameIdAndIdAndDeletedDateIsNull(gameId, commentId)
                .orElseThrow(() -> new RuntimeException("Comment not found"));
        comment.setDeletedDate(LocalDateTime.now()); // Soft delete 처리
        gameCommentRepository.save(comment);
    }
}
