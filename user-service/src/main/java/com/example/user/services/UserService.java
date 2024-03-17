package com.example.user.services;

import java.util.Optional;

import com.example.user.dto.UserDto;
import com.example.user.entity.User;
import com.example.user.exception.NotUniqueException;
import com.example.user.mappers.UserMapper;
import com.example.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional
    public void createUser(UserDto userDto) throws NotUniqueException {
        if (userRepository.findByUsername(userDto.getUsername()).isPresent()) {
            throw new NotUniqueException("Такой пользователь уже существует");
        }

        userRepository.save(userMapper.map(userDto));
    }

    @Transactional
    public UserDto getUser(Integer userId) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("Пользователь не найден");
        }
        return userMapper.map(user.get());
    }

    @Transactional
    public void updateUser(Integer userId, UserDto userDto) {
        User user = userRepository.getReferenceById(userId);

        user.setPhone(userDto.getPhone());
        user.setBirthday(userDto.getBirthday());

        userRepository.save(user);
    }

    public User getByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));

    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }
}
