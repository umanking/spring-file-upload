package com.example.demo;

import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author Geonguk Han
 * @since 2019-11-12
 */

public interface StorageService {

    void init();

    void store(MultipartFile file);

    // todo: stream으로 받아서 처리, Collection 으로 받지 않네?
    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void deleteAll();

}
