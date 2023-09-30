package com.example.mainservice.mapper;

import com.example.mainservice.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class CompilationMapper {
    private final EventMapper eventMapper;
    public Compilation toCompilation(NewCompilationDto newCompilationDto, List<Event> events) {
        return new Compilation(null, events,
                newCompilationDto.getPinned(),
                newCompilationDto.getTitle());
    }
    public CompilationDto toCompilationDto (Compilation compilation){
        return new CompilationDto(
                compilation.getEvents().stream().map(eventMapper::mapToShortDto).collect(Collectors.toList()),
                compilation.getId(), compilation.getPinned(),compilation.getTitle());
    }
    public Compilation toCompilationFromUpdateDto(Compilation compilation, UpdateCompilationRequest updateDto,
                                                  List<Event> events){
        return new Compilation(compilation.getId(),events,
                updateDto.getPinned() == null ? compilation.getPinned() : updateDto.getPinned(),
                updateDto.getTitle() == null? compilation.getTitle() : updateDto.getTitle());
    }
}
