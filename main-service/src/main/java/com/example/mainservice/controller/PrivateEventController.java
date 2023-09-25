package com.example.mainservice.controller;

import com.example.mainservice.model.EventFullDto;
import com.example.mainservice.model.NewEventDto;
import com.example.mainservice.model.UpdateEventUserRequest;
import com.example.mainservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.PortUnreachableException;
import java.util.List;

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
    @GetMapping
    public List<EventFullDto>getEventsByInitiatorId(@PathVariable Long userId,
                                                    @RequestParam(defaultValue = "0") int from,
                                                    @RequestParam(defaultValue = "10") int size){
        return eventService.getEventByInitiatorId(userId, from, size);
    }
    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdAndInitiatorId(@PathVariable Long eventId,@PathVariable Long userId){
        return eventService.getEventByIdAndInitiatorId(eventId,userId);
    }
    @PatchMapping("/{eventId}")
     public EventFullDto updateEvent (@PathVariable Long eventId,
                                      @PathVariable Long userId,
                                      @RequestBody UpdateEventUserRequest updateEventUserRequest){
        return eventService.updateEventByIdAndInitiatorId(eventId,userId,updateEventUserRequest);
    }

}
