package com.dotsub.challenge.services;

import java.io.IOException;
import java.net.URI;

/**
 * DataStorageService
 */
public interface DataStorageService {

    public URI writeFile(byte[] data) throws IOException;
    public boolean remove(URI uri) throws IOException;
}