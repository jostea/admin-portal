package com.internship.adminpanel.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.net.MalformedURLException;

@Service
@Slf4j
@RequiredArgsConstructor
public class AssetsStorageService {

    @Value("${root.path}")
    private String rootPath;

    @Value("${server.servlet.context-path}")
    private String contextPath;

    public Resource loadAsResource(String fileNameWithPath) throws FileNotFoundException {
        try {
            Resource resource = new UrlResource(fileNameWithPath);
            if (resource.exists()) {
                return resource;
            } else {
                resource = new UrlResource("https://am-interns-project-s3.s3.us-east-2.amazonaws.com/no_image_found.png");
                return resource;
            }
        } catch (MalformedURLException e) {
            log.error("Error during loading the file {}", fileNameWithPath, e);
        }
        return null;
    }
}
