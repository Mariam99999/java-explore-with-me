package com.example.mainservice.service;

import com.example.mainservice.enums.RequestStatus;
import com.example.mainservice.enums.StatEnum;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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


    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {
        DateTimeFormatter DATE_TME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String s = LocalDateTime.now().format(DATE_TME_FORMATTER);
        LocalDateTime lf = LocalDateTime.parse(s, DATE_TME_FORMATTER);
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Category category = categoryRepository.findById(newEventDto.getCategory()).orElseThrow(NotFoundException::new);
        Event event = eventRepository.save(eventMapper.mapToEvent(user, category, lf, newEventDto));
        return eventMapper.mapToDto(event, 0L, 0);
    }

    public List<EventFullDto> getEventByInitiatorId(Long id, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size, Sort.by("createdOn").descending());
        List<Event> events = eventRepository.findByInitiatorId(id, pageable);
        List<ParticipationRequest> sumRequest = requestRepository.findByEventIdInAndStatus(events.stream()
                        .map(Event::getId)
                        .collect(Collectors.toList()), RequestStatus.CONFIRMED);
        Map<String, Object> map = Map.of(
                "start",LocalDateTime.now().minusYears(100),
                "end",LocalDateTime.now().plusYears(100),
                "uris",events.stream().map(event -> "/events/" + event.getId()).collect(Collectors.toList()),
                "unique",false
        );
         ResponseEntity<Object> stats = statClient.getStats("/stats", Map.of(
                "start",LocalDateTime.now().minusYears(100).format(DATE_TME_FORMATTER),
                "end",LocalDateTime.now().plusYears(100).format(DATE_TME_FORMATTER),
                "uris", events.stream().map(event -> "/events/" + event.getId()).collect(Collectors.joining(",")),
                "unique","false"
        ));
        return null;
    }

    public EventFullDto getEventByIdAndInitiatorId(Long evenId, Long userId) {
//        return eventMapper.mapToDto(eventRepository.findByIdAndInitiatorId(evenId, userId).orElseThrow(NotFoundException::new));
        return null;
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
//        return eventRepository.findByAdmin(users, statEnums, categories,
//                        rangeStart == null ? LocalDateTime.now().minusYears(100) :
//                                LocalDateTime.parse(rangeStart, DATE_TME_FORMATTER),
//                        rangeEnd == null ? LocalDateTime.now().plusYears(100) :
//                                LocalDateTime.parse(rangeEnd, DATE_TME_FORMATTER), pageable)
//                .stream().map(eventMapper::mapToDto).collect(Collectors.toList());
        return null;

    }

    private EventFullDto updateEvent(Event event, UpdateEventUserRequest updateEventUserRequest) {
        Category category = null;
        if (updateEventUserRequest.getCategory() != null) {
            category = categoryRepository.findById(updateEventUserRequest.getCategory()).orElseThrow(NotFoundException::new);
        }
//        return eventMapper.mapToDto(eventRepository.save(EventUtils.update(event, updateEventUserRequest, category)));
        return null;
    }

    public List<EventFullDto> getEventsByFilter(String text, List<Long> categories,
                                                Boolean paid, String rangeStart,
                                                String rangeEnd, Boolean onlyAvailable,
                                                String sort, int from, int size,
                                                HttpServletRequest httpServletRequest) {
        saveState(httpServletRequest);
        Pageable pageable = PageRequest.of(from / size, size,
                Sort.by(sort == null || sort.equals("EVENT_DATE") ? "createdOn" : "views").descending());
//        return eventRepository.findByFilters(text, categories, paid,
//                        rangeStart == null ? LocalDateTime.now().plusSeconds(1) :
//                                LocalDateTime.parse(rangeStart, DATE_TME_FORMATTER),
//                        rangeEnd == null ? LocalDateTime.now().plusYears(100) :
//                                LocalDateTime.parse(rangeEnd, DATE_TME_FORMATTER), onlyAvailable, pageable).stream()
//                .map(eventMapper::mapToDto).collect(Collectors.toList());
        return null;
    }

    public EventFullDto getEventById(Long eventId, HttpServletRequest httpServletRequest) {
        saveState(httpServletRequest);
//        return eventMapper.mapToDto(eventRepository.findById(eventId).orElseThrow(NotFoundException::new));
        return null;
    }

    private void saveState(HttpServletRequest httpServletRequest) {
        statClient.saveStat("/hit", new StatDtoCreate("main-service",
                httpServletRequest.getRequestURI(), httpServletRequest.getRemoteAddr(), LocalDateTime.now()));
    }
}
