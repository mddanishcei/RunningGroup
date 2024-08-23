package com.springmvc.springmvc.mappers;

import java.util.stream.Collectors;

import com.springmvc.springmvc.dtos.ClubDto;
import com.springmvc.springmvc.models.Club;

public final class ClubMapper {
    public static Club mapToClub(ClubDto clubDto) {
        Club club = Club.builder()
                .id(clubDto.getId())
                .title(clubDto.getTitle())
                .photoUrl(clubDto.getPhotoUrl())
                .content(clubDto.getContent())
                .createdBy(clubDto.getCreatedBy())
                .createdOn(clubDto.getCreatedOn())
                .updatedOn(clubDto.getUpdatedOn())
                .build();
        return club;
    }

    public static ClubDto mapToClubDto(Club club){
        ClubDto clubDto=ClubDto.builder()
            .id(club.getId())
            .title(club.getTitle())
            .photoUrl(club.getPhotoUrl())
            .content(club.getContent())
            .createdBy(club.getCreatedBy())
            .createdOn(club.getCreatedOn())
            .updatedOn(club.getUpdatedOn())
            .events(club.getEvents().stream().map((event)->EventMapper.mapToEventDto(event)).collect(Collectors.toList()))
            .build();

        return clubDto;
    }
}
