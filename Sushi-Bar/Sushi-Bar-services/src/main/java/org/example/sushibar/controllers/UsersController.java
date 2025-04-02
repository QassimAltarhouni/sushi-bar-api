package org.example.sushibar.controllers;

import com.example.api.UsersApi;
import com.example.models.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.NativeWebRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class UsersController implements UsersApi {

    @Override
    public Optional<NativeWebRequest> getRequest() {
        return UsersApi.super.getRequest();
    }

    @Override
    public ResponseEntity<User> createUser(User user) {
        // Simulate creating user
        return ResponseEntity.status(201).body(user);
    }

    @Override
    public ResponseEntity<Void> deleteAllUsers() {
        return ResponseEntity.noContent().build();
    }

    @Override
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User().id(1).username("john").email("john@example.com").role("user"));
        users.add(new User().id(2).username("Qassim").email("Qassim@example.com").role("admin"));
        return ResponseEntity.ok(users);
    }

    @Override
    public ResponseEntity<User> updateUser(Integer userId, User user) {
        user.setId(userId);
        return ResponseEntity.ok(user);
    }
}
