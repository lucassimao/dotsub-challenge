package com.dotsub.challenge.repositories;

import javax.persistence.EntityManager;

import com.dotsub.challenge.services.DataStorageService;

import org.springframework.data.rest.core.annotation.RestResource;

/**
 * Defining additional useful methods for FileRepository 
 */
public interface CustomFileRepository {
    @RestResource(exported = false)
    EntityManager getEntityManager();
    @RestResource(exported = false)
    DataStorageService getDataStorageService();
    
}