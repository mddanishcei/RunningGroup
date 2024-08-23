package com.springmvc.springmvc.models;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Entity(name = "roles")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends UserModel{
    
    private String name;
    @Builder.Default
    @ManyToMany(mappedBy = "roles")
    private List<UserEntity> users=new ArrayList<>();
}
