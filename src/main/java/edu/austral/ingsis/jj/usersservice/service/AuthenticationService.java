package edu.austral.ingsis.jj.usersservice.service;

import edu.austral.ingsis.jj.usersservice.config.JwtUtils;
import edu.austral.ingsis.jj.usersservice.config.UserDetailsImpl;
import edu.austral.ingsis.jj.usersservice.dto.LoginResponseDto;
import edu.austral.ingsis.jj.usersservice.dto.LoginDto;

import edu.austral.ingsis.jj.usersservice.exceptions.NotFoundException;
import edu.austral.ingsis.jj.usersservice.model.user.UserRole;
import edu.austral.ingsis.jj.usersservice.model.user.UserRoleType;
import edu.austral.ingsis.jj.usersservice.repository.RoleRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;


@Service
public class AuthenticationService {

    private final AuthenticationManager authenticationManager;

    private final JwtUtils jwtUtils;

    private final RoleRepository roleRepository;

    public AuthenticationService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, RoleRepository roleRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.roleRepository = roleRepository;
    }

    public LoginResponseDto authenticate(LoginDto loginDto, HttpServletResponse response) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);
        response.setHeader("Set-Cookie", "jwt=" + jwt + "; HttpOnly; SameSite=strict; Path=/api");

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        String role = userDetails.getAuthorities().stream().findFirst().map((GrantedAuthority::getAuthority)).orElseThrow(() -> new NotFoundException("Role Not found"));

        return LoginResponseDto.builder()
                .userId(userDetails.getId())
                .username(userDetails.getUsername())
                .email(userDetails.getEmail())
                .role(role)
                .firstName(userDetails.getFirstName())
                .lastName(userDetails.getLastName())
                .build();
    }

    @PostConstruct
    public void addRoles(){
        if (!roleRepository.existsByUserRoleType(UserRoleType.ROLE_USER)) {
            UserRole userRole = new UserRole();
            userRole.setUserRoleType(UserRoleType.ROLE_USER);
            roleRepository.save(userRole);
        }
        if (!roleRepository.existsByUserRoleType(UserRoleType.ROLE_ADMIN)) {
            UserRole userRole = new UserRole();
            userRole.setUserRoleType(UserRoleType.ROLE_ADMIN);
            roleRepository.save(userRole);
        }
    }
}
