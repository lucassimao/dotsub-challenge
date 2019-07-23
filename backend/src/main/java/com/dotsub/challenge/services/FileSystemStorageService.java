package com.dotsub.challenge.services;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

/**
 * Implementation of DataStorageService who writes data to the local filesystem's temp folder
 */
@Service
@Profile("default")
public class FileSystemStorageService implements DataStorageService {

    private Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);

    public URI writeFile(byte[] data) throws IOException {
        
        Path path = File.createTempFile("challenge", ".data").toPath();
        Files.write(path, data);
        logger.info("{} bytes written to {}", data.length, path.toUri().toString());
        return path.toUri();
    }

    public boolean remove(URI uri) throws IOException {
        return Files.deleteIfExists(Path.of(uri));
    }
    
}