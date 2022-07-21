package com.example.goguma.service;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    @Value("${aws.s3.bucket}")
    private String bucketName;

    private final AmazonS3 s3Client;

    /**
     * aws s3 로 파일 업로드
     *
     * @param file
     * @return
     */
    public String uploadToAWS(MultipartFile file) {
        String key = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {

            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            PutObjectRequest request = new PutObjectRequest(bucketName, key, file.getInputStream(), metadata);
            request.withCannedAcl(CannedAccessControlList.PublicReadWrite); // 접근권한 체크
            PutObjectResult result = s3Client.putObject(request);
            return key;
        } catch (AmazonServiceException e) {
            log.error("uploadToAWS AmazonServiceException filePath={}, error={}",key, e.getMessage());
        } catch (SdkClientException e) {
            log.error("uploadToAWS SdkClientException filePath={}, error={}",key, e.getMessage());
        } catch (Exception e) {
            log.error("uploadToAWS SdkClientException filePath={}, error={}",key, e.getMessage());
        }

        return key;
    }

    public void delete(String fileKey) {
        s3Client.deleteObject(bucketName, fileKey);
    }

    public void rename(String sourceKey, String destinationKey){
        s3Client.copyObject(
                bucketName,
                sourceKey,
                bucketName,
                destinationKey
        );
        s3Client.deleteObject(bucketName, sourceKey);
    }
}
