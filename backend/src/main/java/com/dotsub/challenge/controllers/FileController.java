package com.dotsub.challenge.controllers;

import java.io.IOException;
import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import javax.validation.Valid;

import com.dotsub.challenge.dto.FileDTO;
import com.dotsub.challenge.model.File;
import com.dotsub.challenge.repositories.FileRepository;
import com.dotsub.challenge.services.FileSystemStorageService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * FileController
 * 
 */
@Validated
@RepositoryRestController
public class FileController {

    private Logger logger = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private FileSystemStorageService fileService;

    @Autowired
    private EntityLinks entityLinks;

    @PostMapping("/files")
    public DeferredResult<ResponseEntity<?>> uploadNewFile(@Valid @RequestBody FileDTO dto) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        LinkBuilder linkBuilder = entityLinks.linkFor(File.class);

        CompletableFuture.supplyAsync(() -> {
            URI uri = null;
            try {
                uri = this.fileService.writeFile(dto.getData());

                File file = new File();
                file.setDescription(dto.getDescription());
                file.setTitle(dto.getTitle());
                file.setDataUri(uri.toString());
                this.fileRepository.save(file);

                URI link = linkBuilder.slash(file.getId()).toUri();

                return ResponseEntity.created(link).build();

            } catch (Exception e) {
                this.logger.error("There was a error while persisting the file", e);
                if (uri != null) {
                    try {
                        this.fileService.remove(uri);
                    } catch (IOException e1) {
                        this.logger.error("There was a error while excluding a file", e1);
                        throw new CompletionException(e1);
                    }
                }
                throw new CompletionException(e);
            }
        }).whenCompleteAsync((result, throwable) -> {
            if (throwable == null) {
                deferredResult.setResult(result);
            } else
                deferredResult.setErrorResult(throwable);
        });

        return deferredResult;
    }

    @PutMapping("/files/{id}")
    public DeferredResult<ResponseEntity<?>> updateFile(@Valid @RequestBody FileDTO dto,@PathVariable("id") Long id) {

        Optional<File> optional = this.fileRepository.findById(id);
        var deferredResult = new DeferredResult<ResponseEntity<?>>();
        
        if(optional.isPresent()){

            CompletableFuture.supplyAsync(() -> {
                URI uri = null;
                try {
                    uri = this.fileService.writeFile(dto.getData());
    
                    File file = optional.get();
                    String oldFileDataUri = file.getDataUri();

                    file.setDescription(dto.getDescription());
                    file.setTitle(dto.getTitle());
                    file.setDataUri(uri.toString());

                    this.fileRepository.save(file);
                    this.fileService.remove(URI.create(oldFileDataUri));
    
                    return ResponseEntity.noContent().build();
    
                } catch (Exception e) {
                    this.logger.error("There was a error while updating the file", e);
                    if (uri != null) {
                        try {
                            this.fileService.remove(uri);
                        } catch (IOException e1) {
                            this.logger.error("There was a error while excluding a file", e1);
                            throw new CompletionException(e1);
                        }
                    }
                    throw new CompletionException(e);
                }
            }).whenCompleteAsync((result, throwable) -> {
                if (throwable == null) {
                    deferredResult.setResult(result);
                } else
                    deferredResult.setErrorResult(throwable);
            });            
        } else {
            deferredResult.setResult(ResponseEntity.notFound().build());
        }
            
        
        return deferredResult;
    }

}