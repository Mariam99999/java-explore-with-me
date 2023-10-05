package com.example.mainservice.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewEventDto {
    @NotBlank
    @Length(min = 20, max = 2000)
    private String annotation;
    @NotNull
    private Long category;
    @NotBlank
    @Length(min = 20, max = 7000)
    private String description;
    @NotNull
    private String eventDate;
    @NotNull
    private Location location;
    private Boolean paid = false;
    private Integer participantLimit = 0;
    private Boolean requestModeration = true;
    @NotBlank
    @Length(min = 3, max = 120)
    private String title;
}
