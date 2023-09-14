package com.example.statserviceclient.client;

import com.example.statservicedto.StatDtoCreate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Map;

public class StatClient extends BaseClient {

    public StatClient(@Value("${stat.server.url}") String serverUrl) {
        super(new RestTemplateBuilder()
                .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl))
                .requestFactory(SimpleClientHttpRequestFactory::new)
                .build());
    }

    public ResponseEntity<Object> getStats(String path, Map<String, Object> parameters) {
        return get(path, parameters);
    }

    public ResponseEntity<Object> saveStat(String path, StatDtoCreate statDtoCreate) {
        return save(path, statDtoCreate);
    }
}
