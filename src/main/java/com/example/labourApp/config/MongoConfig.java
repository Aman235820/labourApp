package com.example.labourApp.config;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.lang.NonNull;

import java.util.concurrent.TimeUnit;

@Configuration
@ConditionalOnProperty(name = "spring.data.mongodb.uri")
@EnableMongoRepositories(basePackages = "com.example.labourApp.Repository.mongo")
public class MongoConfig extends AbstractMongoClientConfiguration {
    private final Environment environment;

    public MongoConfig(Environment environment) {
        this.environment = environment;
    }

    @Value("${spring.data.mongodb.uri}")
    private String mongoUri;

    @Value("${spring.data.mongodb.database}")
    private String databaseName;

    @Value("${spring.data.mongodb.socket.connect-timeout:60000}")
    private int connectTimeout;

    @Value("${spring.data.mongodb.socket.read-timeout:60000}")
    private int readTimeout;

    @Value("${spring.data.mongodb.server.heartbeat-frequency:10000}")
    private int heartbeatFrequency;

    @Value("${spring.data.mongodb.cluster.server-selection-timeout:60000}")
    private int serverSelectionTimeout;

    @Value("${spring.data.mongodb.connection-pool.max-size:100}")
    private int maxPoolSize;

    @Value("${spring.data.mongodb.connection-pool.min-size:5}")
    private int minPoolSize;

    @Value("${spring.data.mongodb.connection-pool.max-wait-time:30000}")
    private int maxWaitTime;

    @Value("${spring.data.mongodb.connection-pool.max-connection-life-time:30000}")
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
        
        MongoClientSettings.Builder settingsBuilder = MongoClientSettings.builder()
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
                           .maxConnectionLifeTime(maxConnectionLifeTime, TimeUnit.MILLISECONDS));

        Boolean sslEnabled = environment.getProperty("spring.data.mongodb.ssl.enabled", Boolean.class);
        Boolean invalidHostNameAllowed = environment.getProperty("spring.data.mongodb.ssl.invalid-host-name-allowed", Boolean.class);
        if (sslEnabled != null || invalidHostNameAllowed != null) {
            settingsBuilder.applyToSslSettings(builder -> {
                if (sslEnabled != null) builder.enabled(sslEnabled);
                if (invalidHostNameAllowed != null) builder.invalidHostNameAllowed(invalidHostNameAllowed);
            });
        }

        MongoClientSettings settings = settingsBuilder.build();

        return MongoClients.create(settings);
    }
} 