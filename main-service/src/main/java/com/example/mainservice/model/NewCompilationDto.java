package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewCompilationDto {
    @NotNull
    private List<Long> events;
    @NotNull
    private Boolean pinned;
    @NotBlank
    private String title;
}
