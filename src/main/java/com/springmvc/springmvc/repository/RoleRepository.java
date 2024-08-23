package com.springmvc.springmvc.repository;


import org.springframework.stereotype.Repository;

import com.springmvc.springmvc.models.Role;

@Repository
public interface RoleRepository extends BaseRepository<Role,Long>{
    Role findByName(String name);
}
