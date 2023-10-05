package com.example.mainservice.controller;

import com.example.mainservice.model.EventFullDto;
import com.example.mainservice.model.UpdateEventRequest;
import com.example.mainservice.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @RequestBody @Valid UpdateEventRequest UpdateEventRequest) {
        return eventService.updateEventById(eventId, UpdateEventRequest);
    }

    @GetMapping
    public List<EventFullDto> getEventsByAmin(@RequestParam(required = false) List<Long> users,
                                              @RequestParam(required = false) List<String> states,
                                              @RequestParam(required = false) List<Long> categories,
                                              @RequestParam(required = false) String rangeStart,
                                              @RequestParam(required = false) String rangeEnd,
                                              @RequestParam(defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        return eventService.getEventsByAmin(users, states, categories, rangeStart, rangeEnd, from, size);
    }
}
