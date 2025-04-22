package org.example.sushibar.controllers;

import com.example.api.UsersApi;
import com.example.models.GetAllUsers200Response;
import com.example.models.User;
import org.example.sushibar.entities.UserEntity;
import org.example.sushibar.mappers.UserMapper;
import org.example.sushibar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;


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
    public ResponseEntity<GetAllUsers200Response> getAllUsers(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<UserEntity> pageResult = userService.getAllPaged(pageable);

        GetAllUsers200Response response = new GetAllUsers200Response();
        response.setContent(
                pageResult.getContent().stream()
                        .map(UserMapper::toDto)
                        .collect(Collectors.toList())
        );
        response.setNumber(pageResult.getNumber());
        response.setSize(pageResult.getSize());
        response.setTotalPages(pageResult.getTotalPages());
        response.setTotalElements((int) pageResult.getTotalElements());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<User> updateUser(Integer userId, User user) {
        UserEntity updated = UserMapper.toEntity(user);
        UserEntity result = userService.update(userId.longValue(), updated);
        return ResponseEntity.ok(UserMapper.toDto(result));
    }
    @Override
    public ResponseEntity<User> getUserById(Integer userId) {
        return userService.getById(userId.longValue())
                .map(UserMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<User> getCurrentUser() {
        Long demoUserId = 1L; // Replace with extracted userId from token in the future
        return userService.getById(demoUserId)
                .map(UserMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Override
    public ResponseEntity<Void> deleteUserById(Integer userId) {
        boolean deleted = userService.delete(userId.longValue());
        return deleted ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

}
