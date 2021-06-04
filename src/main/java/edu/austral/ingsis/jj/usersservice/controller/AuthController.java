package edu.austral.ingsis.jj.usersservice.controller;

import edu.austral.ingsis.jj.usersservice.config.JwtUtils;
import edu.austral.ingsis.jj.usersservice.dto.LoginResponseDto;
import edu.austral.ingsis.jj.usersservice.dto.LoginDto;
import edu.austral.ingsis.jj.usersservice.dto.RegisterRequestDto;
import edu.austral.ingsis.jj.usersservice.repository.RoleRepository;
import edu.austral.ingsis.jj.usersservice.repository.UserRepository;
import edu.austral.ingsis.jj.usersservice.service.AuthenticationService;
import edu.austral.ingsis.jj.usersservice.service.UserService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final UserRepository userRepository;

    final RoleRepository roleRepository;

    final PasswordEncoder encoder;

    final JwtUtils jwtUtils;

    final AuthenticationService authenticationService;

    final UserService userService;

    public AuthController(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder encoder, JwtUtils jwtUtils, AuthenticationService authenticationService, UserService userService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public LoginResponseDto authenticateUser(@Valid @RequestBody LoginDto loginRequest, HttpServletResponse response) {
        return authenticationService.authenticate(loginRequest, response);
    }

    @PostMapping("/register")
    public void registerUser(@Valid @RequestBody RegisterRequestDto registerRequest) {
        userService.registerUser(registerRequest);
    }

    @PostMapping("/logout")
    public void logout(HttpServletResponse response) {
        authenticationService.logout(response);
    }



}
