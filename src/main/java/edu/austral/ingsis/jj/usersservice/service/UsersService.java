package edu.austral.ingsis.jj.usersservice.service;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UsersService {

    public List<String> getAllUsers(){
        return Arrays.asList("pepito", "juancito");
    }
}
