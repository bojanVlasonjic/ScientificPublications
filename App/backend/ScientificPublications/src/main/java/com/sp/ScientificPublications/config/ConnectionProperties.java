package com.sp.ScientificPublications.config;

import lombok.Getter;
import lombok.Setter;

import java.util.Properties;

@Getter
@Setter
public class ConnectionProperties {

    private String host;
    private int port;
    private String user;
    private String password;
    private String driver;
    private String uri;

    private static final String connectionUri = "xmldb:exist://%1$s:%2$s/exist/xmlrpc";


    public ConnectionProperties(Properties properties) {

        user = properties.getProperty("conn.user").trim();
        password = properties.getProperty("conn.password").trim();

        host = properties.getProperty("conn.host").trim();
        port = Integer.parseInt(properties.getProperty("conn.port"));

        uri = String.format(connectionUri, host, port);

        driver = properties.getProperty("conn.driver").trim();
    }

}
