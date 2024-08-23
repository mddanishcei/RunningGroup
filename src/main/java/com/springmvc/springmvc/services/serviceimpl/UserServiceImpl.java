package com.springmvc.springmvc.services.serviceimpl;


import java.util.Arrays;
import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springmvc.springmvc.dtos.RegistrationDto;
import com.springmvc.springmvc.models.Role;
import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.repository.RoleRepository;
import com.springmvc.springmvc.repository.UserRepository;
import com.springmvc.springmvc.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
  
    private final UserRepository userRepository;
    
    private final RoleRepository roleRepository;
  
    private final PasswordEncoder passwordEncoder;

    @Override
    public void saveUser(RegistrationDto registrationDto) {
        UserEntity user=new UserEntity();
        user.setUsername(registrationDto.getUsername());
        user.setEmail(registrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        Role roles=roleRepository.findByName("USER");
        user.setRoles(Arrays.asList(roles));
        userRepository.save(user);
    }

    @Override
    public UserEntity findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public UserEntity findByUserName(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }

}
