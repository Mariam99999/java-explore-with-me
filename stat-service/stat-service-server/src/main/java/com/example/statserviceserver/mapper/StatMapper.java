package com.example.statserviceserver.mapper;


import com.example.statservicedto.StatDtoCreate;
import com.example.statservicedto.StatDtoGet;
import com.example.statserviceserver.model.StatModel;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;

@Component
public class StatMapper {
    public StatDtoGet mapToDto(StatModel statModel, Long hits) {
        return new StatDtoGet(statModel.getApp(), statModel.getUri(), hits);
    }

    public StatModel mapFromDto(StatDtoCreate statDtoCreate) throws UnsupportedEncodingException {
        return new StatModel(null, statDtoCreate.getApp(), statDtoCreate.getUri(), statDtoCreate.getIp(),statDtoCreate.getTimestamp());
    }
}
