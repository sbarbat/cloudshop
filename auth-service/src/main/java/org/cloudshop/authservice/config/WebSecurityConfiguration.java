package org.cloudshop.authservice.config;

import org.cloudshop.authservice.AuthenticatedRequestFilter;
import org.cloudshop.authservice.ports.UserServiceClient;
import org.cloudshop.authservice.ports.WhenIsNotAuthenticated;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Order(1)
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final ApplicationContext context;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticatedRequestFilter authenticatedRequestFilter;
    private final WhenIsNotAuthenticated whenIsNotAuthenticated;

    public WebSecurityConfiguration(ApplicationContext context,
            BCryptPasswordEncoder bCryptPasswordEncoder,
            AuthenticatedRequestFilter authenticatedRequestFilter,
            WhenIsNotAuthenticated whenIsNotAuthenticated) {
        this.context = context;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.authenticatedRequestFilter = authenticatedRequestFilter;
        this.whenIsNotAuthenticated = whenIsNotAuthenticated;
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        // configure AuthenticationManager so that it knows from where to load
        // user for matching credentials
        // Use BCryptPasswordEncoder
        auth.userDetailsService(this.context.getBean(UserServiceClient.class))
                .passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                // dont authenticate this particular request
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, "/authenticate").permitAll()
                // all other requests need to be authenticated
                .anyRequest().authenticated().and()
                .formLogin().disable()
                .exceptionHandling().authenticationEntryPoint(this.whenIsNotAuthenticated).and().sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        // Add a filter to validate the tokens with every request
        http.addFilterAfter(this.authenticatedRequestFilter, UsernamePasswordAuthenticationFilter.class);

    }
}
