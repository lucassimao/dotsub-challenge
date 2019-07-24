package com.dotsub.challenge.repositories;

import java.io.IOException;
import java.net.URI;

import com.dotsub.challenge.model.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * FileRepository
 * 
 * Repository for the File domain entity, with paging and sorting features included
 * @since july 19, 2019
 */
@RepositoryRestResource(path = "files")
public interface FileRepository extends PagingAndSortingRepository<File,Long>, CustomFileRepository {

    @Override
    default void delete(File entity) {
        try {
            getDataStorageService().remove(URI.create(entity.getDataUri()));
            getEntityManager().remove(entity);
        } catch (IOException e) {
            Logger logger = LoggerFactory.getLogger(FileRepository.class);
            logger.error("There was a problem when removing the file", e);
		}
    }
    
}