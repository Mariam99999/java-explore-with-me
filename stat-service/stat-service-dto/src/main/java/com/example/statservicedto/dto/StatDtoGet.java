package com.example.statservicedto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StatDtoGet {
    private String app;
    private String uri;
    private Long hits;
}
