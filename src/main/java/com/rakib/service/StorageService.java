package com.rakib.service;


import com.rakib.entity.FileData;
import com.rakib.repositoty.FileDataRepository;
import com.rakib.repositoty.ImageDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@Service

@RequiredArgsConstructor
public class StorageService {


    private final FileDataRepository fileDataRepository;
    private final ImageDataRepository imageDataRepository;


    private final String FOLDER_PATH= "G:\\img/";


    public String uploadZImgToSys(MultipartFile file) throws IOException {


        String originalFilename = file.getOriginalFilename();
        String extension = "";

        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }

        String uniqueFileName = UUID.randomUUID().toString() + extension;
        String filePath = FOLDER_PATH + uniqueFileName;



//        String filePath = FOLDER_PATH + file.getOriginalFilename();





        FileData fileData = FileData.builder()
                        .name(uniqueFileName)
                        .type(file.getContentType())
                        .filePath(filePath).build();

        FileData saved = fileDataRepository.save(fileData);

        file.transferTo(new File(filePath));

        return "File uploaded successfully: " + saved.getFilePath();
    }


    public byte[] downloadImageFromFileSystem(String fileName) throws IOException
    {


        FileData fileData = fileDataRepository.findAllSortedByNameUsingNative(fileName);

        if (fileData == null){

            throw new RuntimeException(("File not found: " + fileName));
        }

        return Files.readAllBytes(new File(fileData.getFilePath()).toPath());

    }

}
