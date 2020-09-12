package org.cloudshop.authservice.ports;

import org.cloudshop.authservice.domain.AuthUser;
import org.cloudshop.authservice.domain.AuthUserDetails;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@FeignClient("user-service")
public interface UserServiceClient extends UserDetailsService {

    @RequestMapping(method = RequestMethod.GET, value = "/user/email/{email}")
    AuthUser getUserByEmail(@RequestParam(value="email") String email);

    /**
     * Method for Spring auth system
     *
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    default UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AuthUser authUser = this.getUserByEmail(username);

        if (authUser == null) {
            throw new UsernameNotFoundException("Username ["+ username +"] not found");
        }

        return new AuthUserDetails(authUser.getEmail(), authUser.getPassword());
    }
}
