package com.nhekfqn.sample.server;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nhekfqn.sample.server.guice.CommandModule;
import com.nhekfqn.sample.server.guice.PersistentModule;
import com.nhekfqn.sample.server.guice.ServerModule;
import com.nhekfqn.sample.server.server.Server;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) throws Exception {
        PropertiesConfiguration propertiesConfiguration;
        try {
            propertiesConfiguration = loadPropertiesConfiguration(args);
        } catch (IllegalArgumentException e) {
            return;
        }

        Injector injector = Guice.createInjector(
                new ServerModule(propertiesConfiguration),
                new CommandModule(),
                new PersistentModule(propertiesConfiguration));

        Server server = injector.getInstance(Server.class);

        server.start();
    }

    private static PropertiesConfiguration loadPropertiesConfiguration(String[] args) throws IllegalArgumentException, ConfigurationException {
        if (args.length > 1) {
            logger.info("USAGE: <path_to_server_properties_file>");
            logger.info("All arguments but first are ignored!");

            throw new IllegalArgumentException();
        }

        if (args.length < 1) {
            logger.info("No server properties file specified!");

            throw new IllegalArgumentException();
        }

        String pathToServerPropertiesFile = args[0];

        final PropertiesConfiguration propertiesConfiguration = new PropertiesConfiguration(pathToServerPropertiesFile);
        propertiesConfiguration.setThrowExceptionOnMissing(true);

        return propertiesConfiguration;
    }

}
