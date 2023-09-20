package com.example.mainservice.service;

import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.EventMapper;
import com.example.mainservice.model.Category;
import com.example.mainservice.model.EventFullDto;
import com.example.mainservice.model.NewEventDto;
import com.example.mainservice.model.User;
import com.example.mainservice.storage.CategoryRepository;
import com.example.mainservice.storage.EventRepository;
import com.example.mainservice.storage.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final EventMapper eventMapper;


    public EventFullDto addEvent(Long userId, NewEventDto newEventDto) {

        User user = userRepository.findById(userId).orElseThrow(NotFoundException::new);
        Category category =categoryRepository.findById(newEventDto.getCategory()).orElseThrow(NotFoundException::new);
        return eventMapper.mapToDto(eventRepository.save(eventMapper.mapToEvent(user,category,newEventDto)));
    }
}
