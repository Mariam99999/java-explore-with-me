package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class UpdateCompilationRequest {
    private List<Long> events;
    private Boolean pinned;
    private String title;
}
