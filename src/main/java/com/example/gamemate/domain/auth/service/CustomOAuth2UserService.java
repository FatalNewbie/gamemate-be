package com.example.gamemate.domain.auth.service;

import com.example.gamemate.domain.auth.dto.CustomOAuth2UserDTO;
import com.example.gamemate.domain.auth.dto.GoogleResponse;
import com.example.gamemate.domain.auth.dto.NaverResponse;
import com.example.gamemate.domain.auth.dto.OAuth2DTO;
import com.example.gamemate.domain.auth.dto.OAuth2Response;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.user.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    public CustomOAuth2UserService(UserRepository userRepository) {

        this.userRepository = userRepository;

    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        OAuth2User oAuth2User = super.loadUser(userRequest);

        log.info(String.valueOf(oAuth2User));

        String registrationId = userRequest.getClientRegistration()
                                            .getRegistrationId();
        OAuth2Response oAuth2Response = null;

        if(registrationId.equals("naver")) {

            oAuth2Response = new NaverResponse(oAuth2User.getAttributes());

        } else if(registrationId.equals("google")) {

            oAuth2Response = new GoogleResponse(oAuth2User.getAttributes());

        } else {

            return null;

        }

        //리소스 서버에서 발급받은 정보로 사용자를 특정할 아이디값을 만듦
        String username = oAuth2Response.getProvider() + " " + oAuth2Response.getProviderId();

        User existData = userRepository.findByUsername(username);

        if(existData == null) {

            User user = new User();

            user.setUsername(oAuth2Response.getEmail());
            user.setNickname(oAuth2Response.getName());

            userRepository.save(user);

            OAuth2DTO oAuth2DTO = new OAuth2DTO();

            oAuth2DTO.setUsername(username);
            oAuth2DTO.setNickname(oAuth2Response.getName());

            return new CustomOAuth2UserDTO(oAuth2DTO);

        } else {

            existData.setUsername(oAuth2Response.getEmail());
            existData.setNickname(oAuth2Response.getName());

            userRepository.save(existData);

            OAuth2DTO oAuth2DTO = new OAuth2DTO();
            oAuth2DTO.setUsername(existData.getUsername());
            oAuth2DTO.setNickname(oAuth2Response.getName());
            oAuth2DTO.setRole("ROLE_USER");

            return new CustomOAuth2UserDTO(oAuth2DTO);

        }
    }
}
