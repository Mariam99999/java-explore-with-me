package com.example.mainservice.service;

import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.ParticipationRequestMapper;
import com.example.mainservice.model.Event;
import com.example.mainservice.model.ParticipationRequestDto;
import com.example.mainservice.model.User;
import com.example.mainservice.storage.EventRepository;
import com.example.mainservice.storage.ParticipationRequestRepository;
import com.example.mainservice.storage.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class ParticipationRequestService {
    private final UserRepository userRepository;
    private final EventRepository eventRepository;
    private final ParticipationRequestRepository participationRequestRepository;
    private final ParticipationRequestMapper participationRequestMapper;

    public ParticipationRequestDto addParticipationRequest(Long userId, Long eventId) {
        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Event event = eventRepository.findById(eventId).orElseThrow(NotFoundException::new);
        return participationRequestMapper.mapToParticipationRequestDto(participationRequestRepository
                .save(participationRequestMapper.mapToParticipationRequest(user, event)));

    }
}
