package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Embeddable
public class Location {
    @NotNull
    private Float lat;
    @NotNull
    private Float lon;
}
