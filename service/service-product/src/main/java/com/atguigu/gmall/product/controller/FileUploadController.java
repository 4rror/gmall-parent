package com.atguigu.gmall.product.controller;

import com.atguigu.gmall.common.result.Result;
import io.minio.*;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/admin/product")
public class FileUploadController {

    @Value("${minio.endpointUrl}")
    private String endpoint;
    @Value("${minio.accessKey}")
    private String accessKey;
    @Value("${minio.secretKey}")
    private String secretKey;
    @Value("${minio.bucketName}")
    private String bucket;

    @PostMapping("/fileUpload")
    public Result<Object> uploadFile(@RequestBody MultipartFile file) {
        try {
            // Create a minioClient with the MinIO server playground, its access key and secret key.
            MinioClient minioClient =
                    MinioClient.builder()
                            .endpoint(endpoint)
                            .credentials(accessKey, secretKey)
                            .build();

            // bucket if not exist.
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucket).build());
            if (!found) {
                // Make a new bucket
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucket).build());
            }

            // file name
            String filename = System.currentTimeMillis() + UUID.randomUUID().toString().substring(0, 3) + file.getOriginalFilename();
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(filename)
                            .contentType(file.getContentType())
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .build());

            String url = endpoint + "/" + bucket + "/" + filename;

            log.info("upload file url: {}", url);

            return Result.ok(url);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
