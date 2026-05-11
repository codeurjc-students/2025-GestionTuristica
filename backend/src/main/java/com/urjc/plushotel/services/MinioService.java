package com.urjc.plushotel.services;

import com.urjc.plushotel.exceptions.MinioErrorException;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.Http;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.MinioException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@Slf4j
public class MinioService {

    private final MinioClient minioClient;

    @Value("${minio.bucket}")
    private String bucket;

    public MinioService(MinioClient minioClient) {
        this.minioClient = minioClient;
    }

    public String uploadImage(MultipartFile file) {

        try {
            String fileName = file.getOriginalFilename();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1L)
                            .contentType(file.getContentType())
                            .build()
            );

            return fileName;
        } catch (IOException e) {
            log.error("Error IO: ", e);
            throw new MinioErrorException("There was an error obtaining the image");
        } catch (MinioException e) {
            log.error("Error Minio: ", e);
            throw new MinioErrorException("There was an error saving the uploaded image");
        }
    }

    public String getImageUrl(String fileName) {
        try {
            return minioClient.getPresignedObjectUrl(
                    GetPresignedObjectUrlArgs.builder()
                            .method(Http.Method.GET)
                            .bucket(bucket)
                            .object(fileName)
                            .expiry(3600)
                            .build()
            );
        } catch (MinioException e) {
            log.error("Error Minio: ", e);
            throw new MinioErrorException("Error generating url for " + fileName);
        }
    }
}
