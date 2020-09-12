package org.cloudshop.userservice.memorystore;

import org.cloudshop.userservice.domain.User;
import org.cloudshop.userservice.ports.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserRepositoryImpl implements UserRepository {

    private final List<User> users;

    public UserRepositoryImpl() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        this.users = new ArrayList<>();

        User user1 = new User();
        user1.setId("id1");
        user1.setEmail("user1@email.com");
        user1.setPassword(encoder.encode("123123"));
        user1.setFirstname("User");
        user1.setLastname("One");
        this.users.add(user1);

        User user2 = new User();
        user2.setId("id2");
        user2.setEmail("user2@email.com");
        user2.setPassword(encoder.encode("123123"));
        user2.setFirstname("User");
        user2.setLastname("Two");
        this.users.add(user2);
    }

    @Override
    public Optional<User> findById(String uuid) {
        return this.users.stream()
                .filter(u -> u.getId().equals(uuid))
                .findFirst();
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return this.users.stream()
                .filter(u -> u.getEmail().equals(email))
                .findFirst();
    }

    @Override
    public void save(User user) {
        this.users.add(user);
    }
}
