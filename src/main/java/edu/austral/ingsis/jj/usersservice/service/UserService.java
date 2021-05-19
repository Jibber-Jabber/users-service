package edu.austral.ingsis.jj.usersservice.service;

import edu.austral.ingsis.jj.usersservice.dto.ProfileEditDto;
import edu.austral.ingsis.jj.usersservice.dto.RegisterRequestDto;
import edu.austral.ingsis.jj.usersservice.dto.UserDataDto;
import edu.austral.ingsis.jj.usersservice.exceptions.BadRequestException;
import edu.austral.ingsis.jj.usersservice.model.user.User;
import edu.austral.ingsis.jj.usersservice.model.user.UserRole;
import edu.austral.ingsis.jj.usersservice.model.user.UserRoleType;
import edu.austral.ingsis.jj.usersservice.repository.RoleRepository;
import edu.austral.ingsis.jj.usersservice.repository.UserRepository;
import edu.austral.ingsis.jj.usersservice.utils.SessionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final RoleRepository roleRepository;

    private final SessionUtils sessionUtils;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder encoder, RoleRepository roleRepository, SessionUtils sessionUtils, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.sessionUtils = sessionUtils;
        this.passwordEncoder = passwordEncoder;
    }

    public List<String> getAllUsers(){
        return Arrays.asList("pepito", "juancito");
    }

    public void registerUser(RegisterRequestDto registerRequest){

        if (userRepository.existsByUsername(registerRequest.getUsername()))
            throw new BadRequestException("Username already in use");

        if (userRepository.existsByEmail(registerRequest.getEmail()))
            throw new BadRequestException("Email already in use");


        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setEmail(registerRequest.getEmail());
        user.setPassword(encoder.encode(registerRequest.getPassword()));
        user.setFirstName(registerRequest.getFirstName());
        user.setLastName(registerRequest.getLastName());

        UserRole role = roleRepository.findByUserRoleType(UserRoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role not found."));

        user.setRole(role);
        userRepository.save(user);
    }

    public UserDataDto editProfile(ProfileEditDto profileEditDto) {
        User currentUser = sessionUtils.getTokenUserInformation();
        if (passwordEncoder.matches(profileEditDto.getPassword(), currentUser.getPassword())){
            if (profileEditDto.getNewPassword() != null){
                currentUser.setPassword(passwordEncoder.encode(profileEditDto.getNewPassword()));
            }
            if (profileEditDto.getEmail() != null){
                currentUser.setEmail(profileEditDto.getEmail());
            }
            if (profileEditDto.getFirstName() != null){
                currentUser.setFirstName(profileEditDto.getFirstName());
            }
            if (profileEditDto.getLastName() != null){
                currentUser.setLastName(profileEditDto.getLastName());
            }
            userRepository.save(currentUser);
            return UserDataDto.from(currentUser);
        }else throw new BadRequestException("Invalid Password");
    }
}
