package com.example.mainservice.mapper;

import com.example.mainservice.model.*;
import com.example.mainservice.enums.StatEnum;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@AllArgsConstructor
@Component
public class EventMapper {

    private final UserMapper userMapper;
    public EventFullDto mapToDto(Event event){
       return  EventFullDto.builder()
               .id(event.getId())
               .title(event.getTitle())
               .location(event.getLocation())
               .annotation(event.getAnnotation())
               .category(event.getCategory())
               .eventDate(event.getEventDate())
               .confirmedRequests(event.getConfirmedRequests())
               .createdOn(event.getCreatedOn())
               .description(event.getDescription())
               .initiator(userMapper.mapToShortDto(event.getInitiator()))
               .paid(event.getPaid())
               .participantLimit(event.getParticipantLimit())
               .publishedOn(event.getPublishedOn())
               .requestModeration(event.getRequestModeration())
               .state(event.getState())
               .views(event.getViews())
               .build();
    }
    public Event mapToEvent(User user,Category category, NewEventDto newEventDto){
        return Event.builder()
                .id(null)
                .title(newEventDto.getTitle())
                .location(newEventDto.getLocation())
                .annotation(newEventDto.getAnnotation())
                .category(category)
                .eventDate(newEventDto.getEventDate())
                .confirmedRequests(0L)
                .description(newEventDto.getDescription())
                .initiator(user)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(newEventDto.getRequestModeration())
                .state(StatEnum.PENDING)
                .views(0)
                .build();

    }
}
