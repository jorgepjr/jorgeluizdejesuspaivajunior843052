package com.musiccatalog.service;

import io.minio.*;
import io.minio.http.Method;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@Service
public class MinioService {

    @Value("${minio.bucket}")
    private String bucketName;

    private static final int TRINTA_MINUTOS = 30 * 60;

    private final MinioClient minioClient;

    public MinioService(MinioClient minioClient) {this.minioClient = minioClient;}

    public void upload(MultipartFile arquivo, String nomeArquivo) {
        try (InputStream inputStream = arquivo.getInputStream()) {

            criarBucketSeNaoExistir();

            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(nomeArquivo)
                            .stream(inputStream, arquivo.getSize(), -1)
                            .contentType(arquivo.getContentType())
                            .build()
            );

        } catch (Exception e) {
            throw new RuntimeException("Erro ao salvar arquivo", e);
        }
    }

    public String gerarLinkTemporario(String arquivoHash) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(bucketName)
                        .object(arquivoHash)
                        .expiry(TRINTA_MINUTOS)
                        .build()
        );
    }

    private void criarBucketSeNaoExistir() throws Exception {
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder().bucket(bucketName).build()
        );

        if (!exists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder().bucket(bucketName).build()
            );
        }
    }
}
