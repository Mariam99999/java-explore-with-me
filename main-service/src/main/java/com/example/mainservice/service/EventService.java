package com.example.mainservice.service;

import com.example.mainservice.enums.StatEnum;
import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.EventMapper;
import com.example.mainservice.model.*;
import com.example.mainservice.storage.CategoryRepository;
import com.example.mainservice.storage.EventRepository;
import com.example.mainservice.storage.UserRepository;
import com.example.mainservice.utils.EventUtils;
import com.example.statserviceclient.client.StatClient;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.mainservice.utils.EventUtils.DATE_TME_FORMATTER;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final StatClient statClient;


    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        DateTimeFormatter DATE_TME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String s = LocalDateTime.now().format(DATE_TME_FORMATTER);
        LocalDateTime lf = LocalDateTime.parse(s, DATE_TME_FORMATTER);
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(NotFoundException::new);
        Event event = eventRepository.save(eventMapper.mapToEvent(user, category, lf, newEventDto));
        return eventMapper.mapToDto(event);
    }

    public List<EventFullDto> getEventByInitiatorId(Long id, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        return eventRepository.findByInitiatorId(id, pageable)
                .stream().map(eventMapper::mapToDto)
                .collect(Collectors.toList());
    }

    public EventFullDto getEventByIdAndInitiatorId(Long evenId, Long userId) {
        return eventMapper.mapToDto(eventRepository.findByIdAndInitiatorId(evenId, userId).orElseThrow(NotFoundException::new));
    }

    public EventFullDto updateEventByIdAndInitiatorId(Long evenId, Long userId, UpdateEventUserRequest updateEventUserRequest) {
        Event event = eventRepository.findByIdAndInitiatorId(evenId, userId).orElseThrow(NotFoundException::new);
        return updateEvent(event, updateEventUserRequest);
    }

    public EventFullDto updateEventById(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {

        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        return updateEvent(event, updateEventAdminRequest);
    }

    public List<EventFullDto> getEventsByAmin(List<Long> users, List<String> states, List<Long> categories,
                                              String rangeStart, String rangeEnd, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        List<StatEnum> statEnums = states.stream().map(StatEnum::valueOf)
                .collect(Collectors.toList());
        return eventRepository.findByAdmin(users, statEnums, categories,
                        rangeStart == null ? LocalDateTime.now().minusYears(100) :
                                LocalDateTime.parse(rangeStart, DATE_TME_FORMATTER),
                        rangeEnd == null ? LocalDateTime.now().plusYears(100) :
                                LocalDateTime.parse(rangeEnd, DATE_TME_FORMATTER), pageable)
                .stream().map(eventMapper::mapToDto).collect(Collectors.toList());

    }

    private EventFullDto updateEvent(Event event, UpdateEventUserRequest updateEventUserRequest) {
        Category category = null;
        if (updateEventUserRequest.getCategory() != null) {
            category = categoryRepository.findById(updateEventUserRequest.getCategory()).orElseThrow(NotFoundException::new);
        }
        return eventMapper.mapToDto(eventRepository.save(EventUtils.update(event, updateEventUserRequest, category)));
    }

    public List<EventFullDto> getEventsByFilter(String text, List<Long> categories,
                                                Boolean paid, String rangeStart,
                                                String rangeEnd, Boolean onlyAvailable,
                                                String sort, int from, int size) {

        Pageable pageable = PageRequest.of(from / size, size,
                Sort.by(sort == null || sort.equals("EVENT_DATE") ? "createdOn" : "views").descending());
        return eventRepository.findByFilters(text, categories, paid,
                        rangeStart == null ? LocalDateTime.now().plusSeconds(1) :
                                LocalDateTime.parse(rangeStart, DATE_TME_FORMATTER),
                        rangeEnd == null ? LocalDateTime.now().plusYears(100) :
                                LocalDateTime.parse(rangeEnd, DATE_TME_FORMATTER), onlyAvailable, pageable).stream()
                .map(eventMapper::mapToDto).collect(Collectors.toList());
    }

    public EventFullDto getEventById(Long eventId) {

        return eventMapper.mapToDto(eventRepository.findById(eventId).orElseThrow(NotFoundException::new));
    }
}
