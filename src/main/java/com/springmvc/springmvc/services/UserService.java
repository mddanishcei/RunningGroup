package com.springmvc.springmvc.services;

import java.util.List;

import com.springmvc.springmvc.dtos.RegistrationDto;
import com.springmvc.springmvc.models.UserEntity;

public interface UserService {
    public void saveUser(RegistrationDto registrationDto);
    public UserEntity findByEmail(String email);
    public UserEntity findByUserName(String username);
    public List<UserEntity> findAll();
}
