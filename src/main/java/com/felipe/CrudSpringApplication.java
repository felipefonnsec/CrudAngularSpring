package com.felipe;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

import com.felipe.enums.Category;
import com.felipe.model.Course;
import com.felipe.model.Lesson;
import com.felipe.repository.CourseRepository;

//@Profile dev esta rodando o aplication-dev
@SpringBootApplication
@Profile("dev")
public class CrudSpringApplication {

	public static void main(String[] args) {
		SpringApplication.run(CrudSpringApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(CourseRepository courseRepository){
		return args -> {
			courseRepository.deleteAll();

			for(int i = 0; i < 20; i++){
				Course c = new Course();
				c.setName("Angular com Spring" + i);
				c.setCategory(Category.FRONT_END);

				Lesson l = new Lesson();
				l.setName("teste");
				l.setYoutubeUrl("url1254698");
				l.setCourse(c);
				c.getLessons().add(l);

				courseRepository.save(c);
			}
		};
	}

}
