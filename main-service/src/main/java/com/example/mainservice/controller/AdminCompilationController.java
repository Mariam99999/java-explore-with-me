package com.example.mainservice.controller;

import com.example.mainservice.model.CompilationDto;
import com.example.mainservice.model.NewCompilationDto;
import com.example.mainservice.model.ParticipationRequestDto;
import com.example.mainservice.model.UpdateCompilationRequest;
import com.example.mainservice.service.CompilationService;
import com.example.mainservice.service.ParticipationRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/admin/compilations")
public class AdminCompilationController {
    private final CompilationService compilationService;


    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public CompilationDto addCompilation(@RequestBody NewCompilationDto newCompilationDto){
        return compilationService.addCompilation(newCompilationDto);
    }
    @DeleteMapping("{compId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompilation (@PathVariable Long compId){
        compilationService.deleteCompilation(compId);
    }
    @PatchMapping("/{compId}")
    CompilationDto updateCompilation(@PathVariable Long compId,@RequestBody UpdateCompilationRequest updateCompilationRequest){
        return compilationService.updateCompilation(compId,updateCompilationRequest);
    }

}
