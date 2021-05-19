package edu.austral.ingsis.jj.usersservice.utils;

import edu.austral.ingsis.jj.usersservice.config.UserDetailsImpl;
import edu.austral.ingsis.jj.usersservice.config.UserDetailsServiceImpl;
import edu.austral.ingsis.jj.usersservice.exceptions.NotFoundException;
import edu.austral.ingsis.jj.usersservice.exceptions.UnauthorizedException;
import edu.austral.ingsis.jj.usersservice.model.user.User;
import edu.austral.ingsis.jj.usersservice.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SessionUtils {

    private final UserRepository userRepository;

    public SessionUtils(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getTokenUserInformation() {
        Authentication jwt = SecurityContextHolder.getContext().getAuthentication();
        if (jwt == null) throw new UnauthorizedException("Error while getting session token");

        UserDetailsImpl user = (UserDetailsImpl) jwt.getPrincipal();
        Optional<User> found = this.userRepository.findByUsername(user.getUsername());
        if (found.isEmpty()) throw new NotFoundException("Token user not found");
        return found.get();
    }
}
