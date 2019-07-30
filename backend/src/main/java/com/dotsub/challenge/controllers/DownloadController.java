package com.dotsub.challenge.controllers;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

import com.dotsub.challenge.model.File;
import com.dotsub.challenge.repositories.FileRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.async.DeferredResult;

/**
 * FileController
 */
@Controller
public class DownloadController {

    private Logger logger = LoggerFactory.getLogger(DownloadController.class);

    @Autowired
    private FileRepository fileRepository;


    @GetMapping(value = "/download/{id}")
    public DeferredResult<ResponseEntity<?>> getFileData(@PathVariable("id") Long fileId) {

        DeferredResult<ResponseEntity<?>> deferredResult = new DeferredResult<>();

        Optional<File> optional = this.fileRepository.findById(fileId);
        if (optional.isPresent()) {
            URI uri = URI.create(optional.get().getDataUri());

            CompletableFuture.supplyAsync(() -> {
                try {
                    this.logger.info("reading from {}", uri.toString());
                    return Files.readAllBytes(Path.of(uri));
                } catch (IOException e) {
                    this.logger.error("There was a error while reading data for the file #" + fileId, e);
                    throw new CompletionException(e);
                }

            }).whenCompleteAsync((data, throwable) -> {
                if (throwable == null) {
                    File file = optional.get();
                    String contentDisposition = "attachment";
                    String contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
                    BodyBuilder bodyBuilder = ResponseEntity.ok().contentLength(data.length);

                    if (!StringUtils.isEmpty(file.getOriginalFileName()))
                        contentDisposition +=  ";filename=\"" + file.getOriginalFileName() +"\"";
                        
                    if(!StringUtils.isEmpty(file.getMimeType()))
                        contentType = file.getMimeType();

                    bodyBuilder.header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                                .header(HttpHeaders.CONTENT_TYPE, contentType);

                    ResponseEntity<byte[]> result = bodyBuilder.body(data);

                    deferredResult.setResult(result);
                } else
                    deferredResult.setErrorResult(throwable);
            });
        } else {
            ResponseEntity<String> result = ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("There is no file with id " + fileId);
            deferredResult.setResult(result);
        }

        return deferredResult;
    }
}