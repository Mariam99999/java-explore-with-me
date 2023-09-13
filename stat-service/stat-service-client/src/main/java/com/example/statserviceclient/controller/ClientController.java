package com.example.statserviceclient.controller;

import com.example.statservicedto.StatDtoCreate;
import com.example.statservicedto.StatDtoGet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class ClientController {
    private final StatClient statClient;

    @GetMapping("/stats")
    ResponseEntity<Object> getStat(@RequestParam String start, @RequestParam String end,
                                   @RequestParam(required = false) List<String> uris,
                                   @RequestParam(required = false) Boolean unique) throws UnsupportedEncodingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern
                ("yyyy-MM-dd HH:mm:ss");
        LocalDateTime decodedStart = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8.toString()), formatter);
        LocalDateTime decodedEnd = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8.toString()), formatter);
        Map<String, Object> params = new HashMap<>();
        params.put("start", start);
        params.put("end", end);
        params.put("uris",uris);
        params.put("unique",unique);

        return statClient.getStats("/stats?start={start}&end={end}&uris={uris}&unique={unique}", params);
    }
    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    StatDtoCreate addStat(@RequestBody @Validated StatDtoCreate statDtoCreate) {
        statClient.saveStat("/hit", statDtoCreate);
        return statDtoCreate;
    }
}
