package com.example.user.controllers;

import com.example.user.dto.UserDto;
import com.example.user.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService service;

    @GetMapping("/{userId}")
    @PreAuthorize("authentication.principal.id == #userId")
    public ResponseEntity<UserDto> get(@PathVariable Integer userId) {
        try {
            return ResponseEntity.ok(service.getUser(userId));
        } catch (UsernameNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage(), e);
        }
    }

    @PutMapping("/{userId}")
    @PreAuthorize("authentication.principal.id == #userId")
    public ResponseEntity<Void> update(@PathVariable Integer userId, @RequestBody UserDto userDto) {
        service.updateUser(userId, userDto);
        return ResponseEntity.ok().build();
    }
}
