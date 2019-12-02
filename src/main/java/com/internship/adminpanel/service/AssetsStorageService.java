package com.internship.adminpanel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetsStorageService {
    public Resource loadAsResource(String fileNameWithPath) throws FileNotFoundException {
        try {
            Resource resource = new UrlResource(fileNameWithPath);
            if (resource.exists()) {
                return resource;
            } else {
                //TODO: should be path to "No_Image_Found" location instead of file on local machine
                resource = new UrlResource("file:C://INTERNSHIP/sqlimages/no_image_found.png");
                return resource;
//                throw new FileNotFoundException(fileNameWithPath + " file was not found.");
            }
        } catch (MalformedURLException e) {
            log.error("Error during loading the file {}", fileNameWithPath, e);
        }
        return null;
    }
}
