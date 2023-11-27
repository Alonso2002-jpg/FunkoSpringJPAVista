package org.develop.FunkoSpringJpa.rest.auth.services.users;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AuthUserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username);
}
