package com.springmvc.springmvc.mappers;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collections;

import org.junit.jupiter.api.Test;
import com.springmvc.springmvc.dtos.ClubDto;
import com.springmvc.springmvc.models.Club;
import com.springmvc.springmvc.models.UserEntity;

public class ClubMapperTest {

    @Test
    public void testMapToClub() {
        ClubDto clubDto = ClubDto.builder()
                .id(1L)
                .title("Test Club")
                .photoUrl("http://example.com/photo.jpg")
                .content("This is a test club")
                .createdBy(new UserEntity())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .build();

        Club club = ClubMapper.mapToClub(clubDto);

        assertThat(club).isNotNull();
        assertThat(club.getId()).isEqualTo(clubDto.getId());
        assertThat(club.getTitle()).isEqualTo(clubDto.getTitle());
        assertThat(club.getPhotoUrl()).isEqualTo(clubDto.getPhotoUrl());
        assertThat(club.getContent()).isEqualTo(clubDto.getContent());
        assertThat(club.getCreatedBy()).isEqualTo(clubDto.getCreatedBy());
        assertThat(club.getCreatedOn()).isEqualTo(clubDto.getCreatedOn());
        assertThat(club.getUpdatedOn()).isEqualTo(clubDto.getUpdatedOn());
    }

    @Test
    public void testMapToClubDto() {
        Club club = Club.builder()
                .id(1L)
                .title("Test Club")
                .photoUrl("http://example.com/photo.jpg")
                .content("This is a test club")
                .createdBy(new UserEntity())
                .createdOn(LocalDateTime.now())
                .updatedOn(LocalDateTime.now())
                .events(Collections.emptyList()) 
                .build();

        ClubDto clubDto = ClubMapper.mapToClubDto(club);

        assertThat(clubDto).isNotNull();
        assertThat(clubDto.getId()).isEqualTo(club.getId());
        assertThat(clubDto.getTitle()).isEqualTo(club.getTitle());
        assertThat(clubDto.getPhotoUrl()).isEqualTo(club.getPhotoUrl());
        assertThat(clubDto.getContent()).isEqualTo(club.getContent());
        assertThat(clubDto.getCreatedBy()).isEqualTo(club.getCreatedBy());
        assertThat(clubDto.getCreatedOn()).isEqualTo(club.getCreatedOn());
        assertThat(clubDto.getUpdatedOn()).isEqualTo(club.getUpdatedOn());
        assertThat(clubDto.getEvents()).isEmpty(); 
    }
}

