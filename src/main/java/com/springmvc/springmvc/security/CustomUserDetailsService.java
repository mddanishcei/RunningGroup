package com.springmvc.springmvc.security;

import java.util.stream.Collectors;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity user = userRepository.findFirstByUsername(username);
        
        if (user != null) {
           
            User authUser = new User(user.getUsername(), user.getPassword(), user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList()));
            return authUser;
        } else {
            throw new UsernameNotFoundException("invalid username and password");

        }
    }

}
