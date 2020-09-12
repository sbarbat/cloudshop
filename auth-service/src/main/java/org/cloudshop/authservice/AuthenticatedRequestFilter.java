package org.cloudshop.authservice;

import io.jsonwebtoken.ExpiredJwtException;
import lombok.extern.slf4j.Slf4j;
import org.cloudshop.authservice.domain.AuthUserDetails;
import org.cloudshop.authservice.jwt.JwtTokenUtil;
import org.cloudshop.authservice.ports.UserServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

@Slf4j
public class AuthenticatedRequestFilter extends OncePerRequestFilter {

    private final UserServiceClient userServiceClient;
    private final JwtTokenUtil jwtTokenUtil;

    @Autowired
    public AuthenticatedRequestFilter(UserServiceClient userServiceClient,
            JwtTokenUtil jwtTokenUtil) {
        this.userServiceClient = userServiceClient;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String requestTokenHeader = request.getHeader("Authorization");

        if ("OPTIONS".equals(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_OK);

            filterChain.doFilter(request, response);
        }

        String email = null;
        String token = null;
        // JWT Token is in the form "Bearer token". Remove Bearer word and get only the Token
        if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
            token = requestTokenHeader.substring(7);

            try {
                email = jwtTokenUtil.getEmailFromToken(token);
            } catch (IllegalArgumentException e) {
                this.logger.error("Unable to get JWT Token");
            } catch (ExpiredJwtException e) {
                this.logger.error("Token expired");
            }
        } else {
            this.logger.warn("JWT Token does not begin with Bearer String");
        }

        //Once we get the token validate it.
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Do no setup with password on security context (to not call the user service which is expensive)
            UserDetails userDetails = new AuthUserDetails(email, "");

            // if token is valid configure Spring Security to manually set authentication
            if (jwtTokenUtil.validateToken(token, userDetails)) {
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, new ArrayList<>());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // After setting the Authentication in the context, we specify
                // that the current user is authenticated. So it passes the Spring Security Configurations successfully.
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
