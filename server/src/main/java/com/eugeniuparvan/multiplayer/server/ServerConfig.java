package com.eugeniuparvan.multiplayer.server;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by eugeniuparvan on 10/1/16.
 */
public class ServerConfig {

    public static final String MAX_JOINED_ROOMS = "max.joined.rooms";

    private static final String SERVER_PROPERTIES = "server.properties";

    private Properties properties;

    public ServerConfig() throws IOException {
        properties = new Properties();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(SERVER_PROPERTIES);
        if (inputStream != null)
            properties.load(inputStream);
        else
            throw new FileNotFoundException("property file " + SERVER_PROPERTIES + " not found in classpath");
    }

    public String getProperty(String name) {
        return (String) properties.get(name);
    }

}
