package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.auth.DiscodeitUsernamePasswordAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableMethodSecurity
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfig {

  private final UserMapper userMapper;


  @Bean
  public SecurityFilterChain chain(HttpSecurity http, DaoAuthenticationProvider provider,
      AuthenticationManager manager, PersistentTokenRepository tokenRepository,
      SessionRegistry sessionRegistry,
      RememberMeServices rememberMeServices) throws Exception {

    CsrfTokenRequestAttributeHandler plain =
        new CsrfTokenRequestAttributeHandler();

    DiscodeitUsernamePasswordAuthenticationFilter filter = new DiscodeitUsernamePasswordAuthenticationFilter(
        userMapper, sessionRegistry(), rememberMeServices);

    filter.setAuthenticationManager(manager);
    filter.setRequiresAuthenticationRequestMatcher(
        new AntPathRequestMatcher("/api/auth/login", "POST")
    );

    http
        .rememberMe(rm -> rm.rememberMeServices(rememberMeServices))
        .sessionManagement(
            session -> session.maximumSessions(1).maxSessionsPreventsLogin(false)
                .sessionRegistry(sessionRegistry()))
        .csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository()).csrfTokenRequestHandler(plain)
            .ignoringRequestMatchers("/api/users", "/api/auth/check-session"))
        .addFilterAt(filter, DiscodeitUsernamePasswordAuthenticationFilter.class)
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .logout(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> auth
            .requestMatchers(
                "/assets/**",
                "/favicon.ico",
                "/index.html",
                "/api/auth/login",
                "/api/auth/csrf-token",
                "/api/users",
                "/",
                "/api/auth/check-session"
            ).permitAll()
            .requestMatchers(HttpMethod.GET, "/api/channels").hasRole("USER")
            .requestMatchers("/api/auth/role").hasRole("ADMIN")
            .requestMatchers("/api/channels/**").hasRole("CHANNEL_MANAGER")
            .anyRequest().hasRole("USER")
        );

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)
      throws Exception {
    return configuration.getAuthenticationManager();
  }

  @Bean
  public CsrfTokenRepository csrfTokenRepository() {
    CookieCsrfTokenRepository repository = CookieCsrfTokenRepository.withHttpOnlyFalse();
    repository.setCookieName("CSRF-TOKEN");
    repository.setHeaderName("X-Csrf-Token");
    repository.setCookiePath("/");
    return repository;
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public MethodSecurityExpressionHandler methodSecurityExpressionHandler(
      RoleHierarchy roleHierarchy) {
    DefaultMethodSecurityExpressionHandler handler = new DefaultMethodSecurityExpressionHandler();
    handler.setRoleHierarchy(roleHierarchy);
    return handler;
  }

  @Bean
  public SessionRegistry sessionRegistry() {
    return new SessionRegistryImpl();
  }


}

