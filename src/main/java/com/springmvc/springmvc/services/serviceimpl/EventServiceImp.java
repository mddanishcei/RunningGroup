package com.springmvc.springmvc.services.serviceimpl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.springmvc.springmvc.dtos.EventDto;
import com.springmvc.springmvc.mappers.EventMapper;
import com.springmvc.springmvc.models.Club;
import com.springmvc.springmvc.models.Event;
import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.repository.ClubRepository;
import com.springmvc.springmvc.repository.EventRepository;
import com.springmvc.springmvc.services.BaseService;
import com.springmvc.springmvc.services.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EventServiceImp implements BaseService<EventDto,Long> {

    private final EventRepository eventRepository;

    private final ClubRepository clubRepository;
    private final UserService userService;
    private final EmailService emailService;

    
    public void createEvent(Long clubId, EventDto eventDto) {
        Club club = clubRepository.findById(clubId).get();
        Event event = EventMapper.mapToEvent(eventDto);
        event.setClub(club);
        eventRepository.save(event);
        List<UserEntity> users = userService.findAll();
        emailService.sendingEmailToUser(users, eventDto);
    }

    @Override
    public List<EventDto> findAll() {
        List<Event> events = eventRepository.findAll();
        return events.stream().map((event) -> EventMapper.mapToEventDto(event)).collect(Collectors.toList());
    }

    @Override
    public EventDto findById(Long EventId) {
        Event event = eventRepository.findById(EventId).get();
        return EventMapper.mapToEventDto(event);
    }

    @Override
    public void update(EventDto eventDto) {
        Event event = EventMapper.mapToEvent(eventDto);
        eventRepository.save(event);
    }

    @Override
    public void delete(Long eventId) {
        eventRepository.deleteById(eventId);
    }

}
