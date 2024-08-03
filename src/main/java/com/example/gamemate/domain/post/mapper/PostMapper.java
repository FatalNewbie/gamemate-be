package com.example.gamemate.domain.post.mapper;


import com.example.gamemate.domain.post.dto.OfflinePostDTO;
import com.example.gamemate.domain.post.dto.OnlinePostDTO;
import com.example.gamemate.domain.post.entity.Post;
import com.example.gamemate.domain.post.dto.PostResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    Post OfflinePostRequestToPost(OfflinePostDTO offlinePostDTO);

    Post OnlinePostRequestToPost(OnlinePostDTO onlinePostRequest);

    PostResponseDTO PostToOfflinePostResponse(Post post);
}