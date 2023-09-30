package com.example.mainservice.storage;

import com.example.mainservice.enums.StatEnum;
import com.example.mainservice.model.Event;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findByInitiatorId(Long id, Pageable pageable);

    Optional<Event> findByIdAndInitiatorId(Long eventId, Long userId);

    @Query("select e from Event as e " +
            "where (:users is null or e.initiator.id in :users) " +
            "AND (:states is null or e.state in :states) " +
            "AND (:categories is null or e.category.id in :categories) " +
            "AND  e.createdOn BETWEEN :rangeStart and :rangeEnd")
    List<Event> findByAdmin(@Param("users") List<Long> users, @Param("states") List<StatEnum> states, List<Long> categories,
                            LocalDateTime rangeStart, LocalDateTime rangeEnd, Pageable pageable);

    @Query("select e from Event as e " +
            "where (:text is null or e.annotation like lower(concat('%', :text,'%')) " +
            "OR e.description like lower(concat('%', :text,'%'))) " +
            "AND (:categories is null or e.category.id in :categories) " +
            "AND (:paid is null or e.paid = :paid) " +
            "AND  e.createdOn BETWEEN :rangeStart and :rangeEnd " +
            "AND (:onlyAvailable is false or e.id in  (select p.event.id from ParticipationRequest as p" +
            " where p.event.id = e.id group by p.event.id " +
            "having (count (p.event.id) < e.participantLimit )))")
    List<Event> findByFilters(String text, List<Long> categories, Boolean paid,
                              LocalDateTime rangeStart, LocalDateTime rangeEnd, Boolean onlyAvailable, Pageable pageable);

}
