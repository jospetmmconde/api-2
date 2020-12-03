package com.ppm.springboot.web.app;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.auth.AWSSessionCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.auth.profile.internal.AwsProfileNameLoader;
import com.amazonaws.services.secretsmanager.AWSSecretsManager;
import com.amazonaws.services.secretsmanager.AWSSecretsManagerClientBuilder;
import com.amazonaws.services.secretsmanager.model.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.context.event.ApplicationPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import javax.security.auth.login.Configuration;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

public class DatabasePropertiesListener implements ApplicationListener<ApplicationPreparedEvent> {

    private final static String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
    private final static String SPRING_DATASOURCE_URL = "spring.datasource.url";
    private final static String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";

    private ObjectMapper mapper = new ObjectMapper();

    @Override
    public void onApplicationEvent(ApplicationPreparedEvent event) {
        String secretJson = getSecret();
        String dbUser = getString(secretJson, "username");
        String dbPassword = getString(secretJson, "password");
        String dbURL = "jdbc:mysql://" + getString(secretJson, "host") + ":3306/pruebadb1?zeroDateTimeBehavior=convertToNull&useUnicode=true&characterEncoding=UTF-8&serverTimezone=UTC&autoReconnect=true";

        ConfigurableEnvironment environment = event.getApplicationContext().getEnvironment();
        Properties props = new Properties();
        props.put(SPRING_DATASOURCE_USERNAME, dbUser);
        props.put(SPRING_DATASOURCE_PASSWORD, dbPassword);
        props.put(SPRING_DATASOURCE_URL, dbURL);

        environment.getPropertySources().addFirst(new PropertiesPropertySource("aws.secret.manager", props));
    }

    private String getSecret() {

        String secretName = "PPM-dev-mysql";
        String region = "us-east-2";

        // Create a Secrets Manager client
        AWSSecretsManager client  = AWSSecretsManagerClientBuilder.standard()
                .withRegion(region)
                .build();

        // In this sample we only handle the specific exceptions for the 'GetSecretValue' API.
        // See https://docs.aws.amazon.com/secretsmanager/latest/apireference/API_GetSecretValue.html
        // We rethrow the exception by default.

        String secret = null, decodedBinarySecret = null;
        GetSecretValueRequest getSecretValueRequest = new GetSecretValueRequest()
                .withSecretId(secretName);
        GetSecretValueResult getSecretValueResult = null;

        try {
            getSecretValueResult = client.getSecretValue(getSecretValueRequest);
        } catch (DecryptionFailureException e) {
            // Secrets Manager can't decrypt the protected secret text using the provided KMS key.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (InternalServiceErrorException e) {
            // An error occurred on the server side.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (InvalidParameterException e) {
            // You provided an invalid value for a parameter.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (InvalidRequestException e) {
            // You provided a parameter value that is not valid for the current state of the resource.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        } catch (ResourceNotFoundException e) {
            // We can't find the resource that you asked for.
            // Deal with the exception here, and/or rethrow at your discretion.
            throw e;
        }

        // Decrypts secret using the associated KMS CMK.
        // Depending on whether the secret is a string or binary, one of these fields will be populated.
        if (getSecretValueResult.getSecretString() != null) {
            secret = getSecretValueResult.getSecretString();
        }
        else {
            decodedBinarySecret = new String(Base64.getDecoder().decode(getSecretValueResult.getSecretBinary()).array());
        }
        return secret != null ? secret : decodedBinarySecret;
    }

    private String getString(String json, String path) {
        try {
            JsonNode root = mapper.readTree(json);
            return root.path(path).asText();
        } catch (IOException e) {
            return null;
        }
    }

}