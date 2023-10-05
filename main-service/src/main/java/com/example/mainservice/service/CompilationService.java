package com.example.mainservice.service;

import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.CompilationMapper;
import com.example.mainservice.mapper.EventMapper;
import com.example.mainservice.model.*;
import com.example.mainservice.storage.CompilationRepository;
import com.example.mainservice.storage.EventRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventMapper eventMapper;
    private final EventService eventService;

    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) throws JsonProcessingException {

        List<Event> events = newCompilationDto.getEvents() == null ? List.of() :
                eventRepository.findAllById(newCompilationDto.getEvents());
        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        Compilation c = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(c, getEventsShortDto(c));
    }

    public void deleteCompilation(Long compId) {
        compilationRepository.findById(compId).orElseThrow(NotFoundException::new);
        compilationRepository.deleteById(compId);
    }

    public List<CompilationDto> getCompilation(Boolean pinned, int from, int size) {
        Pageable pageable = PageRequest.of(from / size, size);
        List<Compilation> compilations = pinned == null ?
                compilationRepository.findAll(pageable).getContent() :
                compilationRepository.findAllByPinned(pinned, pageable);
        return compilations.stream()
                .map(c -> {
                    try {
                        return compilationMapper.toCompilationDto(c, getEventsShortDto(c));
                    } catch (JsonProcessingException e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(Long compId) throws JsonProcessingException {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(NotFoundException::new);
        return compilationMapper.toCompilationDto(compilation, getEventsShortDto(compilation));
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) throws JsonProcessingException {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(NotFoundException::new);
        List<Event> events = compilation.getEvents();
        if (updateCompilationRequest.getEvents() != null) {
            events = eventRepository.findAllById(updateCompilationRequest.getEvents());
        }
        Compilation updatedComp = compilationMapper.toCompilationFromUpdateDto(compilation, updateCompilationRequest, events);
        Compilation savedCompilation = compilationRepository.save(updatedComp);
        return compilationMapper.toCompilationDto(savedCompilation, getEventsShortDto(savedCompilation));
    }

    private List<EventShortDto> getEventsShortDto(Compilation compilation) throws JsonProcessingException {
        List<Event> events = compilation.getEvents();
        return eventService.getEventsDto(events).stream().map(eventMapper::mapToShortDto).collect(Collectors.toList());
    }
}
