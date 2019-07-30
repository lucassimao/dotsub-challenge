package com.dotsub.challenge;

import static java.io.File.createTempFile;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.util.stream.IntStream;

import com.dotsub.challenge.model.File;
import com.dotsub.challenge.repositories.FileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
@EnableAsync
public class ChallengeApplication implements WebMvcConfigurer {

	public static void main(String[] args) {
		SpringApplication.run(ChallengeApplication.class, args);
	}


	@Bean
	public CommandLineRunner populateDB(@Autowired FileRepository repo){
		return (args)->{
			if (repo.count() > 0)
				return;

			IntStream.range(0,100).forEach(idx -> {
				
				try {
					File f = new File();
					f.setDataUri(createTempFile("kkk", "kkk1").toURI().toString());
					f.setDescription("file " + idx);
					f.setTitle("title "+idx);
					f.setDateCreated(ZonedDateTime.now());
					repo.save(f);	
					System.out.println(f);
				} catch (IOException e) {
					e.printStackTrace();
				}
			});
		};
		
	}

}