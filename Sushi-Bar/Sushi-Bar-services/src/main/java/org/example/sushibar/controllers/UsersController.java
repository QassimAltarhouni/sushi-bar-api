package org.example.sushibar.controllers;

import com.example.api.UsersApi;
import com.example.models.User;
import org.example.sushibar.entities.UserEntity;
import org.example.sushibar.mappers.UserMapper;
import org.example.sushibar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class UsersController implements UsersApi {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @Override
    public ResponseEntity<User> createUser(User user) {
        UserEntity entity = UserMapper.toEntity(user);
        UserEntity saved = userService.create(entity);
        return ResponseEntity.status(201).body(UserMapper.toDto(saved));
    }

    @Override
    public ResponseEntity<Void> deleteAllUsers() {
        userService.getAll().forEach(user -> userService.delete(user.getId()));
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll().stream()
                .map(UserMapper::toDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<User> updateUser(Integer userId, User user) {
        UserEntity updated = UserMapper.toEntity(user);
        UserEntity result = userService.update(userId.longValue(), updated);
        return ResponseEntity.ok(UserMapper.toDto(result));
    }
}
