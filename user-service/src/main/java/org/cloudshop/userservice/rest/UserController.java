package org.cloudshop.userservice.rest;

import org.cloudshop.userservice.domain.User;
import org.cloudshop.userservice.ports.UserRegisters;
import org.cloudshop.userservice.ports.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class UserController {

    private final UserRegisters userRegisters;
    private final UserRepository userRepository;

    @Autowired
    public UserController(UserRegisters userRegisters,
            UserRepository userRepository) {
        this.userRegisters = userRegisters;
        this.userRepository = userRepository;
    }

    @GetMapping("/user/{id}")
    @ResponseBody
    public Optional<User> getUser(@PathVariable String id) {
        return this.userRepository.findById(id);
    }

    @GetMapping("/user/email/{email}")
    @ResponseBody
    public Optional<User> getUserByEmail(@PathVariable String email) {
        return this.userRepository.findByEmail(email);
    }

    @PostMapping("/register")
    public void register(User user) {
        this.userRegisters.register(user);
    }
}
