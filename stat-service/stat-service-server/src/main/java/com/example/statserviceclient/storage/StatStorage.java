package com.example.statserviceclient.storage;


import com.example.statservicedto.dto.StatDtoCreate;
import com.example.statservicedto.dto.StatDtoGet;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.List;


public interface StatStorage {
     List<StatDtoGet> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
    void addStat (StatDtoCreate statDtoCreate) throws UnsupportedEncodingException;
}
