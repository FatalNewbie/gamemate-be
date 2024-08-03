package com.example.gamemate.domain.game.controller;

import com.example.gamemate.domain.game.dto.GameCommentDto;
import com.example.gamemate.domain.game.service.GameCommentService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/games/{gameId}/comments")
public class GameCommentController {

    @Autowired
    private GameCommentService gameCommentService;

    @GetMapping
    public List<GameCommentDto> getAllComments(@PathVariable Long gameId) {
        return gameCommentService.getAllComments(gameId);
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<GameCommentDto> getCommentById(@PathVariable Long gameId, @PathVariable Long commentId) {
        GameCommentDto commentDto = gameCommentService.getCommentById(gameId, commentId);
        return ResponseEntity.ok(commentDto);
    }

    @PostMapping
    public ResponseEntity<GameCommentDto> createComment(@PathVariable Long gameId, @RequestBody GameCommentDto commentDto) {
        GameCommentDto newComment = gameCommentService.createComment(gameId, commentDto);
        return ResponseEntity.ok(newComment);
    }

    @PutMapping("/{commentId}")
    public ResponseEntity<GameCommentDto> updateComment(@PathVariable Long gameId, @PathVariable Long commentId, @RequestBody GameCommentDto commentDto) {
        GameCommentDto updatedComment = gameCommentService.updateComment(gameId, commentId, commentDto);
        return ResponseEntity.ok(updatedComment);
    }

    @Transactional
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long gameId, @PathVariable Long commentId) {
        gameCommentService.deleteComment(gameId, commentId);
        return ResponseEntity.noContent().build();
    }
}
