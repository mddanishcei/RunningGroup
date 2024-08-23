package com.springmvc.springmvc.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.springmvc.springmvc.models.Role;
import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.repository.UserRepository;

import java.util.Collections;

public class CustomUserDetailsServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testLoadUserByUsername_UserFound() {
        String username = "testuser";
        String password = "password";
        Role role = new Role();
        role.setName("USER");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(password);
        userEntity.setRoles(Collections.singletonList(role));

        when(userRepository.findFirstByUsername(username)).thenReturn(userEntity);
        
        UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

        assertEquals(username, userDetails.getUsername());
        assertEquals(password, userDetails.getPassword());
        assertEquals(1, userDetails.getAuthorities().size());
        assertEquals(new SimpleGrantedAuthority("USER"), userDetails.getAuthorities().iterator().next());
    }

    @Test
    public void testLoadUserByUsername_UserNotFound() {
        String username = "nonexistentuser";
        when(userRepository.findFirstByUsername(username)).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> {
            customUserDetailsService.loadUserByUsername(username);
        });
    }
}
