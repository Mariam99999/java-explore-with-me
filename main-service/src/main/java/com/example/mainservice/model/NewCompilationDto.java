package com.example.mainservice.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class NewCompilationDto {
    private List<Long> events;
    private Boolean pinned = false;
    @NotBlank
    @Length(min = 1, max = 50)
    private String title;
}
