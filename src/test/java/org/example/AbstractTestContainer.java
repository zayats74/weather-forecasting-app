package org.example;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.DockerImageName;

public abstract class AbstractTestContainer {

    public static final String DB_USER = "postgres";
    public static final String DB_PASSWORD = "postgres";
    public static final String DB_NAME = "postgres";

    static GenericContainer<?> postgres = new GenericContainer<>(
            DockerImageName.parse("postgres:15"))
            .withEnv("POSTGRES_DB", DB_NAME)
            .withEnv("POSTGRES_USER", DB_USER)
            .withEnv("POSTGRES_PASSWORD", DB_PASSWORD)
            .withExposedPorts(5432);

    static {
        postgres.start();
        String dbUrl = String.format("jdbc:postgresql://localhost:%s/%s", postgres.getMappedPort(5432), DB_NAME);
        System.setProperty("DB_URL", dbUrl);
        System.setProperty("DB_USER", DB_USER);
        System.setProperty("DB_PASSWORD", DB_PASSWORD);
    }
}
