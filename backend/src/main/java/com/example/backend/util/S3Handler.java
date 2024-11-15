//package com.example.backend.util;
//
//import com.amazonaws.auth.AWSStaticCredentialsProvider;
//import com.amazonaws.auth.BasicAWSCredentials;
//import com.amazonaws.regions.Regions;
//import com.amazonaws.services.s3.AmazonS3;
//import com.amazonaws.services.s3.AmazonS3ClientBuilder;
//import com.amazonaws.services.s3.model.ObjectMetadata;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.List;
//import java.util.UUID;
//
//@Component
//public class S3Handler {
//
//    private final AmazonS3 s3Client;
//
//    @Value("${cloud.aws.s3.bucket}")
//    private String bucketName;
//
//    public S3Handler(
//            @Value("${cloud.aws.access-key}") String accessKey,
//            @Value("${cloud.aws.secret-key}") String secretKey) {
//        BasicAWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
//        this.s3Client = AmazonS3ClientBuilder.standard()
//                .withRegion(Regions.AP_NORTHEAST_2)
//                .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
//                .build();
//    }
//
//    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
//        List<String> fileUrls = new ArrayList<>();
//
//        for (MultipartFile file : files) {
//            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
//            ObjectMetadata metadata = new ObjectMetadata();
//            metadata.setContentLength(file.getSize());
//
//            s3Client.putObject(bucketName, fileName, file.getInputStream(), metadata);
//            String fileUrl = s3Client.getUrl(bucketName, fileName).toString();
//            fileUrls.add(fileUrl);
//        }
//
//        return fileUrls;
//    }
//}
