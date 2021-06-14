package edu.austral.ingsis.jj.usersservice;

import edu.austral.ingsis.jj.usersservice.config.UserDetailsImpl;
import edu.austral.ingsis.jj.usersservice.model.user.User;
import edu.austral.ingsis.jj.usersservice.model.user.UserRole;
import edu.austral.ingsis.jj.usersservice.model.user.UserRoleType;
import edu.austral.ingsis.jj.usersservice.repository.RoleRepository;
import edu.austral.ingsis.jj.usersservice.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Collections;
import java.util.Date;

public class WithTestUserSecurityContextFactory implements WithSecurityContextFactory<WithTestUser> {

    @Value("${JJ_SECRET}")
    private String jwtSecret;

    @Value("${JJ_EXPIRATION}")
    private int jwtExpirationMs;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Override
    public SecurityContext createSecurityContext(WithTestUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

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

        User user = new User(customUser.username(), customUser.email(), customUser.password(), customUser.firstName(), customUser.lastName(), roleRepository.findByUserRoleType(UserRoleType.ROLE_USER).get());
        userRepository.save(user);
        UserDetailsImpl principal = UserDetailsImpl.build(user);

        String jwt = Jwts.builder()
                .setSubject(customUser.username())
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();

        Authentication auth = new UsernamePasswordAuthenticationToken(principal, jwt, Collections.singletonList(new SimpleGrantedAuthority(customUser.role())));
        context.setAuthentication(auth);
        return context;
    }
}
