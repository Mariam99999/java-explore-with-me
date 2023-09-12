package com.example.statservicedto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StatDtoCreate {
    private String app;
    private String uri;
    private String ip;
    private String timestamp;
}
