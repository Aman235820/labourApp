package com.example.labourApp.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.lang.NonNull;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnProperty(name = "spring.data.mongodb.uri")
@EnableMongoRepositories(basePackages = "com.example.labourApp.Repository.mongo")
public class MongoConfig extends AbstractMongoClientConfiguration {

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.socket.connect-timeout}")
    private int connectTimeout;

    @Value("${spring.data.mongodb.socket.read-timeout}")
    private int readTimeout;

    @Value("${spring.data.mongodb.server.heartbeat-frequency}")
    private int heartbeatFrequency;

    @Value("${spring.data.mongodb.cluster.server-selection-timeout}")
    private int serverSelectionTimeout;

    @Value("${spring.data.mongodb.connection-pool.max-size}")
    private int maxPoolSize;

    @Value("${spring.data.mongodb.connection-pool.min-size}")
    private int minPoolSize;

    @Value("${spring.data.mongodb.connection-pool.max-wait-time}")
    private int maxWaitTime;

    @Value("${spring.data.mongodb.connection-pool.max-connection-life-time}")
    private int maxConnectionLifeTime;

    @Override
    @NonNull
    protected String getDatabaseName() {
        return databaseName;
    }

    @Override
    @Bean
    @NonNull
    public MongoClient mongoClient() {
        ConnectionString connectionString = new ConnectionString(mongoUri);
        
        MongoClientSettings settings = MongoClientSettings.builder()
                .applyConnectionString(connectionString)
                .applyToSocketSettings(builder -> 
                    builder.connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                           .readTimeout(readTimeout, TimeUnit.MILLISECONDS))
                .applyToServerSettings(builder -> 
                    builder.heartbeatFrequency(heartbeatFrequency, TimeUnit.MILLISECONDS))
                .applyToClusterSettings(builder -> 
                    builder.serverSelectionTimeout(serverSelectionTimeout, TimeUnit.MILLISECONDS))
                .applyToConnectionPoolSettings(builder ->
                    builder.maxSize(maxPoolSize)
                           .minSize(minPoolSize)
                           .maxWaitTime(maxWaitTime, TimeUnit.MILLISECONDS)
                           .maxConnectionLifeTime(maxConnectionLifeTime, TimeUnit.MILLISECONDS))
                .build();

        return MongoClients.create(settings);
    }
} 