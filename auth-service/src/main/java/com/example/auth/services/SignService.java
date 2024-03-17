package com.example.auth.services;

import com.example.auth.dto.JwtResponseDTO;
import com.example.auth.dto.SignInRequestDTO;
import com.example.auth.dto.SignUpRequestDTO;
import com.example.auth.dto.SignUpResponseDTO;
import com.example.auth.entity.User;
import com.example.auth.exception.NotUniqueException;
import com.example.auth.repository.UserRepository;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignService {
    private static final String X_USER_ID = "X-USER-ID";
    private static final String X_USERNAME = "X-USERNAME";

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final UserService userService;

    // Регистрация
    @Transactional
    public SignUpResponseDTO signUp(SignUpRequestDTO signUpRequestDTO) throws NotUniqueException {
        if (userRepository.existsByUsername(signUpRequestDTO.getUsername())) {
            throw new NotUniqueException("Пользователь с таким именем уже существует");
        }

        if (userRepository.existsByEmail(signUpRequestDTO.getEmail())) {
            throw new NotUniqueException("Пользователь с таким email уже существует");
        }

        User user = User.builder()
                .username(signUpRequestDTO.getUsername())
                .email(signUpRequestDTO.getEmail())
                .firstName(signUpRequestDTO.getFirstName())
                .lastName(signUpRequestDTO.getLastName())
                .password(passwordEncoder.encode(signUpRequestDTO.getPassword()))
                .build();

        userRepository.save(user);
        return new SignUpResponseDTO(user.getId());
    }

    // Вход
    public JwtResponseDTO signIn(SignInRequestDTO signInRequestDTO) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                signInRequestDTO.getUsername(),
                signInRequestDTO.getPassword()
        ));

        UserDetails userDetails = userService.userDetailsService()
                .loadUserByUsername(signInRequestDTO.getUsername());

        String jwt = jwtService.generateToken(userDetails);

        return new JwtResponseDTO(jwt);
    }

    // Авторизация по токену
    public void auth(HttpServletResponse response) {
        User user = userService.getCurrentUser();

        response.addHeader(X_USER_ID, String.valueOf(user.getId()));
        response.addHeader(X_USERNAME, String.valueOf(user.getUsername()));
    }
}
