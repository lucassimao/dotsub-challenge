package com.dotsub.challenge.repositories;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.dotsub.challenge.services.DataStorageService;

import org.springframework.beans.factory.annotation.Autowired;

/**
 * This default implementation will help FileRepository to
 * enhance the delete method, allowing it to remove the binary data associated with the file record
 */
public class CustomFileRepositoryImpl implements CustomFileRepository {


    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private DataStorageService dataStorageService;

    @Override
    public EntityManager getEntityManager() {
        return this.entityManager;
    }

    @Override
    public DataStorageService getDataStorageService() {
		return this.dataStorageService;
	}
    
}