package com.example.mainservice.service;

import com.example.mainservice.enums.RequestStatus;
import com.example.mainservice.enums.StatEnum;
import com.example.mainservice.exception.ConflictException;
import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.ParticipationRequestMapper;
import com.example.mainservice.model.*;
import com.example.mainservice.storage.EventRepository;
import com.example.mainservice.storage.ParticipationRequestRepository;
import com.example.mainservice.storage.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final ParticipationRequestMapper participationRequestMapper;

    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        Event event = checkAndReturnEvent(userId, eventId);
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        return participationRequestMapper.mapToParticipationRequestDto(participationRequestRepository
                .save(participationRequestMapper.mapToParticipationRequest(user, event)));
    }


    public List<ParticipationRequestDto> getParticipationRequestByEventIdAndInitiatorId(Long eventId, Long userId) {
        return participationRequestRepository.findByEventIdAndEventInitiatorId(eventId, userId).stream()
                .map(participationRequestMapper::mapToParticipationRequestDto).collect(Collectors.toList());
    }

    public EventRequestStatusUpdateResult updateStatus(Long userId, Long eventId,
                                                       EventRequestStatusUpdateRequest e) {
        checkRequests(userId, eventId, e);
        List<ParticipationRequest> p = participationRequestRepository.findAllById(e.getRequestIds());
        participationRequestRepository.saveAll(p.stream().peek(pa -> pa.setStatus(e.getStatus()))
                .collect(Collectors.toList()));
        p = participationRequestRepository.findByEventIdAndStatusIn(eventId,
                List.of(RequestStatus.CONFIRMED, RequestStatus.REJECTED));
        return new EventRequestStatusUpdateResult(
                p.stream()
                        .filter(pa -> pa.getStatus() == RequestStatus.CONFIRMED)
                        .map(participationRequestMapper::mapToParticipationRequestDto)
                        .collect(Collectors.toList()),
                p.stream()
                        .filter(pa -> pa.getStatus() == RequestStatus.REJECTED)
                        .map(participationRequestMapper::mapToParticipationRequestDto)
                        .collect(Collectors.toList()));
    }

    public List<ParticipationRequestDto> getRequestByRequesterId(Long userId) {
        return participationRequestRepository.findByRequesterId(userId)
                .stream()
                .map(participationRequestMapper::mapToParticipationRequestDto)
                .collect(Collectors.toList());
    }

    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = participationRequestRepository.findById(requestId).orElseThrow(NotFoundException::new);
        request.setStatus(RequestStatus.CANCELED);
        return participationRequestMapper.mapToParticipationRequestDto(participationRequestRepository.save(request));

    }

    private Event checkAndReturnEvent(Long userId, Long eventId) {
        if (!participationRequestRepository.findByEventIdAndRequesterId(eventId, userId).isEmpty())
            throw new ConflictException();
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        if (event.getInitiator().getId().equals(userId) || event.getState() != StatEnum.PUBLISHED)
            throw new ConflictException();
        List<ParticipationRequest> requests = participationRequestRepository.findByEventIdInAndStatus(List.of(eventId), RequestStatus.CONFIRMED);
        if (event.getParticipantLimit() > 0 && requests.size() >= event.getParticipantLimit())
            throw new ConflictException();
        return event;
    }

    private void checkRequests(Long userId, Long eventId, EventRequestStatusUpdateRequest e) {
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        if (!event.getInitiator().getId().equals(userId)) throw new ConflictException();
        int confirmedRequestsSize = participationRequestRepository.findByEventIdInAndStatus(List.of(eventId), RequestStatus.CONFIRMED).size();
        if (event.getParticipantLimit() > 0 && confirmedRequestsSize + e.getRequestIds().size() > event.getParticipantLimit())
            throw new ConflictException();
        List<ParticipationRequest> requests = participationRequestRepository.findAllById(e.getRequestIds());
        if (requests.stream().anyMatch(p -> p.getStatus() != RequestStatus.PENDING)) throw new ConflictException();
    }

}
