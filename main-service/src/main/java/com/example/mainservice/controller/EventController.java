package com.example.mainservice.controller;

import com.example.mainservice.model.EventFullDto;
import com.example.mainservice.service.EventService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events")
public class EventController {
    private final EventService eventService;

    @GetMapping("/{eventId}")
    public EventFullDto getEventById(@PathVariable Long eventId, HttpServletRequest httpServletRequest) throws JsonProcessingException {

        return eventService.getEventById(eventId, httpServletRequest);
    }

    @GetMapping
    public List<EventFullDto> getEvents(@RequestParam(required = false) String text,
                                        @RequestParam(required = false) List<Long> categories,
                                        @RequestParam(required = false) Boolean paid,
                                        @RequestParam(required = false) String rangeStart,
                                        @RequestParam(required = false) String rangeEnd,
                                        @RequestParam(defaultValue = "false") Boolean onlyAvailable,
                                        @RequestParam(required = false) String sort,
                                        @RequestParam(defaultValue = "0") int from,
                                        @RequestParam(defaultValue = "10") int size,
                                        HttpServletRequest httpServletRequest) throws JsonProcessingException {
        return eventService.getEventsByFilter(text, categories, paid, rangeStart, rangeEnd, onlyAvailable, sort, from, size, httpServletRequest);
    }
}

