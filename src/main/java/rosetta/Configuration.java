package rosetta;

import org.javafunk.funk.monads.Option;

public interface Configuration {
    Option<String> getEnvironmentProperty(String name);
    Option<String> getProperty(String name);
    Arguments getArguments();
}
