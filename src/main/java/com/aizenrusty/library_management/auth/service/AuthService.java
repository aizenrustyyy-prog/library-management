package com.aizenrusty.library_management.auth.service;

import com.aizenrusty.library_management.auth.dto.LoginRequestDto;
import com.aizenrusty.library_management.auth.dto.LoginResponseDto;
import com.aizenrusty.library_management.book.entity.Users;
import com.aizenrusty.library_management.book.repository.UserRepository;
import com.aizenrusty.library_management.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       JwtService jwtService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtService = jwtService;
    }

    public LoginResponseDto login(LoginRequestDto dto) {
        String email = dto.getEmail().trim().toLowerCase();

        // throws BadCredentialsException if email or password is wrong
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, dto.getPassword()));

        Users user = userRepository.findByEmail(email)
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        String token = jwtService.generateToken(user.getEmail(), user.getRole().name());
        return new LoginResponseDto(token);
    }
}
