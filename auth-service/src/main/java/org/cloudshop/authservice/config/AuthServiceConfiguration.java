package org.cloudshop.authservice.config;

import org.cloudshop.authservice.AuthenticatedRequestFilter;
import org.cloudshop.authservice.domain.adapters.UserAuthenticatesAdapter;
import org.cloudshop.authservice.domain.adapters.WhenIsNotAuthenticatedAdapter;
import org.cloudshop.authservice.jwt.JwtTokenUtil;
import org.cloudshop.authservice.ports.UserAuthenticates;
import org.cloudshop.authservice.ports.UserServiceClient;
import org.cloudshop.authservice.ports.WhenIsNotAuthenticated;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthServiceConfiguration extends WebSecurityConfigurerAdapter {

    @Bean
    public JwtTokenUtil jwtTokenUtil() {
        return new JwtTokenUtil();
    }

    @Bean
    public UserAuthenticates userAuthenticatesAdapter(AuthenticationManager authenticationManagerBean,
            JwtTokenUtil jwtTokenUtil,
            UserServiceClient userServiceClient) {
        return new UserAuthenticatesAdapter(authenticationManagerBean, jwtTokenUtil, userServiceClient);
    }

    @Bean
    public WhenIsNotAuthenticated whenIsNotAuthenticated() {
        return new WhenIsNotAuthenticatedAdapter();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticatedRequestFilter authenticatedRequestFilter(UserServiceClient userServiceClient, JwtTokenUtil jwtTokenUtil) {
        return new AuthenticatedRequestFilter(userServiceClient, jwtTokenUtil);
    }
}
