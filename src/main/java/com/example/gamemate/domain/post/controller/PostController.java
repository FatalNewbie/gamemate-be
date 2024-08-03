package com.example.gamemate.domain.post.controller;


import com.example.gamemate.domain.post.dto.PostDTO;
import com.example.gamemate.domain.post.dto.PostUpdateDTO;
import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.post.dto.PostResponseDTO;
import com.example.gamemate.domain.post.entity.PostComment;
import com.example.gamemate.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.objenesis.ObjenesisHelper;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@Tag(name = "Post", description = "Post API")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService){
        this.postService = postService;
    }


    //온라인 글 List 조회 api
    @GetMapping("/online")
    public ResponseEntity<Page<PostResponseDTO>> getAllOnlinePosts(@PageableDefault(size = 10) Pageable pageable){

        Page<PostResponseDTO> onlinePosts = postService.readPostsOnline(pageable);

        return ResponseEntity.ok(onlinePosts);
    }

    //오프라인 글 List 조회 api
    @GetMapping("/offline")
    public ResponseEntity<Page<PostResponseDTO>> getAllOfflinePosts(@PageableDefault(size = 10) Pageable pageable){

        Page<PostResponseDTO> offlinePosts = postService.readPostsOffline(pageable);

        return ResponseEntity.ok(offlinePosts);
    }

    //글 조회 api
    @GetMapping("/post/{id}")
    public ResponseEntity<PostResponseDTO> getPostWithComments(@PathVariable Long id){
        PostResponseDTO post = postService.readPost(id);
        return ResponseEntity.ok(post);
    }

    //글 작성 api
    @PostMapping("/post")
    public ResponseEntity<Object> registerPost(@Valid @RequestBody PostDTO postDTO){

        postService.createPost(postDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //글 수정 api
    @PutMapping("/post/{id}")
    public ResponseEntity<Object> editPost(@PathVariable Long id, @Valid @RequestBody PostUpdateDTO postUpdateDTO){

        postService.updatePost(id, postUpdateDTO);

        return ResponseEntity.ok().build();
    }

    //글 삭제 api
    @DeleteMapping("post/{id}")
    public ResponseEntity<Object> removePost(@PathVariable Long id){

        postService.deletePost(id);

        return ResponseEntity.ok().build();
    }

}