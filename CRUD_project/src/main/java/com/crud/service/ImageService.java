package com.crud.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import com.crud.entity.OriginImage;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

@Service
public class ImageService {

    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void saveAllImagesInFolder(String folderPath) throws IOException {
        File folder = new File(folderPath);
        File[] listOfFiles = folder.listFiles();
        
        for (File file : listOfFiles) {
            if (file.isFile()) {
                byte[] imageBytes = Files.readAllBytes(file.toPath());

                OriginImage originImage = new OriginImage();
                originImage.setOriImage(imageBytes);

                entityManager.persist(originImage);
            }
        }
    }
}



