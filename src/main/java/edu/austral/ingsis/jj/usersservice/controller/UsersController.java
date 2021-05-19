package edu.austral.ingsis.jj.usersservice.controller;

import edu.austral.ingsis.jj.usersservice.dto.ProfileEditDto;
import edu.austral.ingsis.jj.usersservice.dto.UserDataDto;
import edu.austral.ingsis.jj.usersservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("users")
public class UsersController {

    private final UserService userService;

    @Autowired
    public UsersController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    public List<String> getAllUsers(){
        return userService.getAllUsers();
    }

    @PutMapping("/editProfile")
    public UserDataDto changeUserPassword(@Valid @RequestBody ProfileEditDto profileEditDto){
        return userService.editProfile(profileEditDto);
    }
}
