package edu.iis.aws;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.QueueMessageHandlerFactory;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.handler.annotation.support.PayloadMethodArgumentResolver;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;

@Configuration
public class AwsConfiguration {

    @Value("${cloud.aws.credentials.access-key}")
    private String key;

    @Value("${cloud.aws.credentials.secret-key}")
    private String secret;
    @Value("${cloud.aws.credentials.session-token}")
    private String token;

    @Value("${cloud.aws.region.static}")
    private String awsRegion;

    @Bean(name = "sessionCredentials")
    public BasicSessionCredentials sessionCredentials() {

        return new BasicSessionCredentials(key, secret, token);
    }

    @Bean(name = "awsRegion")
    public Region awsRegion() {
        return Region.getRegion(Regions.fromName(awsRegion));
    }

    @Primary
    @Bean
    public AmazonSQSAsync amazonSQSAsync(BasicSessionCredentials sessionCredentials) {
        return AmazonSQSAsyncClientBuilder.standard()
                                          .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                                          .withRegion(Regions.fromName(awsRegion))
                                          .build();
    }

    @Primary
    @Bean
    public AmazonSNS amazonSNS(BasicSessionCredentials sessionCredentials) {
        return AmazonSNSClientBuilder.standard()
                                     .withCredentials(new AWSStaticCredentialsProvider(sessionCredentials))
                                     .withRegion(Regions.fromName(awsRegion))
                                     .build();
    }

    @Bean
    public QueueMessageHandlerFactory queueMessageHandlerFactory() {
        QueueMessageHandlerFactory factory = new QueueMessageHandlerFactory();
        MappingJackson2MessageConverter messageConverter = new MappingJackson2MessageConverter();
        messageConverter.setStrictContentTypeMatch(false);
        factory.setArgumentResolvers(Collections.singletonList(new PayloadMethodArgumentResolver(messageConverter)));
        return factory;
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    public NotificationMessagingTemplate notificationMessagingTemplate(AmazonSNS amazonSNS) {
        return new NotificationMessagingTemplate(amazonSNS);
    }
}
