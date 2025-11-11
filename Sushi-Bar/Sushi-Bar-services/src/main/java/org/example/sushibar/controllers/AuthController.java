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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

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
        return authenticate(loginUserRequest.getEmail(), loginUserRequest.getPassword())
                .map(user -> {
                    LoginUser200Response response = new LoginUser200Response();
                    response.setToken("Sushi_Bar_Token");
                    response.setUser(sanitizeUser(user));
                    return ResponseEntity.ok(response);
                })
                .orElse(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
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
        String email = trimToNull(request.getEmail());
        String password = request.getPassword();
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        if (userService.findByEmailIgnoreCase(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        String phone = trimToNull(request.getPhone());
        if (phone != null && userService.getByPhone(phone).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        UserEntity newUser = buildUserEntity(request);
        newUser.setEmail(email);
        newUser.setPhone(phone);

        UserEntity created = userService.create(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body(sanitizeUser(created));
    }

    @LogMethod
    @Override

    public ResponseEntity<User> getCurrentAuthenticatedUser(@RequestParam("userId") Integer userId) {
        return userService.getById(userId.longValue())
                .map(this::sanitizeUser)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping(value = "/auth/sign-in", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> signIn(@RequestBody LoginUserRequest loginUserRequest) {
        return authenticate(loginUserRequest.getEmail(), loginUserRequest.getPassword())
                .map(user -> ResponseEntity.ok(buildAuthPayload(user, "Login successful")))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(buildErrorResponse("Invalid email or password")));
    }

    @PostMapping(value = "/auth/sign-up", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> signUp(@RequestBody RegisterUserRequest request) {
        String email = trimToNull(request.getEmail());
        String password = request.getPassword();
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(buildErrorResponse("Email and password are required"));
        }

        if (userService.findByEmailIgnoreCase(email).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(buildErrorResponse("An account with that email already exists"));
        }

        String phone = trimToNull(request.getPhone());
        if (phone != null && userService.getByPhone(phone).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(buildErrorResponse("An account with that phone number already exists"));
        }

        UserEntity newUser = buildUserEntity(request);
        newUser.setEmail(email);
        newUser.setPhone(phone);

        UserEntity created = userService.create(newUser);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(buildAuthPayload(created, "Account created successfully"));
    }

    @PostMapping(value = "/auth/sign-out", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> signOut() {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("status", "success");
        payload.put("message", "Token invalidated on client-side. Please delete it.");
        return ResponseEntity.ok(payload);
    }

    @GetMapping(value = "/auth/session", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getSession(@RequestParam("userId") Integer userId) {
        return userService.getById(userId.longValue())
                .map(user -> ResponseEntity.ok(buildAuthPayload(user, "Authenticated")))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(buildErrorResponse("User not found")));
    }

    private Optional<UserEntity> authenticate(String email, String password) {
        if (!StringUtils.hasText(email) || !StringUtils.hasText(password)) {
            return Optional.empty();
        }

        return userService.findByEmailIgnoreCase(email.trim())
                .filter(user -> password.equals(user.getPassword()));
    }

    private Map<String, Object> buildAuthPayload(UserEntity user, String message) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("status", "success");
        payload.put("message", message);
        payload.put("token", "Sushi_Bar_Token");
        payload.put("userId", user.getId());
        payload.put("user", sanitizeUser(user));
        return payload;
    }

    private Map<String, Object> buildErrorResponse(String message) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("status", "error");
        payload.put("message", message);
        return payload;
    }

    private User sanitizeUser(UserEntity entity) {
        User dto = UserMapper.toDto(entity);
        dto.setPassword(null);
        return dto;
    }

    private UserEntity buildUserEntity(RegisterUserRequest request) {
        UserEntity newUser = new UserEntity();
        newUser.setUsername(trimToNull(request.getUsername()));
        newUser.setPassword(request.getPassword());
        newUser.setRole("customer");
        return newUser;
    }

    private String trimToNull(String value) {
        return StringUtils.hasText(value) ? value.trim() : null;
    }

}
