package com.suds.carbonmeasure.api;

import com.suds.carbonmeasure.api.model.LoginResponse;
import com.suds.carbonmeasure.domain.CarbonUser;
import com.suds.carbonmeasure.domain.UserSession;
import com.suds.carbonmeasure.domain.repo.CarbonUserRepository;
import com.suds.carbonmeasure.domain.repo.UserSessionRepository;
import com.suds.carbonmeasure.service.JWTService;
import com.suds.carbonmeasure.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CarbonUserRepository userRepository;

    @Autowired
    private UserSessionRepository sessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTService jwtService;

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody CarbonUser user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userService.registerUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
    }

    @PostMapping("/login")
    public @ResponseBody ResponseEntity<LoginResponse> login(@RequestBody LoginUser user) {
        try {
            CarbonUser cb = userRepository.findByUsername(user.getUsername());
            String sessionToken = jwtService.generateToken(cb.getUsername(), cb.getId());
            jwtService.extractAllClaims(sessionToken);
            UserSession session = new UserSession();
            session.setUserId(cb.getId());
            session.setSessionToken(sessionToken);
            session.setCreatedAt(LocalDateTime.now());
            sessionRepository.save(session);
            final LoginResponse response = new LoginResponse();
            response.setToken(sessionToken);
            response.setUsername(user.getUsername());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestParam String sessionToken) {
        UserSession session = sessionRepository.findBySessionToken(sessionToken);
        if (session != null) {
            sessionRepository.deleteByUserId(session.getUserId());
            return ResponseEntity.ok("Logged out successfully");
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid session token");
    }
}

