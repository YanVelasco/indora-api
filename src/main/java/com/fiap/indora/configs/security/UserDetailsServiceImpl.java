package com.fiap.indora.configs.security;

import com.fiap.indora.model.UserModel;
import com.fiap.indora.repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    final UserRepository userRepository;

    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        UserModel userModel = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Email not found: " + email));

        return UserDetailsImpl.build(userModel);
    }

    public UserDetails loadUserById(UUID uuid) throws AuthenticationCredentialsNotFoundException {
        UserModel userModel = userRepository.findByUserId(uuid)
                .orElseThrow(() -> new AuthenticationCredentialsNotFoundException("User not found with id: " + uuid));

        return UserDetailsImpl.build(userModel);
    }

}
