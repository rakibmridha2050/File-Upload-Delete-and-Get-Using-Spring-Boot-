package com.rakib.controller;


import com.rakib.dtos.FileInfo;
import com.rakib.dtos.ResponseMessage;
import com.rakib.entity.FileData;
import com.rakib.repositoty.FileDataRepository;
import com.rakib.service.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("controller")
@RequiredArgsConstructor
public class Controller {


    private final StorageService storageService;
    private final FileDataRepository fileDataRepository;



    @GetMapping("/fileSystem/{fileName}")
    public ResponseEntity<?> downloadImageFromFileSystem(@PathVariable String fileName) throws IOException{

        byte[] imageData = storageService.downloadImageFromFileSystem(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }



    @PostMapping(value = "/fileSystem", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Upload image file")
    public ResponseEntity<?> uploadImageToFileSystem(@RequestParam("image")MultipartFile file) throws IOException{
        String message = "";

        try {
            String uploadImage = storageService.uploadZImgToSys(file);

            message = "Uploaded the file successfully: " + file.getOriginalFilename();

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));



        }catch (Exception e){
            message = "Could not upload the file: " + file.getOriginalFilename() + " Error: " + e.getMessage();
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
        }
    }

    @GetMapping("/allfiles")

    public ResponseEntity<?> getAllImage() throws IOException{
        List<FileInfo> fileInfoList = storageService.getAllImage();

        return ResponseEntity.status(HttpStatus.OK).body(fileInfoList);
    }




}
