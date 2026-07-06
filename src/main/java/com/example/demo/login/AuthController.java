package com.example.demo.login;

import com.example.demo.user.User;
import com.example.demo.user.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder; // 👈 Correct modern import
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
 @CrossOrigin(origins = {"http://localhost:4200", "https://bliss-front-2x0f.onrender.com"}, allowCredentials = "true")   
//@CrossOrigin(origins = "http://localhost:4200")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder; // 👈 Inject the encoder bean from SecurityConfig

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        // 1. Look up the user by username in the database
        Optional<User> userOptional = userRepository.findByUsername(loginRequest.getUsername());

        if (userOptional.isPresent()) {
            User user = userOptional.get();

            // 2. Safely compare incoming raw password with the database BCrypt hash string
            if (passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {

                // Return success token string matching your Angular service expectations
                return ResponseEntity.ok(Map.of("token", "dummy-jwt-token-string-bliss"));
            }
        }

        // Return 401 Unauthorized if verification fails
        return ResponseEntity.status(401).body(Map.of("message", "Invalid username or password!"));
    }
}
