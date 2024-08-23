package com.springmvc.springmvc.repository;


import org.springframework.stereotype.Repository;

import com.springmvc.springmvc.models.UserEntity;

@Repository
public interface UserRepository extends BaseRepository<UserEntity,Long>{
    UserEntity findByEmail(String email);

    UserEntity findByUsername(String username);

    UserEntity findFirstByUsername(String username);

}
