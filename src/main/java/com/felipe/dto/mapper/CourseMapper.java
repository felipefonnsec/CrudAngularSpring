package com.felipe.dto.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.felipe.dto.CourseDTO;
import com.felipe.dto.LessonDTO;
import com.felipe.enums.Category;
import com.felipe.model.Course;
import com.felipe.model.Lesson;

@Component
public class CourseMapper {
    
    public CourseDTO toDTO(Course course){
        if (course == null) {
            return null;
        }
        List<LessonDTO> lessons = course.getLessons()
            .stream()
            .map(lesson -> new LessonDTO(lesson.getId(), lesson.getName(), lesson.getYoutubeUrl()))
            .collect(Collectors.toList());
        return new CourseDTO(course.getId(), course.getName(), 
        course.getCategory().getValue(), lessons);
    }

    public Course toEntity(CourseDTO courseDTO) {
        if (courseDTO == null) {
            return null;
        }

        Course course = new Course();
        if (courseDTO.id() != null) {
            course.setId(courseDTO.id());
        }
        course.setName(courseDTO.name());
        course.setCategory(convertCategoryValue(courseDTO.category()));

        //passando de lessonDTO para lessons
        List<Lesson> lessons = courseDTO.lessons().stream().map(lessonDTO -> {
            var lesson = new Lesson();
            lesson.setId(lessonDTO.id());
            lesson.setName(lessonDTO.name());
            lesson.setYoutubeUrl(lessonDTO.youtubeUrl());
            lesson.setCourse(course);
            return lesson;
        }).collect(Collectors.toList());
        course.setLessons(lessons);

        return course;
    }

    //calcular valor do enum
    public Category convertCategoryValue(String value){
        if (value == null) {
            return null;
        }
       return switch (value) {
            case "Front-end" -> Category.FRONT_END;
            case "Back-end" -> Category.BACK_END;
            default -> throw new IllegalArgumentException("Categoria Invalida" + value);
        };
    }
}
