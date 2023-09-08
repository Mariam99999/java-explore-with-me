package com.example.statservicedto.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@AllArgsConstructor
@Data
public class StatDtoCreate {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
