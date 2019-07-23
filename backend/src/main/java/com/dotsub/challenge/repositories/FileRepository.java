package com.dotsub.challenge.repositories;

import com.dotsub.challenge.model.File;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * FileRepository
 * 
 * Repository for the File domain entity, with paging and sorting features included
 * @since july 19, 2019
 */
@RepositoryRestResource(path = "files")
public interface FileRepository extends PagingAndSortingRepository<File,Long> {

    
}