package com.springmvc.springmvc.dtos;

import java.time.LocalDateTime;
import java.util.List;

import com.springmvc.springmvc.models.UserEntity;

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
public class ClubDto extends BaseDto{
    
    @NotEmpty(message = "Club title should not be empty")
    private String title;
    @NotEmpty(message = "Club Content should not be empty")
    private String content;
    private UserEntity createdBy;
    
    private LocalDateTime updatedOn;
    private List<EventDto> events;
}
