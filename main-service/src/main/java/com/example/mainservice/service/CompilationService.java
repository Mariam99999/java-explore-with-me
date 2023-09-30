package com.example.mainservice.service;

import com.example.mainservice.exception.NotFoundException;
import com.example.mainservice.mapper.CompilationMapper;
import com.example.mainservice.mapper.EventMapper;
import com.example.mainservice.model.*;
import com.example.mainservice.storage.CompilationRepository;
import com.example.mainservice.storage.EventRepository;
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

    private final CompilationMapper compilationMapper;
    private final EventRepository eventRepository;

    public CompilationDto addCompilation(NewCompilationDto newCompilationDto) {
        List<Event> events = eventRepository.findAllById(newCompilationDto.getEvents());

        Compilation compilation = compilationMapper.toCompilation(newCompilationDto, events);
        Compilation c = compilationRepository.save(compilation);
        return compilationMapper.toCompilationDto(c);
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
        return compilations.stream().map(compilationMapper::toCompilationDto).collect(Collectors.toList());
    }

    public CompilationDto getCompilationById(Long compId) {
        return compilationMapper.toCompilationDto(compilationRepository.findById(compId)
                .orElseThrow(NotFoundException::new));
    }

    public CompilationDto updateCompilation(Long compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = compilationRepository.findById(compId).orElseThrow(NotFoundException::new);
        List<Event> events = compilation.getEvents();
        if (updateCompilationRequest.getEvents() != null) {
            events = eventRepository.findAllById(updateCompilationRequest.getEvents());
        }
        Compilation updatedComp = compilationMapper.toCompilationFromUpdateDto(compilation,updateCompilationRequest,events);
       return compilationMapper.toCompilationDto(compilationRepository.save(updatedComp));
    }
}
