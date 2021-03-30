package edu.iis.aws.controllers;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
class S3Controller {

    private final AmazonS3 s3;

    S3Controller(BasicSessionCredentials sessionCredentials, Region region) {
        s3 = AmazonS3ClientBuilder.standard()
                                  .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                                  .withRegion(region.getName())
                                  .build();
    }

    @GetMapping(value = "/s3", produces = MediaType.APPLICATION_JSON_VALUE)
    public String getS3Bucket(@RequestParam String bucketName) {
        List<S3ObjectSummary> objectSummaries = s3.listObjects(bucketName)
                                                  .getObjectSummaries();
        return map(objectSummaries);
    }

    private String map(List<S3ObjectSummary> objectSummaries) {
        try {
            return new ObjectMapper().writeValueAsString(objectSummaries);
        } catch (JsonProcessingException e) {
            return "error " + e.getMessage();
        }
    }
}
