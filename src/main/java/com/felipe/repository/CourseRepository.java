package com.felipe.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.felipe.model.Course;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {
    
}
