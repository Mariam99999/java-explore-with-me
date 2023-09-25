package com.example.mainservice.controller;

import com.example.mainservice.model.NewUserRequest;
import com.example.mainservice.model.ParticipationRequestDto;
import com.example.mainservice.model.UserDto;
import com.example.mainservice.service.ParticipationRequestService;
import com.example.mainservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/users/{userId}/requests")
public class ParticipationRequestController {
    private final ParticipationRequestService participationRequestService;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    ParticipationRequestDto addParticipationRequest(@PathVariable Long userId,@RequestParam Long eventId){
        return participationRequestService.addParticipationRequest(userId,eventId);
    }

}
