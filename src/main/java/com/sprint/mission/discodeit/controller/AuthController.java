package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.controller.openapi.AuthApiDocs;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.security.auth.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApiDocs {

    private final AuthService authService;
    private final UserMapper mapper;

    @GetMapping("/me")
    public ResponseEntity<UserResponseDto> me(Authentication authentication) {
        if (authentication == null
            || !(authentication.getPrincipal() instanceof DiscodeitUserDetails discodeitUser)) {
            return ResponseEntity.status(401).build();
        }

        User user = discodeitUser.getUser();
        return ResponseEntity.ok(mapper.toDto(user));
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }
        SecurityContextHolder.clearContext();
        return ResponseEntity.ok().build();
    }
}
