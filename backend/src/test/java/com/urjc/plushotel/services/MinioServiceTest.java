package com.urjc.plushotel.services;

import com.urjc.plushotel.exceptions.MinioErrorException;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.errors.MinioException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MinioServiceTest {

    @Mock
    private MinioClient minioClient;

    @InjectMocks
    private MinioService minioService;

    @BeforeEach
    void setup() {
        ReflectionTestUtils.setField(minioService, "bucket", "test-bucket");
    }

    @Test
    void uploadImageSuccessfulTest() throws IOException, MinioException {

        MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("image".getBytes()));
        when(file.getSize()).thenReturn(8L);
        when(file.getContentType()).thenReturn("image/jpeg");

        String fileName = minioService.uploadImage(file);

        assertNotNull(fileName);
        assertEquals("image.jpg", fileName);

        verify(minioClient, times(1)).putObject(any());
    }

    @Test
    void uploadImageMinioExceptionTest() throws MinioException, IOException {

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("image".getBytes()));
        when(file.getSize()).thenReturn(8L);
        when(file.getContentType()).thenReturn("image/jpeg");

        when(minioClient.putObject(any(PutObjectArgs.class))).thenThrow(MinioException.class);

        assertThrows(MinioErrorException.class, () -> minioService.uploadImage(file));

        verify(minioClient, times(1)).putObject(any());
    }

    @Test
    void uploadImageIOExceptionTest() throws IOException, MinioException {

        MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("image.jpg");
        when(file.getInputStream()).thenThrow(IOException.class);

        assertThrows(MinioErrorException.class, () -> minioService.uploadImage(file));

        verify(minioClient, times(0)).putObject(any());
    }

    @Test
    void getImageSuccessfulTest() throws MinioException {

        when(minioClient.getPresignedObjectUrl(any())).thenReturn("image.url");

        String url = minioService.getImageUrl("image.jpg");

        assertNotNull(url);
        assertEquals("image.url", url);

        verify(minioClient, times(1)).getPresignedObjectUrl(any());
    }

    @Test
    void getImageMinioExceptionTest() throws MinioException {

        when(minioClient.getPresignedObjectUrl(any())).thenThrow(MinioException.class);

        assertThrows(MinioErrorException.class, () -> minioService.getImageUrl("image.jpg"));

        verify(minioClient, times(1)).getPresignedObjectUrl(any());
    }

    @Test
    void deleteImageSuccessfulTest() throws MinioException {

        minioService.deleteImage("image.jpg");

        verify(minioClient, times(1)).removeObject(any());
    }

    @Test
    void deleteImageMinioExceptionTest() throws MinioException {

        doThrow(MinioException.class).when(minioClient).removeObject(any(RemoveObjectArgs.class));

        assertThrows(MinioErrorException.class, () -> minioService.deleteImage("image.jpg"));

        verify(minioClient, times(1)).removeObject(any());
    }
}
