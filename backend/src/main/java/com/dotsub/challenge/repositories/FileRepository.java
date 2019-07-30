package com.dotsub.challenge.repositories;

import java.io.IOException;
import java.net.URI;

import com.dotsub.challenge.dto.FileDTO;
import com.dotsub.challenge.model.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
 * FileRepository
 * 
 * Repository for the File domain entity, with paging and sorting features
 * included
 * 
 * @since july 19, 2019
 */
@RepositoryRestResource(path = "files")
public interface FileRepository extends PagingAndSortingRepository<File, Long>, CustomFileRepository {

    @Query("select f from File f where LOWER(f.description) LIKE LOWER('%' || ?1 || '%') OR LOWER(f.title) LIKE LOWER('%' || ?1 || '%')")
    Page<File> findByDescriptionOrTitle(String searchTerm,Pageable pageable);

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

    @Transactional
    default public File save(FileDTO dto) throws RuntimeException {
        var logger = LoggerFactory.getLogger(FileRepository.class);
        URI uri = null;
        try {
            uri = this.getDataStorageService().writeFile(dto.getData());

            File file = new File();
            file.setMimeType(dto.getMimeType());
            file.setOriginalFileName(dto.getOriginalFileName());
            file.setDescription(dto.getDescription());
            file.setTitle(dto.getTitle());
            file.setDataUri(uri.toString());
            this.save(file);

            return file;
        } catch (Exception e) {
            logger.error("There was a error while persisting the file", e);
            if (uri != null) {
                try {
                    this.getDataStorageService().remove(uri);
                } catch (IOException e1) {
                    logger.error("There was a error while excluding a file", e1);
                    throw new RuntimeException(e1);
                }
            }
            throw new RuntimeException(e);
        }        
    }

    @Transactional
    default void update(File entity, FileDTO dto) throws RuntimeException {
        var logger = LoggerFactory.getLogger(FileRepository.class);

        if (StringUtils.hasText(dto.getDescription()))
            entity.setDescription(dto.getDescription());

        if (StringUtils.hasText(dto.getTitle()))
            entity.setTitle(dto.getTitle());

        if (StringUtils.hasText(dto.getMimeType()))
            entity.setMimeType(dto.getMimeType());

        if (StringUtils.hasText(dto.getOriginalFileName()))
            entity.setOriginalFileName(dto.getOriginalFileName());

        if (dto.getData() != null) {
            URI uri = null;
            try {
                uri = this.getDataStorageService().writeFile(dto.getData());

                String oldFileDataUri = entity.getDataUri();

                entity.setDataUri(uri.toString());

                this.save(entity);
                this.getDataStorageService().remove(URI.create(oldFileDataUri));

            } catch (IOException e) {
                logger.error("There was a error while updating the file", e);
                if (uri != null) {
                    try {
                        this.getDataStorageService().remove(uri);
                    } catch (IOException e1) {
                        logger.error("There was a error while excluding a file", e1);
                        throw new RuntimeException(e);
                    }
                }
                throw new RuntimeException(e);
            }

        } else {
            this.save(entity);
        }
    }

}