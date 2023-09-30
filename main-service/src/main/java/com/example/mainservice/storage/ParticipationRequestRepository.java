package com.example.mainservice.storage;

import com.example.mainservice.enums.RequestStatus;
import com.example.mainservice.model.ParticipationRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest,Long> {
    List<ParticipationRequest> findByEventId(Long eventId);
    List<ParticipationRequest> findByRequesterId(Long userId);
    List<ParticipationRequest> findByEventIdAndEventInitiatorId(Long eventId, Long userId);
    List<ParticipationRequest> findByEventIdAndStatusIn(Long eventId, List<RequestStatus> statuses);


}
