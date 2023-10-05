package com.example.mainservice.service;

import com.example.mainservice.enums.RequestStatus;
import com.example.mainservice.enums.StatEnum;
import com.example.mainservice.exception.BadRequestException;
import com.example.mainservice.exception.ConflictException;
import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.EventMapper;
import com.example.mainservice.model.*;
import com.example.mainservice.storage.CategoryRepository;
import com.example.mainservice.storage.EventRepository;
import com.example.mainservice.storage.ParticipationRequestRepository;
import com.example.mainservice.storage.UserRepository;
import com.example.mainservice.utils.EventUtils;
import com.example.statserviceclient.client.StatClient;
import com.example.statservicedto.StatDtoCreate;
import com.example.statservicedto.StatDtoGet;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.example.mainservice.utils.EventUtils.DATE_TME_FORMATTER;

@Service
@RequiredArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;
    private final StatClient statClient;
    private final ParticipationRequestRepository requestRepository;
    private final ObjectMapper objectMapper;


    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        if (!checkUpdatedData(newEventDto.getEventDate(), false)) throw new BadRequestException();
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(NotFoundException::new);
        Event event = eventRepository.save(eventMapper.mapToEvent(user, category, LocalDateTime.now(), newEventDto));
        return eventMapper.mapToDto(event, 0L, 0L);
    }

    public List<EventFullDto> getEventByInitiatorId(Long id, int from, int size) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        List<Event> events = eventRepository.findByInitiatorId(id, pageable);
        return getEventsDto(events);
    }

    public EventFullDto getEventByIdAndInitiatorId(Long evenId, Long userId) throws JsonProcessingException {
        Event event = eventRepository.findByIdAndInitiatorId(evenId, userId).orElseThrow(NotFoundException::new);
        return getEventDto(event);
    }

    public EventFullDto updateEventByIdAndInitiatorId(Long evenId, Long userId, UpdateEventRequest updateEventRequest) {
        if (!checkUpdatedData(updateEventRequest.getEventDate(), false)) throw new BadRequestException();
        Event event = eventRepository.findByIdAndInitiatorId(evenId, userId).orElseThrow(NotFoundException::new);
        if (event.getState() == StatEnum.PUBLISHED)
            throw new ConflictException();
        return updateEvent(event, updateEventRequest);
    }

    public EventFullDto updateEventById(Long eventId, UpdateEventRequest updateEventRequest) {
        if (!checkUpdatedData(updateEventRequest.getEventDate(), true)) throw new BadRequestException();
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        if (updateEventRequest.getStateAction() != null && event.getState() == StatEnum.PUBLISHED)
            throw new ConflictException();
        return updateEvent(event, updateEventRequest);
    }

    public List<EventFullDto> getEventsByAmin(List<Long> users, List<String> states,
                                              List<Long> categories, String rangeStart,
                                              String rangeEnd, int from, int size) throws JsonProcessingException {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        List<StatEnum> statEnums = null;
        if (states != null && !states.isEmpty())
            statEnums = states.stream().map(StatEnum::valueOf).collect(Collectors.toList());
        List<Event> events = eventRepository.findByAdmin(users, statEnums, categories,
                rangeStart == null ? LocalDateTime.now().minusYears(100) :
                        LocalDateTime.parse(rangeStart, DATE_TME_FORMATTER),
                rangeEnd == null ? LocalDateTime.now().plusYears(100) :
                        LocalDateTime.parse(rangeEnd, DATE_TME_FORMATTER), pageable);
        return getEventsDto(events);

    }

    public List<EventFullDto> getEventsByFilter(String text, List<Long> categories, Boolean paid, String rangeStart,
                                                String rangeEnd, Boolean onlyAvailable, String sort, int from, int size,
                                                HttpServletRequest httpServletRequest) throws JsonProcessingException {
        saveState(httpServletRequest);
        Pageable pageable = PageRequest.of(from / size, size, Sort.by(sort == null || sort.equals("EVENT_DATE") ? "createdOn" : "views").descending());
        List<Event> events = eventRepository.findByFilters(text, categories, paid,
                rangeStart == null ? LocalDateTime.now().plusSeconds(1) :
                        LocalDateTime.parse(rangeStart, DATE_TME_FORMATTER),
                rangeEnd == null ? LocalDateTime.now().plusYears(100) :
                        LocalDateTime.parse(rangeEnd, DATE_TME_FORMATTER), onlyAvailable, pageable);
        if (events.isEmpty()) throw new BadRequestException();

        return getEventsDto(events);
    }

    public EventFullDto getEventById(Long eventId, HttpServletRequest httpServletRequest) throws JsonProcessingException {
        saveState(httpServletRequest);
        ResponseEntity<Object> stats = statClient.getStats("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                Map.of("start", LocalDateTime.now().minusYears(100).format(DATE_TME_FORMATTER),
                        "end", LocalDateTime.now().plusYears(100).format(DATE_TME_FORMATTER),
                        "uris", "events/" + eventId,
                        "unique", "true"));
        Event event = eventRepository.findByIdAndState(eventId, StatEnum.PUBLISHED).orElseThrow(NotFoundException::new);
        return getEventDto(event);
    }

    private void saveState(HttpServletRequest httpServletRequest) {
        statClient.saveStat("/hit", new StatDtoCreate("main-service", httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr(), LocalDateTime.now()));
    }

    private List<ParticipationRequest> getRequests(List<Event> events) {
        return requestRepository.findByEventIdInAndStatus(events.stream().map(Event::getId).collect(Collectors.toList()), RequestStatus.CONFIRMED);
    }

    private List<StatDtoGet> getStats(List<Event> events) throws JsonProcessingException {
        ResponseEntity<Object> stats = statClient.getStats("/stats?start={start}&end={end}&uris={uris}&unique={unique}",
                Map.of("start", LocalDateTime.now().minusYears(100).format(DATE_TME_FORMATTER),
                        "end", LocalDateTime.now().plusYears(100).format(DATE_TME_FORMATTER),
                        "uris", events.stream().map(event -> "/events/" + event.getId()).collect(Collectors.joining(",")),
                        "unique", "true"));
        String bodyString = objectMapper.writeValueAsString(stats.getBody());

        return Arrays.asList(objectMapper.readValue(bodyString, StatDtoGet[].class));
    }

    private Map<String, Long> getHitsMap(List<Event> events) throws JsonProcessingException {
        List<StatDtoGet> stats = getStats(events);
        return stats.stream().collect(Collectors.toMap(s -> s.getUri().replace("/events/", ""), StatDtoGet::getHits));
    }

    private Long getRequestCount(List<ParticipationRequest> requests, Long id) {
        return requests.stream().filter(r -> r.getEvent().getId().equals(id)).count();
    }

    public List<EventFullDto> getEventsDto(List<Event> events) throws JsonProcessingException {
        List<ParticipationRequest> requests = getRequests(events);
        Map<String, Long> hits = getHitsMap(events);
        return events.stream().map(e -> eventMapper.mapToDto(e, getRequestCount(requests, e.getId()), hits.get(e.getId().toString()))).collect(Collectors.toList());
    }

    private EventFullDto getEventDto(Event event) throws JsonProcessingException {
        Long requests = (long) getRequests(List.of(event)).size();
        List<StatDtoGet> stats = getStats(List.of(event));
        Long views = stats.isEmpty() ? 0L : stats.get(0).getHits();
        return eventMapper.mapToDto(event, requests, views);
    }

    private EventFullDto updateEvent(Event event, UpdateEventRequest updateEventUserRequest) {
        Category category = null;
        if (updateEventUserRequest.getCategory() != null) {
            category = categoryRepository.findById(updateEventUserRequest.getCategory()).orElseThrow(NotFoundException::new);
        }
        return eventMapper.mapToDto(eventRepository.save(EventUtils.update(event, updateEventUserRequest, category)),
                event.getConfirmedRequests(), event.getViews());

    }

    private Boolean checkUpdatedData(String data, Boolean isAdmin) {
        if (data == null) return true;
        return (LocalDateTime.parse(data, DATE_TME_FORMATTER)).isAfter(LocalDateTime.now().plusHours(isAdmin ? 1 : 2));
    }
}
