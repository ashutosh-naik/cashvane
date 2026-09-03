package com.cashvane.backend.auth;

import com.cashvane.backend.organization.Organization;
import com.cashvane.backend.organization.OrganizationRepository;
import com.cashvane.backend.user.Role;
import com.cashvane.backend.user.SignupRequest;
import com.cashvane.backend.user.User;
import com.cashvane.backend.user.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import com.cashvane.backend.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final JwtService jwtService;

    public AuthController(OrganizationRepository organizationRepository, UserRepository userRepository, JwtService jwtService) {
        this.organizationRepository = organizationRepository;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    @PostMapping("/signup")
    public User signup(@RequestBody SignupRequest request) {
        Organization organization = new Organization();
        organization.setName(request.getOrganizationName());
        organizationRepository.save(organization);

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.ADMIN);
        user.setOrganization(organization);

        return userRepository.save(user);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        User user = userRepository.findByEmail(request.email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(request.password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid email or password");
        }

        return jwtService.generateToken(user.getId().toString(), user.getOrganization().getId().toString());
    }

    public static class LoginRequest {
        public String email;
        public String password;
    }
}