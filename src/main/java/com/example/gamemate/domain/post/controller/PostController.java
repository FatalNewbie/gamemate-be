package com.example.gamemate.domain.post.controller;


import com.amazonaws.services.ec2.model.UserData;
import com.example.gamemate.domain.post.dto.PostDTO;
import com.example.gamemate.domain.post.dto.PostUpdateDTO;
import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.post.dto.PostResponseDTO;
import com.example.gamemate.domain.post.entity.PostComment;
import com.example.gamemate.domain.post.service.PostService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.objenesis.ObjenesisHelper;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Post", description = "Post API")
@Slf4j
@RequestMapping("/posts")
@RestController
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
    public ResponseEntity<PostResponseDTO> getPostWithComments(@PathVariable Long id,
                                                               @AuthenticationPrincipal UserDetails userDetails){

        PostResponseDTO post = postService.readPost(userDetails.getUsername(), id);

        return ResponseEntity.ok(post);
    }

    //글 작성 api
    @PostMapping("/post")
    public ResponseEntity<PostResponseDTO> registerPost(@Valid @RequestBody PostDTO postDTO,
                                               @AuthenticationPrincipal UserDetails userDetails){

        postService.createPost(userDetails.getUsername(), postDTO);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    //글 수정 api
    @PutMapping("/post/{id}")
    public ResponseEntity<PostResponseDTO> editPost(@PathVariable Long id,
                                           @Valid @RequestBody PostUpdateDTO postUpdateDTO,
                                           @AuthenticationPrincipal UserDetails userDetails){

        PostResponseDTO postResponseDTO = postService.updatePost(userDetails.getUsername(), id, postUpdateDTO);

        return ResponseEntity.ok(postResponseDTO);
    }

    //글 삭제 api
    @Transactional
    @DeleteMapping("post/{id}")
    public ResponseEntity<PostResponseDTO> removePost(@PathVariable Long id,
                                             @AuthenticationPrincipal UserDetails userDetails){

        postService.deletePost(userDetails.getUsername(), id);

        return ResponseEntity.noContent().build();
    }

}