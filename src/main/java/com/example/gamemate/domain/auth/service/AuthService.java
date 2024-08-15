package com.example.gamemate.domain.auth.service;

import com.example.gamemate.domain.user.entity.Genre;
import com.example.gamemate.domain.user.entity.PlayTime;
import com.example.gamemate.domain.user.entity.User;
import com.example.gamemate.domain.auth.dto.JoinDTO;
import com.example.gamemate.domain.user.repository.GenreRepository;
import com.example.gamemate.domain.user.repository.PlayTimeRepository;
import com.example.gamemate.domain.user.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final GenreRepository genreRepository;
    private final PlayTimeRepository playTimeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public AuthService(
        UserRepository userRepository,
        BCryptPasswordEncoder bCryptPasswordEncoder,
        GenreRepository genreRepository,
        PlayTimeRepository playTimeRepository
    ) {

        this.userRepository = userRepository;
        this.genreRepository = genreRepository;
        this.playTimeRepository = playTimeRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;

    }

    public void joinProcess(JoinDTO joinDTO) {

        if(isEmailDuplicated(joinDTO.getUsername())) {
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }

        List<Integer> preferredGenres = joinDTO.getPreferredGenres();
        List<Integer> playTimes = joinDTO.getPlayTimes();

        User data = new User();

        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));
        data.setNickname(joinDTO.getNickname());

        List<Genre> genres = IntStream.range(0, preferredGenres.size())
                .filter(i -> preferredGenres.get(i) == 1)
                .mapToObj(i -> genreRepository.findById((long) (i + 1)).orElseThrow(() -> new IllegalArgumentException("Invalid genre ID: " + (i + 1))))
                .collect(Collectors.toList());
        data.setPreferredGenres(genres);

        List<PlayTime> times = IntStream.range(0, playTimes.size())
                .filter(i -> playTimes.get(i) == 1)
                .mapToObj(i -> playTimeRepository.findById((long) (i + 1)).orElseThrow(() -> new IllegalArgumentException("Invalid play time ID: " + (i + 1))))
                .collect(Collectors.toList());
        data.setPlayTimes(times);

        userRepository.save(data);

    }

    public boolean isEmailDuplicated(String username) {
        return userRepository.existsByUsername(username);
    }

    public boolean isNicknameDuplicated(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

//    public String getNaverAuthorizeUrl(String type)
//        throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
//
//        String baseUrl = naverClientRegistration.getProviderDetails().getAuthorizationUri();
//        String clientId = naverClientRegistration.getClientId();
//
//        String redirectUrl = UriComponentsBuilder
//            .fromUriString("http://localhost:8080/login/oauth2/code/naver") // replace with your redirect URI
//            .build()
//            .toUriString();
//
//        UriComponents uriComponents = UriComponentsBuilder
//            .fromUriString(baseUrl)
//            .pathSegment(type)
//            .queryParam("response_type", "code")
//            .queryParam("client_id", clientId)
//            .queryParam("redirect_uri", redirectUrl)
//            .queryParam("state", "1234")
//            .build();
//
//        return uriComponents.toString();
//
//    }
//
//    public String getNaverTokenUrl(String type, NaverCallbackDTO callback)
//        throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
//
//        // 환경 변수에서 값 가져오기
//        String baseUrl = naverClientRegistration.getProviderDetails().getAuthorizationUri();
//        String clientId = naverClientRegistration.getClientId();
//        String clientSecret = naverClientRegistration.getClientSecret();
//
//        // UriComponentsBuilder를 사용하여 URL 생성
//        UriComponents uriComponents = UriComponentsBuilder
//            .fromUriString(baseUrl)
//            .queryParam("grant_type", "authorization_code")
//            .queryParam("client_id", clientId)
//            .queryParam("client_secret", clientSecret)
//            .queryParam("code", callback.getCode())
//            .queryParam("state", URLEncoder.encode(callback.getState(), "UTF-8"))
//            .build();
//
//        try {
//            // URL 생성 및 HTTP 요청 설정
//            URL url = new URL(uriComponents.toUriString());
//            HttpURLConnection con = (HttpURLConnection) url.openConnection();
//            con.setRequestMethod("POST"); // OAuth2 token 요청은 POST 방식이 일반적입니다.
//            con.setDoOutput(true); // 요청 본문을 전송할 수 있도록 설정
//
//            // 응답 처리
//            int responseCode = con.getResponseCode();
//            BufferedReader br;
//
//            if (responseCode == HttpURLConnection.HTTP_OK) { // 정상 호출
//                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            } else {  // 에러 발생
//                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
//            }
//
//            StringBuilder response = new StringBuilder();
//            String inputLine;
//            while ((inputLine = br.readLine()) != null) {
//                response.append(inputLine);
//            }
//
//            br.close();
//            return response.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    public String getNaverUserByToken(NaverTokenDTO token) {
//
//        try {
//            String accessToken = token.getAccess_token();
//            String tokenType = token.getToken_type();
//
//            URL url = new URL("https://openapi.naver.com/v1/nid/me");
//            HttpURLConnection con = (HttpURLConnection)url.openConnection();
//            con.setRequestMethod("GET");
//            con.setRequestProperty("Authorization", tokenType + " " + accessToken);
//
//            int responseCode = con.getResponseCode();
//            BufferedReader br;
//
//            if(responseCode==200) { // 정상 호출
//                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
//            } else {  // 에러 발생
//                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
//            }
//
//            String inputLine;
//            StringBuffer response = new StringBuffer();
//            while ((inputLine = br.readLine()) != null) {
//                response.append(inputLine);
//            }
//
//            br.close();
//            return response.toString();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

}
