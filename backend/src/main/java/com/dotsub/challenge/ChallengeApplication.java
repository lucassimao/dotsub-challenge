package com.dotsub.challenge;

import java.nio.file.Files;

import com.dotsub.challenge.dto.FileDTO;
import com.dotsub.challenge.repositories.FileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.util.ResourceUtils;
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

            FileDTO dto1 = new FileDTO("The Great Wall of China",
                        "The Great Wall of China is the collective name of a series of fortification systems generally built across the historical northern borders of China to protect and consolidate territories of Chinese states",
                        Files.readAllBytes(ResourceUtils.getFile("classpath:img/The_Great_Wall_of_China.jpg").toPath()),
                        MediaType.IMAGE_JPEG_VALUE,"The_Great_Wall_of_China.jpg");

            FileDTO dto2 = new FileDTO("Petra","Petra is a historical and archaeological city in southern Jordan",
                        Files.readAllBytes(ResourceUtils.getFile("classpath:img/Petra_Jordan_BW_21.jpeg").toPath()),
                        MediaType.IMAGE_JPEG_VALUE,"Petra_Jordan.jpeg");                        
                
            FileDTO dto3 = new FileDTO("Christ the Redeemer","Christ the Redeemer is an Art Deco statue of Jesus Christ in Rio de Janeiro, Brazil, created by French sculptor Paul Landowski ",
                        Files.readAllBytes(ResourceUtils.getFile("classpath:img/Cristo_Redentor.jpg").toPath()),
                        MediaType.IMAGE_JPEG_VALUE,"Cristo_Redentor.jpg");  
                        
            FileDTO dto4 = new FileDTO("Machu Picchu","Machu Picchu is a 15th-century Inca citadel, located in the Eastern Cordillera of southern Peru, on a 2,430-metre (7,970 ft) mountain ridge",
                        Files.readAllBytes(ResourceUtils.getFile("classpath:img/Machu_Picchu.jpg").toPath()),
                        MediaType.IMAGE_JPEG_VALUE,"Machu_Picchu.jpg");  
                        
            FileDTO dto5 = new FileDTO("Chichen Itza","Chichen Itza was a large pre-Columbian city built by the Maya people of the Terminal Classic period. The archaeological site is located in Tinúm Municipality, Yucatán State, Mexico.",
                        Files.readAllBytes(ResourceUtils.getFile("classpath:img/Chichen_Itza_3.jpg").toPath()),
                        MediaType.IMAGE_JPEG_VALUE,"Chichen_Itza_3.jpg");  
                        
            FileDTO dto6 = new FileDTO("Colosseum","Colosseum is an oval amphitheatre in the centre of the city of Rome, Italy",
                        Files.readAllBytes(ResourceUtils.getFile("classpath:img/Colosseum_in_Rome.jpg").toPath()),
                        MediaType.IMAGE_JPEG_VALUE,"Colosseum_in_Rome.jpg");  

            FileDTO dto7 = new FileDTO("Taj Mahal","Taj Mahal is an ivory-white marble mausoleum on the south bank of the Yamuna river in the Indian city of Agra",
                        Files.readAllBytes(ResourceUtils.getFile("classpath:img/Taj_Mahal.jpg").toPath()),
                        MediaType.IMAGE_JPEG_VALUE,"Taj_Mahal.jpg");  

            repo.save(dto1);
            repo.save(dto2);
            repo.save(dto3);
            repo.save(dto4);
            repo.save(dto5);
            repo.save(dto6);
            repo.save(dto7);
		};
		
	}

}