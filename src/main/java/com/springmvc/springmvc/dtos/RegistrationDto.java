package com.springmvc.springmvc.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationDto {
private Long id;
@NotEmpty
private String username;
@NotEmpty
private String email;
@NotEmpty
private String password;
}
