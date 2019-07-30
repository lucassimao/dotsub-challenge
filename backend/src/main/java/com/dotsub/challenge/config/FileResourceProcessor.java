package com.dotsub.challenge.config;

import com.dotsub.challenge.controllers.DownloadController;
import com.dotsub.challenge.model.File;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceProcessor;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

@Configuration
public class FileResourceProcessor {

  @Bean
  public ResourceProcessor<Resource<File>> processor() {

    return new ResourceProcessor<Resource<File>>() {

      @Override
      public Resource<File> process(Resource<File> resource) {

        resource.add(ControllerLinkBuilder.linkTo(DownloadController.class).slash("download")
            .slash(resource.getContent().getId()).withRel("download"));
        return resource;
      }
    };
  }

}