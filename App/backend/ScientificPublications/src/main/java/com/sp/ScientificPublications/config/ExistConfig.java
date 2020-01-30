package com.sp.ScientificPublications.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xmldb.api.DatabaseManager;
import org.xmldb.api.base.Database;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
public class ExistConfig {

    private static final String existProperties = "src/main/resources/exist.properties";

    @Bean
    public ConnectionProperties connectionProperties() throws IOException {

        return new ConnectionProperties(getExistProperties());
    }

    @Bean
    public Database database() throws Exception {

        Class<?> cl = Class.forName(getExistProperties().getProperty("conn.driver").trim());

        Database database = (Database) cl.newInstance();
        database.setProperty("create-database", "true");

        DatabaseManager.registerDatabase(database);

        return database;
    }

    private Properties getExistProperties() throws IOException {

        InputStream propStream = new FileInputStream(new File(existProperties));
        Properties props = new Properties();
        props.load(propStream);

        return props;
    }
}
