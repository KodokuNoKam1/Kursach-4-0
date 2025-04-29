package by.bsuir.kursach.commercialoffer.service;

import by.bsuir.kursach.commercialoffer.dto.JwtResponse;
import by.bsuir.kursach.commercialoffer.dto.LoginRequest;
import by.bsuir.kursach.commercialoffer.dto.RegisterRequest;
import by.bsuir.kursach.commercialoffer.model.User;
import by.bsuir.kursach.commercialoffer.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public JwtResponse register(RegisterRequest request) {
        Optional<User> existingUser = userRepository.findByUsername(request.getUsername());

        if (existingUser.isPresent()) { // Теперь работаем с Optional
            throw new RuntimeException("Username already exists");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(request.getRole() != null ? request.getRole() : "USER");

        userRepository.save(user);

        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new JwtResponse(token);
    }

    public JwtResponse login(LoginRequest request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        String token = jwtService.generateToken(user.getUsername(), user.getRole());
        return new JwtResponse(token);
    }
}
