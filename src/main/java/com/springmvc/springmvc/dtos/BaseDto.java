package com.springmvc.springmvc.dtos;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
abstract class BaseDto {
    private Long id;
    private LocalDateTime createdOn;   
    @NotEmpty(message = "PhotoUrl should not be empty")
    private String photoUrl;
}
