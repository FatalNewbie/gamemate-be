package com.example.gamemate.domain.game.controller;

import com.example.gamemate.domain.game.dto.GameRatingDto;
import com.example.gamemate.domain.game.service.GameRatingService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/games/{gameId}/ratings")
public class GameRatingController {

    @Autowired
    private GameRatingService gameRatingService;

    @GetMapping
    public List<GameRatingDto> getAllRatings(@PathVariable Long gameId) {
        return gameRatingService.getAllRatings(gameId);
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<GameRatingDto> getRatingById(@PathVariable Long gameId, @PathVariable Long ratingId) {
        GameRatingDto ratingDto = gameRatingService.getRatingById(gameId, ratingId);
        return ResponseEntity.ok(ratingDto);
    }

    @PostMapping
    public ResponseEntity<GameRatingDto> createRating(@PathVariable Long gameId, @RequestBody GameRatingDto ratingDto) {
        GameRatingDto newRating = gameRatingService.createRating(gameId, ratingDto);
        return ResponseEntity.ok(newRating);
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<GameRatingDto> updateRating(@PathVariable Long gameId, @PathVariable Long ratingId, @RequestBody GameRatingDto ratingDto) {
        GameRatingDto updatedRating = gameRatingService.updateRating(gameId, ratingId, ratingDto);
        return ResponseEntity.ok(updatedRating);
    }

    @Transactional
    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long gameId, @PathVariable Long ratingId) {
        gameRatingService.deleteRating(gameId, ratingId);
        return ResponseEntity.noContent().build();
    }
}
