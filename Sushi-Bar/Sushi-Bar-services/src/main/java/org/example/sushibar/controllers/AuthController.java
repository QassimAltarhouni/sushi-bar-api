package org.example.sushibar.controllers;

import com.example.api.AuthApi;
import com.example.models.LoginUser200Response;
import com.example.models.LoginUserRequest;
import com.example.models.LogoutUser200Response;
import com.example.models.RegisterUserRequest;
import com.example.models.User;
import org.example.sushibar.aop.annotations.LogMethod;
import org.example.sushibar.entities.UserEntity;
import org.example.sushibar.mappers.UserMapper;
import org.example.sushibar.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController implements AuthApi {

    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }
    @LogMethod
    @Override

    public ResponseEntity<LoginUser200Response> loginUser(LoginUserRequest loginUserRequest) {
        return userService.findByEmailAndPassword(loginUserRequest.getEmail(), loginUserRequest.getPassword())
                .map(user -> {
                    LoginUser200Response response = new LoginUser200Response();
                    response.setToken("Sushi_Bar_Token"); // Replace later with a real JWT
                    response.setUser(UserMapper.toDto(user)); // ✅ This line is essential
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(401).build());
    }

    @LogMethod
    @Override
    public ResponseEntity<LogoutUser200Response> logoutUser() {
        LogoutUser200Response response = new LogoutUser200Response();
        response.setMessage("Token invalidated on client-side. Please delete it.");
        return ResponseEntity.ok(response);
    }
    @LogMethod
    @Override
    public ResponseEntity<User> registerUser(RegisterUserRequest request) {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(request.getUsername());
        newUser.setEmail(request.getEmail());
        newUser.setPhone(request.getPhone());
        newUser.setPassword(request.getPassword());
        newUser.setRole("customer");

        UserEntity created = userService.create(newUser);
        return ResponseEntity.status(201).body(UserMapper.toDto(created));
    }

    @LogMethod
    @Override

    public ResponseEntity<User> getCurrentAuthenticatedUser(@RequestParam("userId") Integer userId) {
        return userService.getById(userId.longValue())
                .map(UserMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).build());
    }

}
