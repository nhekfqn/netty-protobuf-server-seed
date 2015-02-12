package com.nhekfqn.sample.server.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.nhekfqn.sample.server.persistense.PingDao;
import org.apache.commons.configuration.PropertiesConfiguration;

public class PersistentModule extends AbstractModule {

    private final PropertiesConfiguration propertiesConfiguration;

    public PersistentModule(PropertiesConfiguration propertiesConfiguration) {
        this.propertiesConfiguration = propertiesConfiguration;
    }

    @Override
    protected void configure() {
        bind(String.class).annotatedWith(Names.named("jdbc.url")).toInstance(propertiesConfiguration.getString("jdbc.url"));
        bind(String.class).annotatedWith(Names.named("jdbc.username")).toInstance(propertiesConfiguration.getString("jdbc.username"));
        bind(String.class).annotatedWith(Names.named("jdbc.password")).toInstance(propertiesConfiguration.getString("jdbc.password"));

        bind(PingDao.class);
    }

}
