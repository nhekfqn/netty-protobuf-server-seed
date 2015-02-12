package com.nhekfqn.sample.server.guice;

import com.google.inject.AbstractModule;
import com.google.inject.multibindings.Multibinder;
import com.nhekfqn.sample.server.command.PingCommand;
import com.nhekfqn.sample.server.command.BaseCommand;
import com.nhekfqn.sample.server.command.CommandRegistry;

public class CommandModule extends AbstractModule {

    @Override
    protected void configure() {
        Multibinder<BaseCommand> commandMultibinder = Multibinder.newSetBinder(binder(), BaseCommand.class);
        commandMultibinder.addBinding().to(PingCommand.class);

        bind(CommandRegistry.class);
    }

}
