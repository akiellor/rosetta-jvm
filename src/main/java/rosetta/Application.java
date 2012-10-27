package rosetta;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.jetty.server.Server;

public class Application{
    private final Injector injector;

    public static Application boot(Arguments arguments){
        return new Application(Guice.createInjector(new ApplicationModule(arguments))).boot();
    }

    private Application(Injector injector) {
        this.injector = injector;
    }

    private Application boot() {
        try {
            injector.getInstance(Server.class).start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }
}
