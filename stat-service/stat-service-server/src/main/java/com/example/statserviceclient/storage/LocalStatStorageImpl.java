package com.example.statserviceclient.storage;

import com.example.statserviceclient.mapper.StatMapper;
import com.example.statservicedto.StatModel;
import com.example.statservicedto.dto.StatDtoCreate;
import com.example.statservicedto.dto.StatDtoGet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class LocalStatStorageImpl implements StatStorage {
    private final StatMapper statMapper;
    private static final String APP = "ewm-main-service";
    private final List<StatModel> storage = new ArrayList<>();

    @Override
    public void addStat(StatDtoCreate statDtoCreate) throws UnsupportedEncodingException {
        storage.add(statMapper.mapFromDto(statDtoCreate));
    }

    @Override
    public List<StatDtoGet> getStat(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        List<StatDtoGet> result = new ArrayList<>();
        Set<String> ips = new HashSet<>();
        if (uris != null && !uris.isEmpty()) {
            for (String uri : uris) {
                long hits = storage.stream().filter(s -> s.getUri().equals(uri) && !s.getTimestamp().isBefore(start)
                        && !s.getTimestamp().isAfter(end)
                        && (unique == null || ips.add(s.getIp()))
                ).count();
                result.add(new StatDtoGet(APP, uri, hits));
            }
        } else {
            Set<String> urisSet = new HashSet<>();
            for (StatModel statModel : storage) {
                urisSet.add(statModel.getUri());
            }
            for (String uri : urisSet) {
                long hits = storage.stream().filter(s -> s.getUri().equals(uri) && !s.getTimestamp().isBefore(start)
                        && !s.getTimestamp().isAfter(end) && (unique == null || ips.add(s.getIp()))).count();
                result.add(new StatDtoGet(APP, uri, hits));
            }
        }
        return result;
    }

}
