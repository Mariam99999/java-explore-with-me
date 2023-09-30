package com.example.mainservice.mapper;

import com.example.mainservice.enums.RequestStatus;
import com.example.mainservice.enums.StatEnum;
import com.example.mainservice.model.Event;
import com.example.mainservice.model.ParticipationRequest;
import com.example.mainservice.model.ParticipationRequestDto;
import com.example.mainservice.model.User;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Component
public class ParticipationRequestMapper {
    public ParticipationRequest mapToParticipationRequest (User user, Event event){
        return new ParticipationRequest(null, LocalDateTime.now(),event,user, RequestStatus.PENDING);
    }
    public  ParticipationRequestDto mapToParticipationRequestDto (ParticipationRequest participationRequest){
        return new ParticipationRequestDto(participationRequest.getId(),
                participationRequest.getCreated().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                participationRequest.getEvent().getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus().toString());
    }
}
