package com.example.statserviceclient.controller;

import com.example.statserviceclient.service.StatService;
import com.example.statservicedto.dto.StatDtoCreate;
import com.example.statservicedto.dto.StatDtoGet;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class StatController {
    private final StatService statService;

    @GetMapping("/stats")
    List<StatDtoGet> getStat(@RequestParam String start, @RequestParam String end,
                             @RequestParam(required = false) List<String> uris,
                             @RequestParam(required = false) Boolean unique) throws UnsupportedEncodingException {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern
                ("yyyy-MM-dd HH:mm:ss");
        LocalDateTime decodedStart = LocalDateTime.parse(URLDecoder.decode(start, StandardCharsets.UTF_8.toString()), formatter);
        LocalDateTime decodedEnd = LocalDateTime.parse(URLDecoder.decode(end, StandardCharsets.UTF_8.toString()), formatter);

        return statService.getStat(decodedStart, decodedEnd, uris, unique);
    }

    @PostMapping("/hit")
    @ResponseStatus(value = HttpStatus.CREATED)
    void addStat(@RequestBody StatDtoCreate statDtoCreate) {
        statService.addStat(statDtoCreate);
    }

}
