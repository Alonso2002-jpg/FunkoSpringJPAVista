package org.develop.FunkoSpringJpa.config.auth;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.develop.FunkoSpringJpa.rest.auth.services.jwt.JwtService;
import org.develop.FunkoSpringJpa.rest.auth.services.users.AuthUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final AuthUserService authUserService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, AuthUserService authUserService) {
        this.jwtService = jwtService;
        this.authUserService = authUserService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("Starting JWT authentication filter...");
        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        UserDetails userDetails = null;
        String username;

        if (!StringUtils.hasText(authHeader) || !StringUtils.startsWithIgnoreCase(authHeader,"Bearer ")) {
            log.info("No Authentication header found");
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Authentication header found");

        jwt = authHeader.substring(7);

        try {
            username = jwtService.extractUserName(jwt);
        }catch (Exception e) {
            log.info("Invalid JWT token");
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid JWT token");
            return;
        }

        log.info("Authenticating user: {}", username);
        if (StringUtils.hasText(username) && SecurityContextHolder.getContext().getAuthentication() == null) {
                log.info("Checking JWT token and username");
                try {
                    userDetails = authUserService.loadUserByUsername(username);
                }catch (Exception e) {
                    log.info("User Not Found: {}", username);
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "User Not Authorized");
                    return;
                }

                log.info("User found: {}", username);
                if (jwtService.isTokenValid(jwt,userDetails)){
                    log.info("Valid JWT token");
                    SecurityContext context = SecurityContextHolder.createEmptyContext();

                    UsernamePasswordAuthenticationToken authToke = new UsernamePasswordAuthenticationToken(
                            userDetails,null,userDetails.getAuthorities()
                    );
                    authToke.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    context.setAuthentication(authToke);

                    SecurityContextHolder.setContext(context);
                }

            filterChain.doFilter(request, response);
        }
    }
}
