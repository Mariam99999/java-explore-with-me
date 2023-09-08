package com.example.statserviceclient.service;

import com.example.statserviceclient.storage.StatStorage;
import com.example.statservicedto.dto.StatDtoCreate;
import com.example.statservicedto.dto.StatDtoGet;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;


@Service
@AllArgsConstructor
public class StatService {
    private final StatStorage statStorage;
    public List<StatDtoGet> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique){
        return statStorage.getStat(start,end,uris,unique);
    }
    public void addStat (StatDtoCreate statDtoCreate){
        try {
            statStorage.addStat(statDtoCreate);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }

}
