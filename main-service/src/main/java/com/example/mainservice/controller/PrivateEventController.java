package com.example.mainservice.controller;

import com.example.mainservice.model.EventFullDto;
import com.example.mainservice.model.NewEventDto;
import com.example.mainservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @RequestBody
                                 @Valid NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }
}
