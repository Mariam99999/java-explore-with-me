package com.example.statserviceclient.service;

import com.example.statserviceclient.mapper.StatMapper;
import com.example.statserviceclient.storage.StatRepository;
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
    private final StatRepository statRepository;
    private final StatStorage statStorage;
    private final StatMapper statMapper;
    public List<StatDtoGet> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique){
        if((uris == null || uris.isEmpty()) && (unique == null || !unique)) return statRepository.findByDate(start,end);
        if (uris == null || uris.isEmpty()) return statRepository.findByUniqAndDate(start,end);
        if (unique == null || !unique) return statRepository.findByDateAndUri(start,end,uris);
        return statRepository.findByUniqAndDateAndUri(start,end,uris);
    }
    public void addStat (StatDtoCreate statDtoCreate)  {
        try {
            statRepository.save(statMapper.mapFromDto(statDtoCreate));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

    }

}
