package com.example.mainservice.controller;

import com.example.mainservice.model.EventFullDto;
import com.example.mainservice.model.NewEventDto;
import com.example.mainservice.model.UpdateEventAdminRequest;
import com.example.mainservice.model.UpdateEventUserRequest;
import com.example.mainservice.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/events")
public class AdminEventController {
    private final EventService eventService;

    @PatchMapping("/{eventId}")
     public EventFullDto updateEvent (@PathVariable Long eventId,
                                      @RequestBody UpdateEventAdminRequest updateEventAdminRequest){
        return eventService.updateEventById(eventId,updateEventAdminRequest);
    }
    @GetMapping
    public List<EventFullDto> getEventsByAmin(@RequestParam(required = false) List<Long> users,
                                              @RequestParam(required = false) List<String> states,
                                              @RequestParam(required = false) List<Long> categories,
                                              @RequestParam(required = false) String rangeStart,
                                              @RequestParam(required = false)String rangeEnd,
                                              @RequestParam (defaultValue = "0") int from,
                                              @RequestParam(defaultValue = "10") int size){
        return eventService.getEventsByAmin(users,states,categories,rangeStart,rangeEnd,from,size);
    }
}
