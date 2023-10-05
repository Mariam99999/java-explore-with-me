package com.example.mainservice.controller;

import com.example.mainservice.model.*;
import com.example.mainservice.service.EventService;
import com.example.mainservice.service.ParticipationRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final EventService eventService;
    private final ParticipationRequestService participationRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventFullDto addEvent(@PathVariable Long userId,
                                 @RequestBody
                                 @Valid NewEventDto newEventDto) {
        return eventService.addEvent(userId, newEventDto);
    }

    @GetMapping
    public List<EventFullDto> getEventsByInitiatorId(@PathVariable Long userId,
                                                     @RequestParam(defaultValue = "0") int from,
                                                     @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        return eventService.getEventByInitiatorId(userId, from, size);
    }

    @GetMapping("/{eventId}")
    public EventFullDto getEventByIdAndInitiatorId(@PathVariable Long eventId, @PathVariable Long userId) throws JsonProcessingException {
        return eventService.getEventByIdAndInitiatorId(eventId, userId);
    }

    @PatchMapping("/{eventId}")
    public EventFullDto updateEvent(@PathVariable Long eventId,
                                    @PathVariable Long userId,
                                    @RequestBody @Valid UpdateEventRequest updateEventRequest) {
        return eventService.updateEventByIdAndInitiatorId(eventId, userId, updateEventRequest);
    }

    @GetMapping("/{eventId}/requests")
    public List<ParticipationRequestDto> getParticipationRequestByEventIdAndInitiatorId(@PathVariable Long eventId,
                                                                                        @PathVariable Long userId) {
        return participationRequestService.getParticipationRequestByEventIdAndInitiatorId(eventId, userId);
    }

    @PatchMapping("/{eventId}/requests")
    public EventRequestStatusUpdateResult updateStatus(@PathVariable Long userId, @PathVariable Long eventId,
                                                       @RequestBody @Valid EventRequestStatusUpdateRequest e) {
        return participationRequestService.updateStatus(userId, eventId, e);
    }


}
