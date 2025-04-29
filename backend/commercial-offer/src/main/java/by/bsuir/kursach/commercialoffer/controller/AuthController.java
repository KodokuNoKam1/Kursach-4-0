package by.bsuir.kursach.commercialoffer.controller;

import by.bsuir.kursach.commercialoffer.model.User;
import by.bsuir.kursach.commercialoffer.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        User registeredUser = userService.registerUser(
                user.getUsername(),
                user.getPassword(),
                user.getEmail(),
                user.getRole()
        );
        return ResponseEntity.ok(registeredUser);
    }
}