package com.dotsub.challenge.controllers;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import javax.validation.Valid;

import com.dotsub.challenge.dto.FileDTO;
import com.dotsub.challenge.model.File;
import com.dotsub.challenge.repositories.FileRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.RepositoryRestController;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.LinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * FileController
 * 
 */
@Validated
@RepositoryRestController
public class FileController {

    @Autowired
    private FileRepository fileRepository;

    @Autowired
    private EntityLinks entityLinks;

    @PostMapping("/files")
    public DeferredResult<ResponseEntity<?>> uploadNewFile(@Valid @RequestBody FileDTO dto) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();
        LinkBuilder linkBuilder = entityLinks.linkFor(File.class);

        CompletableFuture.supplyAsync(() -> {
            File newFile = this.fileRepository.save(dto);
            URI link = linkBuilder.slash(newFile.getId()).toUri();
            return ResponseEntity.created(link).build();
        }).whenCompleteAsync((result, throwable) -> {
            if (throwable == null) {
                deferredResult.setResult(result);
            } else
                deferredResult.setErrorResult(throwable);
        });

        return deferredResult;
    }

    // manages the update of the whole file entity
    @PutMapping("/files/{id}")
    public DeferredResult<ResponseEntity<?>> updateFile(@Valid @RequestBody FileDTO dto, @PathVariable("id") Long id) {
        return patchFile(dto, id);
    }

    // the main difference here from the PUT endpoint, is the ability to update a single field
    @PatchMapping("/files/{id}")
    public DeferredResult<ResponseEntity<?>> patchFile(@RequestBody FileDTO dto, @PathVariable("id") Long id) {

        Optional<File> optional = this.fileRepository.findById(id);
        var deferredResult = new DeferredResult<ResponseEntity<?>>();

        if (optional.isPresent()) {
            CompletableFuture.supplyAsync(() -> {

                this.fileRepository.update(optional.get(), dto);
                return ResponseEntity.noContent().build();

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