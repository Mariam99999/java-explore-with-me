package com.example.mainservice.mapper;

import com.example.mainservice.enums.StatEnum;
import com.example.mainservice.model.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static com.example.mainservice.utils.EventUtils.DATE_TME_FORMATTER;

@AllArgsConstructor
@Component
public class EventMapper {
    private final UserMapper userMapper;

    public EventFullDto mapToDto(Event event, Long requests, Long views) {
        return EventFullDto.builder()
                .id(event.getId())
                .title(event.getTitle())
                .location(event.getLocation())
                .annotation(event.getAnnotation())
                .category(event.getCategory())
                .eventDate(event.getEventDate().format(DATE_TME_FORMATTER))
                .confirmedRequests(requests)
                .createdOn(event.getCreatedOn().format(DATE_TME_FORMATTER))
                .description(event.getDescription())
                .initiator(userMapper.mapToShortDto(event.getInitiator()))
                .paid(event.getPaid())
                .participantLimit(event.getParticipantLimit())
                .publishedOn(event.getPublishedOn().format(DATE_TME_FORMATTER))
                .requestModeration(event.getRequestModeration())
                .state(event.getState())
                .views(views)
                .build();
    }

    public Event mapToEvent(User user, Category category, LocalDateTime createdOn, NewEventDto newEventDto) {
        Event event = Event.builder()
                .id(null)
                .title(newEventDto.getTitle())
                .location(newEventDto.getLocation())
                .annotation(newEventDto.getAnnotation())
                .createdOn(createdOn)
                .category(category)
                .eventDate(LocalDateTime.parse(newEventDto.getEventDate(), DATE_TME_FORMATTER))
                .confirmedRequests(0L)
                .description(newEventDto.getDescription())
                .initiator(user)
                .paid(newEventDto.getPaid())
                .participantLimit(newEventDto.getParticipantLimit())
                .publishedOn(LocalDateTime.now())
                .requestModeration(newEventDto.getRequestModeration())
                .state(StatEnum.PENDING)
                .views(0L)
                .build();
        return event;
    }

    public EventShortDto mapToShortDto(EventFullDto event) {
        return new EventShortDto(event.getId(), event.getAnnotation(), event.getCategory(),
                event.getConfirmedRequests(), event.getEventDate(), event.getInitiator(),
                event.getPaid(), event.getTitle(), event.getParticipantLimit());
    }
}
