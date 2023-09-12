package com.example.statserviceclient.controller;

import com.example.statserviceclient.client.BaseClient;
import org.springframework.web.client.RestTemplate;

public class StatClient extends BaseClient {
    public StatClient(RestTemplate rest) {
        super(rest);
    }
}
