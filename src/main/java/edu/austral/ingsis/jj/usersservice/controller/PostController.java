package edu.austral.ingsis.jj.usersservice.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts")
public class PostController {

    @GetMapping("/authenticateUser")
    public String changeUserPassword(){
        return "ok";
    }
}
