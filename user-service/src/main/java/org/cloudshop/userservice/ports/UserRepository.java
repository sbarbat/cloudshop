package org.cloudshop.userservice.ports;

import org.cloudshop.userservice.domain.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(String uuid);

    Optional<User> findByEmail(String email);

    void save(User user);
}
