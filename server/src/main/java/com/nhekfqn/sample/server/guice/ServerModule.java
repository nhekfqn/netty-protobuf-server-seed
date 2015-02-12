package com.nhekfqn.sample.server.guice;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import com.nhekfqn.sample.server.server.Server;
import com.nhekfqn.sample.server.server.ServerChannelInitializer;
import io.netty.channel.ChannelInitializer;
import org.apache.commons.configuration.PropertiesConfiguration;

public class ServerModule extends AbstractModule {

    private final PropertiesConfiguration propertiesConfiguration;

    public ServerModule(PropertiesConfiguration propertiesConfiguration) {
        this.propertiesConfiguration = propertiesConfiguration;
    }

    @Override
    protected void configure() {
        bind(Integer.class).annotatedWith(Names.named("port")).toInstance(propertiesConfiguration.getInt("port"));

        bind(ChannelInitializer.class).to(ServerChannelInitializer.class);

        bind(Server.class);
    }

}
