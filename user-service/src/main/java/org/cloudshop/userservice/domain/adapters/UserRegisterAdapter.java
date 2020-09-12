package org.cloudshop.userservice.domain.adapters;

import org.cloudshop.userservice.domain.User;
import org.cloudshop.userservice.ports.UserRegisters;
import org.cloudshop.userservice.ports.UserRepository;

public class UserRegisterAdapter implements UserRegisters {

    private final UserRepository userRepository;

    public UserRegisterAdapter(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void register(User user) {
        this.userRepository.save(user);
    }
}
