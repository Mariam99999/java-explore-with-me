package com.example.mainservice.model;


import com.example.mainservice.enums.StatEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.awt.print.Pageable;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventFullDto {
    private Long id;
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    private LocalDateTime createdOn;
    private String description;
    private LocalDateTime eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private LocalDateTime publishedOn;
    private Boolean requestModeration;
    private StatEnum state;
    private String title;
    private Integer views;

}
