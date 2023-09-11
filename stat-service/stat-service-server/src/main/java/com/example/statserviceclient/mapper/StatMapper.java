package com.example.statserviceclient.mapper;

import com.example.statserviceclient.model.StatModel;
import com.example.statservicedto.dto.StatDtoCreate;
import com.example.statservicedto.dto.StatDtoGet;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class StatMapper {
  public StatDtoGet mapToDto (StatModel statModel, Long hits){
       return new StatDtoGet(statModel.getApp(),statModel.getUri(),hits);
   }
  public StatModel mapFromDto (StatDtoCreate statDtoCreate) throws UnsupportedEncodingException {
      DateTimeFormatter formatter = DateTimeFormatter.ofPattern
              ("yyyy-MM-dd HH:mm:ss");
      LocalDateTime timestamp = LocalDateTime.parse(statDtoCreate.getTimestamp(), formatter);
       return new StatModel(null,statDtoCreate.getApp(),statDtoCreate.getUri(),statDtoCreate.getIp(),timestamp);
   }
}
