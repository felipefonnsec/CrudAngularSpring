package com.felipe.service;


import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import com.felipe.dto.CourseDTO;
import com.felipe.dto.CoursePageDTO;
import com.felipe.dto.mapper.CourseMapper;
import com.felipe.exeption.RecordNotFoundException;
import com.felipe.model.Course;
import com.felipe.repository.CourseRepository;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;


@Validated
@Service
public class CourseService {
    
    //inject de dependencia via spring
    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseService(CourseRepository courseRepository, CourseMapper courseMapper){
        this.courseRepository = courseRepository;
        this.courseMapper = courseMapper;
    }

    //list de objetos(cursos) para retorno com paginacao
    public CoursePageDTO list(@PositiveOrZero int page,@Positive @Max(100) int size){
      Page<Course> pageCourse = courseRepository.findAll(PageRequest.of(page, size));
      List<CourseDTO> courses = pageCourse.get().map(courseMapper::toDTO).collect(Collectors.toList());
      return new CoursePageDTO(courses, pageCourse.getTotalElements(),pageCourse.getTotalPages());
    }

    //Busca por id - com tratamento de erro
    @SuppressWarnings("null")
    public CourseDTO findById(@NotNull @Positive Long id){
        return courseRepository.findById(id).map(courseMapper::toDTO)        
            .orElseThrow(() -> new RecordNotFoundException(id));
    }

    //criacao de cursos com retorno status 201
    @SuppressWarnings("null")
    public CourseDTO create( @Valid @NotNull CourseDTO course){
      return courseMapper.toDTO(courseRepository.save(courseMapper.toEntity(course)));
    }

    //editando curso
    @SuppressWarnings("null")
    public CourseDTO update(@NotNull @Positive Long id,@Valid @NotNull CourseDTO courseDTO){
      return courseRepository.findById(id)
          .map(recordFound -> {
            Course course = courseMapper.toEntity(courseDTO);
            recordFound.setName(courseDTO.name());
            recordFound.setCategory(courseMapper.convertCategoryValue(courseDTO.category()));
            recordFound.getLessons().clear();
            course.getLessons().forEach(recordFound.getLessons()::add);
            return courseMapper.toDTO(courseRepository.save(recordFound));
          }).orElseThrow(() -> new RecordNotFoundException(id));
    }

    //deletar curso
    @SuppressWarnings("null")
    public void delete(@NotNull @Positive Long id){
        courseRepository.delete(courseRepository.findById(id)
              .orElseThrow(() -> new RecordNotFoundException(id)));
    }

}
