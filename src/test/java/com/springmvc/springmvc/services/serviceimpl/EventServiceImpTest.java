package com.springmvc.springmvc.services.serviceimpl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.springmvc.springmvc.dtos.EventDto;
import com.springmvc.springmvc.mappers.EventMapper;
import com.springmvc.springmvc.models.Club;
import com.springmvc.springmvc.models.Event;
import com.springmvc.springmvc.repository.ClubRepository;
import com.springmvc.springmvc.repository.EventRepository;

public class EventServiceImpTest {

    @InjectMocks
    private EventServiceImp eventService;

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ClubRepository clubRepository;

    private Club club;
    private Event event;
    private EventDto eventDto;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        club = new Club();
        club.setId(1L);
        club.setTitle("Test Club");

        event = new Event();
        event.setId(1L);
        event.setName("Test Event");
        event.setClub(club);

        eventDto = EventMapper.mapToEventDto(event);
    }

    @Test
    public void testCreateEvent() {
        when(clubRepository.findById(1L)).thenReturn(Optional.of(club));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        eventService.createEvent(1L, eventDto);

        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testFindAllEvents() {
        List<Event> events = Arrays.asList(event);
        when(eventRepository.findAll()).thenReturn(events);

        List<EventDto> result = eventService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Test Event", result.get(0).getName());
    }

    @Test
    public void testFindByEventId() {
        when(eventRepository.findById(1L)).thenReturn(Optional.of(event));

        EventDto result = eventService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Event", result.getName());
    }

    @Test
    public void testUpdateEvent() {
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        eventService.update(eventDto);

        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    public void testDeleteEvent() {
        eventService.delete(1L);

        verify(eventRepository, times(1)).deleteById(1L);
    }
}
