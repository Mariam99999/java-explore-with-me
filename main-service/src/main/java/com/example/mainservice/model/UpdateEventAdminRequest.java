package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateEventAdminRequest {
    private String annotation;

    private Long category;

    private String description;

    private String eventDate;

    private Location location;

    private Boolean paid;

    private Integer participantLimit;

    private Boolean requestModeration;

}
