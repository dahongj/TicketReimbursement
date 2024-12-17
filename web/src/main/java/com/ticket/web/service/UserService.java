package com.ticket.web.service;

import com.ticket.web.repository.*;
import com.ticket.web.models.*;
import com.ticket.web.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UsersRepository userRepository;

    public Users registerUser(String username, String password) throws PreexistingUsernameException{
        Optional<Users> userExist = userRepository.findByUsername(username);
        if (userExist.isPresent()) {
            throw new PreexistingUsernameException("Username exists");
        }

        Users newuser = new Users(username, password);
        return userRepository.save(newuser);
    }

    public Users loginUser(String username, String password) throws IncorrectLoginException{
        Optional<Users> userExist = userRepository.findByUsernameAndPassword(username,password);
        if (userExist.isPresent()){
            Users user = userExist.get();
            return user;
        }else{
            throw new IncorrectLoginException("Incorrect Credentials");
        }
    }
}
