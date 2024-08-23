package com.springmvc.springmvc.dtos;
import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import com.springmvc.springmvc.models.Club;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class EventDto extends BaseDto{
    @NotEmpty(message = "Event Name should not be empty")
    private String name;
    @NotNull(message = "Event StartTime should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime startTime;
    @NotNull(message = "Event EndTime should not be empty")
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime endTime;
    @NotEmpty(message = "Type should not be empty")
    private String type;
    private LocalDateTime updatedON;
    private Club club;

}
