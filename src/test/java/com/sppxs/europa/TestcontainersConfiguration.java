package com.sppxs.europa;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.utility.DockerImageName;

@TestConfiguration(proxyBeanMethods = false)
class TestcontainersConfiguration {
    private static final String MYSQL_VERSION = "8.0.41";
    private static final int MYSQL_PORT = 3306;
    private static final String MYSQL_DATABASE = "test";
    private static final String MYSQL_USER = "test";
    private static final String MYSQL_PASSWORD = "test";


    @Bean
    @ServiceConnection
    MySQLContainer<?> mysqlContainer() {
        //return new MySQLContainer<>(DockerImageName.parse("mysql:latest"));
        return new MySQLContainer<>(DockerImageName.parse("mysql").withTag(MYSQL_VERSION))
                .withDatabaseName(MYSQL_DATABASE)
                .withUsername(MYSQL_USER)
                .withPassword(MYSQL_PASSWORD)
                .withExposedPorts(MYSQL_PORT);
    }

}
