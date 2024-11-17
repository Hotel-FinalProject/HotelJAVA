package com.example.backend.service;

import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;
import software.amazon.awssdk.core.sync.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class S3Service {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    public S3Service(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    //    public void uploadFile(MultipartFile file) throws IOException {
//
//        RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
//
//        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
//                .bucket(bucketName)
//                .key(file.getOriginalFilename())
//                .contentType(file.getContentType())
//                .build();
//
//        PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);
//
//        System.out.println("File uploaded successfully with ETag: " + response.eTag());
//    }
    public String uploadFile(MultipartFile file) throws IOException {
        String fileName = file.getOriginalFilename();

        // PutObjectRequest와 RequestBody를 사용한 업로드
        RequestBody requestBody = RequestBody.fromInputStream(file.getInputStream(), file.getSize());
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(bucketName)
                .key(fileName)
                .contentType(file.getContentType())
                .build();

        PutObjectResponse response = s3Client.putObject(putObjectRequest, requestBody);

        // S3 URL 생성
        String fileUrl = "https://" + bucketName + ".s3.amazonaws.com/" + fileName;

        System.out.println("File uploaded successfully with ETag: " + response.eTag());

        // 파일 URL 반환
        return fileUrl;
    }
}