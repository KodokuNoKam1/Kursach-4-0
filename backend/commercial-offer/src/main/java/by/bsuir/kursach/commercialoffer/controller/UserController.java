package by.bsuir.kursach.commercialoffer.controller;

import by.bsuir.kursach.commercialoffer.model.User;
import by.bsuir.kursach.commercialoffer.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@Tag(name = "Users", description = "API for managing users and roles")
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Returns a list of all users (ADMIN only)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @PutMapping("/{id}/role")
    @Operation(summary = "Update user role", description = "Updates the role of a specific user (ADMIN only)")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<User> updateUserRole(@PathVariable Long id, @RequestBody String role) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setRole(role);
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}