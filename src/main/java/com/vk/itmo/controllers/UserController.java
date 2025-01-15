package com.vk.itmo.controllers;

import com.vk.itmo.dto.*;
import com.vk.itmo.exceptions.IllegalAlreadyExistBoardCredentialsException;
import com.vk.itmo.exceptions.AlreadyExistBoardAlreadyExistsException;
import com.vk.itmo.exceptions.PermissionException;
import com.vk.itmo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/users/register")
    private ResponseEntity<Object> registerUser(@RequestBody UserRegistrationDto registrationDto) {
        try {
            userService.registerUser(registrationDto);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (AlreadyExistBoardAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @PostMapping("/users/login")
    private ResponseEntity<TokenDto> login(@RequestBody UserLoginDto loginDto) {
        try {
            TokenDto dto = userService.login(loginDto);
            return ResponseEntity.ok(dto);
        } catch (IllegalAlreadyExistBoardCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }

    @GetMapping("/users/{userId}/projects")
    private ResponseEntity<List<UserProjectDto>> getProjects(@AuthenticationPrincipal Long currentUserId, @PathVariable Long userId) {
        try {
            List<UserProjectDto> dtoList = userService.getProjects(currentUserId, userId);
            return ResponseEntity.ok(dtoList);
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/users/{userId}/tickets")
    private ResponseEntity<List<UserTicketDto>> getTickets(@AuthenticationPrincipal Long currentUserId, @PathVariable Long userId) {
        try {
            List<UserTicketDto> dtoList = userService.getTickets(currentUserId, userId);
            return ResponseEntity.ok(dtoList);
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @GetMapping("/users/{userId}/bugreports")
    private ResponseEntity<List<UserBugreportDto>> getBugreports(@AuthenticationPrincipal Long currentUserId, @PathVariable Long userId) {
        try {
            List<UserBugreportDto> dtoList = userService.getBugreports(currentUserId, userId);
            return ResponseEntity.ok(dtoList);
        } catch (PermissionException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

}
