package com.example.gamemate.global.config;

import com.example.gamemate.domain.auth.jwt.JWTUtil;
import com.example.gamemate.domain.auth.jwt.filter.JWTFilter;
import com.example.gamemate.domain.auth.jwt.filter.LoginFilter;
import com.example.gamemate.domain.auth.service.CustomOAuth2UserService;
import com.example.gamemate.domain.user.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Collections;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    //AuthenticationManager가 인자로 받을 AuthenticationConfiguration 객체 생성자 주입
    private final AuthenticationConfiguration authenticationConfiguration;
    //JWTUtil 주입
    private final JWTUtil jwtUtil;
    private final CustomUserDetailsService customUserDetailsService;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(
        AuthenticationConfiguration authenticationConfiguration,
        JWTUtil jwtUtil,
        CustomUserDetailsService customUserDetailsService,
        CustomOAuth2UserService customOAuth2UserService
    ) {

        this.authenticationConfiguration = authenticationConfiguration;
        this.jwtUtil = jwtUtil;
        this.customUserDetailsService = customUserDetailsService;
        this.customOAuth2UserService = customOAuth2UserService;

    }

    //webClient Bean 등록
    @Bean
    public WebClient webClient() {
        return WebClient.builder().build();
    }


    //AuthenticationManager Bean 등록
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();

    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {

        return new BCryptPasswordEncoder();

    }

    @Bean
    public WebMvcConfigurer webConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("forward:/index.html");
            }
        };
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http.csrf(AbstractHttpConfigurer::disable)

            //Form 로그인 방식 disable
            .formLogin(AbstractHttpConfigurer::disable)

            //http basic 인증 방식 disable
            .httpBasic(AbstractHttpConfigurer::disable)

            //경로별 인가 작업
            .authorizeHttpRequests((auth) -> auth
                .requestMatchers("/login", "/", "/join","/friend/**").permitAll()
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui.html", "/webjars/**").permitAll() // Swagger 경로 허용
                .anyRequest().authenticated())

            //JWTFilter 등록
            .addFilterBefore(new JWTFilter(jwtUtil), LoginFilter.class)

            //필터 추가
            //AuthenticationManager() 메소드에 authenticationConfiguration 객체를 넣어야 함
            //AuthenticationManager()와 JWTUtil, customUserDetailsService, bCryptPasswordEncoder() 인수 전달
            .addFilterAt(new LoginFilter(authenticationManager(authenticationConfiguration), jwtUtil, customUserDetailsService, bCryptPasswordEncoder()), UsernamePasswordAuthenticationFilter.class)

            //세션 설정
            //JWT를 통한 인증/인가를 위해 세션을 STATELESS 상태로 설정
            .sessionManagement((session) -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            //oauth2
            .oauth2Login((oauth2) -> oauth2
                .userInfoEndpoint((userInfoEndpointConfig) -> userInfoEndpointConfig
                    .userService(customOAuth2UserService)))

            //cors 설정
            .cors((cors -> cors.configurationSource(new CorsConfigurationSource() {
                @Override
                public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {

                    CorsConfiguration configuration = new CorsConfiguration();

                    configuration.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    configuration.setAllowedMethods(Collections.singletonList("*"));
                    configuration.setAllowCredentials(true);
                    configuration.setAllowedHeaders(Collections.singletonList("*"));
                    configuration.setMaxAge(3600L);

                    configuration.setExposedHeaders(Collections.singletonList("Authorization"));

                    return configuration;
                }
            })));

        return http.build();

    }
}

