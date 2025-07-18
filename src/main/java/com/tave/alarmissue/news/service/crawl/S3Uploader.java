package com.tave.alarmissue.news.service.crawl;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;


import java.io.IOException;

@Service
@RequiredArgsConstructor
public class S3Uploader {

    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public String uploadContent(String content, String fileName) throws IOException {

        byte[]  bytes= content.getBytes(StandardCharsets.UTF_8);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("text/plain; charset=UTF-8");
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        amazonS3Client.putObject(bucket, fileName, inputStream, metadata);
        return amazonS3Client.getUrl(bucket, fileName).toString();
    }
}

