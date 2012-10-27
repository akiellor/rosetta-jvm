package rosetta;

import com.google.inject.AbstractModule;
import org.eclipse.jetty.server.Server;
import org.javafunk.funk.monads.Option;

public class ApplicationModule extends AbstractModule {
    private final Arguments arguments;

    public ApplicationModule(Arguments arguments){
        this.arguments = arguments;
    }

    @Override protected void configure() {
        bind(Server.class).toProvider(ServerProvider.class);
        bind(Configuration.class).toInstance(new Configuration() {
            @Override public Option<String> getEnvironmentProperty(String name) {
                return Option.option(System.getenv().get(name));
            }

            @Override public Option<String> getProperty(String name) {
                return Option.option(System.getProperty(name));
            }

            @Override public Arguments getArguments() {
                return arguments;
            }
        });
    }
}
