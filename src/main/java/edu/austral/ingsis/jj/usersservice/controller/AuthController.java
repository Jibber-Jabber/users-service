package edu.austral.ingsis.jj.usersservice.controller;

import edu.austral.ingsis.jj.usersservice.dto.LoginResponseDto;
import edu.austral.ingsis.jj.usersservice.dto.LoginDto;
import edu.austral.ingsis.jj.usersservice.dto.RegisterRequestDto;
import edu.austral.ingsis.jj.usersservice.service.AuthenticationService;
import edu.austral.ingsis.jj.usersservice.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@RestController
@RequestMapping("/auth")
public class AuthController {

    final AuthenticationService authenticationService;

    final UserService userService;

    public AuthController(AuthenticationService authenticationService, UserService userService) {
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
