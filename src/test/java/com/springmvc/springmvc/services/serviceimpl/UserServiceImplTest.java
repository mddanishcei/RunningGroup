package com.springmvc.springmvc.services.serviceimpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.springmvc.springmvc.dtos.RegistrationDto;
import com.springmvc.springmvc.models.Role;
import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.repository.RoleRepository;
import com.springmvc.springmvc.repository.UserRepository;

public class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserEntity user;
    private Role role;
    private RegistrationDto registrationDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new UserEntity();
        user.setId(1L);
        user.setUsername("testuser");
        user.setEmail("testuser@example.com");
        user.setPassword("password");

        role = new Role();
        role.setId(1L);
        role.setName("USER");

        registrationDto = new RegistrationDto();
        registrationDto.setUsername("testuser");
        registrationDto.setEmail("testuser@example.com");
        registrationDto.setPassword("password");
    }

    @Test
    public void testSaveUser() {
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(roleRepository.findByName("USER")).thenReturn(role);

        userService.saveUser(registrationDto);

        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    public void testFindByEmail() {
        when(userRepository.findByEmail("testuser@example.com")).thenReturn(user);

        UserEntity result = userService.findByEmail("testuser@example.com");

        assertNotNull(result);
        assertEquals("testuser@example.com", result.getEmail());
    }

    @Test
    public void testFindByUserName() {
        when(userRepository.findByUsername("testuser")).thenReturn(user);

        UserEntity result = userService.findByUserName("testuser");

        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
    }

    @Test
    public void testFindAll() {
        List<UserEntity> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        List<UserEntity> result = userService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
    }
}
