package com.felipe.controller;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.felipe.dto.CourseDTO;
import com.felipe.dto.CoursePageDTO;
import com.felipe.service.CourseService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Validated
@RestController
@RequestMapping("/api/courses")
public class CourseController {
    
  
  //declarando o serice
    private final CourseService courseService;

    public CourseController(CourseService courseService){
      this.courseService = courseService;
    }

    //list de objetos(cursos) para retorno
    @GetMapping
    public CoursePageDTO list(@RequestParam(defaultValue = "0") @PositiveOrZero int page,
        @RequestParam(defaultValue = "0") @Positive @Max(100) int size){
        return courseService.list(page, size);
    }

    //busca curso por id
    @GetMapping("/{id}")
    public CourseDTO findById(@PathVariable @NotNull @Positive Long id){
        return courseService.findById(id);
    }

    //criacao de cursos com retorno status 201
    @PostMapping
    @ResponseStatus(code = HttpStatus.CREATED)
    public CourseDTO create(@RequestBody @Valid @NotNull CourseDTO course){
      return courseService.create(course);
    }

    //editando curso
    @PutMapping("/{id}")
    public CourseDTO update(@PathVariable @NotNull @Positive Long id, 
        @RequestBody @Valid @NotNull CourseDTO course) {
          return courseService.update(id, course);
    }
    
    //deletar curso
    @DeleteMapping("/{id}")
    @ResponseStatus(code = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable @NotNull @Positive Long id){
       courseService.delete(id);       
    }
}
