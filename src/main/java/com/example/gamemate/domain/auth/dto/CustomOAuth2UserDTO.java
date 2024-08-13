//package com.example.gamemate.domain.auth.dto;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Map;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//public class CustomOAuth2UserDTO implements OAuth2User {
//
//    private final OAuth2DTO oAuth2DTO;
//
//    public CustomOAuth2UserDTO(OAuth2DTO oAuth2DTO) {
//
//        this.oAuth2DTO = oAuth2DTO;
//
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//
//        return null;
//
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//
//        Collection<GrantedAuthority> collection = new ArrayList<>();
//
//        collection.add(new GrantedAuthority() {
//            @Override
//            public String getAuthority() {
//
//                return oAuth2DTO.getRole();
//
//            }
//        });
//
//        return collection;
//
//    }
//
//    public Long getId() {
//
//        return oAuth2DTO.getId();
//
//    }
//
//    public String getUsername() {
//
//        return oAuth2DTO.getUsername();
//
//    }
//
//    @Override
//    public String getName() {
//
//        return oAuth2DTO.getNickname();
//
//    }
//
//    public String getRole() {
//
//        return oAuth2DTO.getRole();
//
//    }
//}
