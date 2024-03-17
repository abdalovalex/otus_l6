package com.example.auth.controllers;

import com.example.auth.dto.JwtResponseDTO;
import com.example.auth.dto.SignInRequestDTO;
import com.example.auth.dto.SignUpRequestDTO;
import com.example.auth.dto.SignUpResponseDTO;
import com.example.auth.exception.NotUniqueException;
import com.example.auth.services.SignService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final SignService signService;

    // Регистрация
    @PostMapping("/sign-up")
    public ResponseEntity<SignUpResponseDTO> signUp(@RequestBody SignUpRequestDTO signUpRequestDTO) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(signService.signUp(signUpRequestDTO));
        } catch (NotUniqueException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    // Логин
    @PostMapping("/sign-in")
    public ResponseEntity<JwtResponseDTO> signIn(@RequestBody SignInRequestDTO signInRequestDTO) {
        return ResponseEntity.ok().body(signService.signIn(signInRequestDTO));
    }

    // Проверка токена
    @GetMapping("/auth")
    public ResponseEntity<Void> auth(HttpServletResponse response) {
        signService.auth(response);
        return ResponseEntity.ok().build();
    }

    // Логин для ingress
    @GetMapping("/login")
    public ResponseEntity<String> login() {
        return ResponseEntity.ok().body("Необходимо войти в систему");
    }

    // Выход
    @GetMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            SecurityContextHolder.clearContext();
            request.getSession().invalidate();
        }

        return ResponseEntity.ok().build();
    }
}
