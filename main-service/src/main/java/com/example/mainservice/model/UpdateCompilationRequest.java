package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    @Length(min = 1, max = 50)
    private String title;
}
