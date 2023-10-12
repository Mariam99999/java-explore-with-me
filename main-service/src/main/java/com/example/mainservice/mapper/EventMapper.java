package com.example.mainservice.mapper;

import com.example.mainservice.enums.StatEnum;
import com.example.mainservice.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@AllArgsConstructor
@Component
public class EventMapper {
    private final UserMapper userMapper;
    private final LocationMapper locationMapper;


    public EventFullDto mapToDto(Event event, Long requests, Long views, List<CommentDto> comments) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .location(locationMapper.mapToDto(event.getLocation()))
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .eventDate(event.getEventDate())
                .createdOn(event.getCreatedOn())
                .description(event.getDescription())
                .initiator(userMapper.mapToShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn())
                .requestModeration(event.getRequestModeration())
                .confirmedRequests(requests == null ? 0 : requests)
                .state(event.getState())
                .views(views)
                .comments(comments)
                .build();
    }

    public Event mapToEvent(User user, Category category, LocalDateTime createdOn, NewEventDto newEventDto) {
        return Event.builder()
                .id(null)
                .title(newEventDto.getTitle())
                .location(locationMapper.mapFromDto(newEventDto.getLocation()))
                .annotation(newEventDto.getAnnotation())
                .createdOn(createdOn)
                .category(category)
                .eventDate(newEventDto.getEventDate())
                .description(newEventDto.getDescription())
                .initiator(user)
                .paid(newEventDto.isPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(newEventDto.isRequestModeration())
                .state(StatEnum.PENDING)
                .build();
    }

    public EventShortDto mapToShortDto(EventFullDto event) {
        return new EventShortDto(event.getId(), event.getAnnotation(), event.getCategory(),
                event.getConfirmedRequests(), event.getEventDate(), event.getInitiator(),
                event.getPaid(), event.getTitle(), event.getParticipantLimit());
    }

    public EventShortDto mapToShortDtoFromEvent(Event event, Long requestSize, Integer views) {
        return EventShortDto.builder()
                .id(event.getId())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .confirmedRequests(requestSize)
                .eventDate(event.getEventDate())
                .initiator(userMapper.mapToShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .title(event.getTitle())
                .build();
    }
}
