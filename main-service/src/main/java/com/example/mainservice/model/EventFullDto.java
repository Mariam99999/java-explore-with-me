package com.example.mainservice.model;


import com.example.mainservice.enums.StatEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class EventFullDto {
    private Long id;
    private String annotation;
    private Category category;
    private Long confirmedRequests;
    private String createdOn;
    private String description;
    private String eventDate;
    private UserShortDto initiator;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private String publishedOn;
    private Boolean requestModeration;
    private StatEnum state;
    private String title;
    private Long views;
}
