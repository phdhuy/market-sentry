package com.phdhuy.stock_alert.infrastructure.security.config;

import static org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher;

import com.phdhuy.stock_alert.infrastructure.security.exception.CustomAuthenticationEntryPoint;
import com.phdhuy.stock_alert.infrastructure.security.filter.TokenAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final TokenAuthenticationFilter tokenAuthenticationFilter;

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  @Bean
  protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    http.csrf(AbstractHttpConfigurer::disable)
        .exceptionHandling(
            exp -> exp.authenticationEntryPoint(new CustomAuthenticationEntryPoint()))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(
                        antMatcher("/app/**"),
                        antMatcher("/favicon.ico"),
                        antMatcher("/**/*.png"),
                        antMatcher("/**/*.jpg"),
                        antMatcher("/**/*.css"),
                        antMatcher("/**/*.js"),
                        antMatcher("/swagger-ui/**"),
                        antMatcher("/v3/api-docs/**"))
                    .permitAll()
                    .anyRequest()
                    .permitAll());

    http.addFilterBefore(tokenAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }
}
