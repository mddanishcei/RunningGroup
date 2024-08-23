package com.springmvc.springmvc.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import com.springmvc.springmvc.dtos.EventDto;
import com.springmvc.springmvc.models.UserEntity;
import com.springmvc.springmvc.security.SecurityUtil;
import com.springmvc.springmvc.services.UserService;
import com.springmvc.springmvc.services.serviceimpl.EventServiceImp;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class EventController implements BaseController<EventDto>{
  
  private final EventServiceImp eventService;
  private final UserService userService;

  @GetMapping("/events")
  public String listAll(Model model) {
    UserEntity user = new UserEntity();
    List<EventDto> events = eventService.findAll();
    String username = SecurityUtil.getSessionUser();
    if (username != null) {
      user = userService.findByUserName(username);
      model.addAttribute("user", user);
    }
    model.addAttribute("user", user);
    model.addAttribute("events", events);
    return "events-list";
  }

  @GetMapping("/events/{eventId}")
  public String detail(@PathVariable long eventId, Model model) {
    UserEntity user = new UserEntity();
    EventDto eventDto = eventService.findById(eventId);
    String username = SecurityUtil.getSessionUser();
    if (username != null) {
      user = userService.findByUserName(username);
      model.addAttribute("user", user);
    }
    model.addAttribute("user", user);
    model.addAttribute("event", eventDto);
    return "evetns-detail";
  }

  @GetMapping("/events/{eventId}/edit")
  public String edit(@PathVariable long eventId, Model model) {
    EventDto event = eventService.findById(eventId);
    model.addAttribute("event", event);
    return "events-edit";
  }

  @PostMapping("/events/{eventId}/edit")
  public String update(@PathVariable long eventId, @Valid @ModelAttribute EventDto event, BindingResult result,
      Model model) {
    if (result.hasErrors()) {
      model.addAttribute("event", event);
      return "events-list";
    }
    EventDto eventDto = eventService.findById(eventId);
    event.setId(eventId);
    event.setClub(eventDto.getClub());
    eventService.update(event);
    return "redirect:/events";

  }

  @GetMapping("/events/{clubId}/new")
  public String create(@PathVariable long clubId, Model model) {
    EventDto event = new EventDto();
    model.addAttribute("clubId", clubId);
    model.addAttribute("event", event);
    return "events-create";
  }

  @PostMapping("/events/{clubId}/new")
  public String save(@PathVariable long clubId,@Valid @ModelAttribute("event") EventDto eventDto,BindingResult result,Model model) {
    if (result.hasErrors()) {
      model.addAttribute("clubId", clubId);
      model.addAttribute("event", eventDto);
      return "events-create";
    }
    eventService.createEvent(clubId, eventDto);
    return "redirect:/clubs/" + clubId;
  }

  @GetMapping("/events/{eventId}/delete")
  public String delete(@PathVariable long eventId) {
    eventService.delete(eventId);
    return "redirect:/events";
  }

}
