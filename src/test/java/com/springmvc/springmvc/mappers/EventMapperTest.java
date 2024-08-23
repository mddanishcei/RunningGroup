package com.springmvc.springmvc.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import com.springmvc.springmvc.dtos.EventDto;
import com.springmvc.springmvc.models.Event;

public class EventMapperTest {

    @Test
    public void testMapToEvent() {
        EventDto eventDto = EventDto.builder()
                .id(1L)
                .name("Test Event")
                .startTime(LocalDateTime.of(2024, 8, 5, 10, 0))
                .endTime(LocalDateTime.of(2024, 8, 5, 12, 0))
                .type("Conference")
                .photoUrl("http://example.com/photo.jpg")
                .createdOn(LocalDateTime.now())
                .updatedON(LocalDateTime.now())
                .club(null)
                .build();

        Event event = EventMapper.mapToEvent(eventDto);

        assertThat(event).isNotNull();
        assertThat(event.getId()).isEqualTo(eventDto.getId());
        assertThat(event.getName()).isEqualTo(eventDto.getName());
        assertThat(event.getStartTime()).isEqualTo(eventDto.getStartTime());
        assertThat(event.getEndTime()).isEqualTo(eventDto.getEndTime());
        assertThat(event.getType()).isEqualTo(eventDto.getType());
        assertThat(event.getPhotoUrl()).isEqualTo(eventDto.getPhotoUrl());
        assertThat(event.getCreatedOn()).isEqualTo(eventDto.getCreatedOn());
        assertThat(event.getUpdatedON()).isEqualTo(eventDto.getUpdatedON());
        assertThat(event.getClub()).isEqualTo(eventDto.getClub());
    }

    @Test
    public void testMapToEventDto() {
        Event event = Event.builder()
                .id(1L)
                .name("Test Event")
                .startTime(LocalDateTime.of(2024, 8, 5, 10, 0))
                .endTime(LocalDateTime.of(2024, 8, 5, 12, 0))
                .type("Conference")
                .photoUrl("http://example.com/photo.jpg")
                .createdOn(LocalDateTime.now())
                .updatedON(LocalDateTime.now())
                .club(null) 
                .build();

        EventDto eventDto = EventMapper.mapToEventDto(event);

        assertThat(eventDto).isNotNull();
        assertThat(eventDto.getId()).isEqualTo(event.getId());
        assertThat(eventDto.getName()).isEqualTo(event.getName());
        assertThat(eventDto.getStartTime()).isEqualTo(event.getStartTime());
        assertThat(eventDto.getEndTime()).isEqualTo(event.getEndTime());
        assertThat(eventDto.getType()).isEqualTo(event.getType());
        assertThat(eventDto.getPhotoUrl()).isEqualTo(event.getPhotoUrl());
        assertThat(eventDto.getCreatedOn()).isEqualTo(event.getCreatedOn());
        assertThat(eventDto.getUpdatedON()).isEqualTo(event.getUpdatedON());
        assertThat(eventDto.getClub()).isEqualTo(event.getClub());
    }
}

