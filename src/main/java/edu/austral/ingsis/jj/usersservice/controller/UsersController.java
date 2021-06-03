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
    public UserDataDto editUserProfile(@Valid @RequestBody ProfileEditDto profileEditDto){
        return userService.editProfile(profileEditDto);
    }

    @PostMapping("/followUser")
    public void followUserById(@RequestParam(value = "userId") String userId){
        userService.followUser(userId);
    }

    @PostMapping("/unfollowUser")
    public void unfollowUserById(@RequestParam(value = "userId") String userId){
        userService.unfollowUser(userId);
    }

    @GetMapping("/searchUser")
    public List<UserDataDto> searchUsersByUsername(@RequestParam(value = "username") String username){
        return userService.searchUserByUsername(username);
    }

    @GetMapping("/userInfo/{userId}")
    public UserDataDto getUserInfoById(@PathVariable("userId") String userId){
        return userService.getUserDataById(userId);
    }

    @GetMapping("/followedUsers")
    public List<UserDataDto> getFollowedUsersInfo(){
        return userService.getFollowedUsersInfo();
    }

    @GetMapping("/authenticateUser")
    public UserDataDto changeUserPassword(){
        return userService.getUserData();
    }
}
