package com.dotsub.challenge.repositories;

import javax.persistence.EntityManager;

import com.dotsub.challenge.services.DataStorageService;

/**
 * Defining additional useful methods for FileRepository 
 */
public interface CustomFileRepository {

    EntityManager getEntityManager();
    DataStorageService getDataStorageService();
    
}