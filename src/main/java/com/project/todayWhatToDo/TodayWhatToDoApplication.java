package com.project.todayWhatToDo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class TodayWhatToDoApplication {

	public static void main(String[] args) {
		SpringApplication.run(TodayWhatToDoApplication.class, args);
	}

}
