package edu.austral.ingsis.jj.usersservice.controller;

import edu.austral.ingsis.jj.usersservice.dto.UserDataDto;
import edu.austral.ingsis.jj.usersservice.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @Autowired
    UserService userService;

    @GetMapping("/authenticateUser")
    public UserDataDto changeUserPassword(){
        return userService.getUserData();
    }

    @PostMapping("/userInfo")
    public UserDataDto getUserInfoById(@RequestHeader("userId") String userId){
        return userService.getUserDataById(userId);
    }
}
