package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewCategoryDto {
    @NotNull
    private String name;
}
