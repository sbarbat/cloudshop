package org.cloudshop.userservice;

import org.cloudshop.userservice.domain.adapters.UserRegisterAdapter;
import org.cloudshop.userservice.domain.adapters.UserUpdatesProfileAdapter;
import org.cloudshop.userservice.domain.adapters.UserUpdatesSettingsAdapter;
import org.cloudshop.userservice.memorystore.UserRepositoryImpl;
import org.cloudshop.userservice.ports.UserRegisters;
import org.cloudshop.userservice.ports.UserRepository;
import org.cloudshop.userservice.ports.UserUpdatesProfile;
import org.cloudshop.userservice.ports.UserUpdatesSettings;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserServiceConfiguration {

    @Bean
    public UserUpdatesProfile userUpdatesProfile() {
        return new UserUpdatesProfileAdapter();
    }

    @Bean
    public UserUpdatesSettings userUpdatesSettings() {
        return new UserUpdatesSettingsAdapter();
    }

    @Bean
    public UserRepository userRepository() {
        return new UserRepositoryImpl();
    }

    @Bean
    public UserRegisters userRegisters(UserRepository userRepository) {
        return new UserRegisterAdapter(userRepository);
    }
}
