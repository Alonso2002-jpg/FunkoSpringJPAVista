package org.develop.FunkoSpringJpa.rest.auth.services.users;

import org.develop.FunkoSpringJpa.rest.auth.repositories.AuthRepository;
import org.develop.FunkoSpringJpa.rest.users.exceptions.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class AuthUserServiceImpl implements AuthUserService{
    private final AuthRepository authRepository;

    @Autowired
    public AuthUserServiceImpl(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    @Override
    public UserDetails loadUserByUsername(String username) {
        return authRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("username " + username));
    }
}
