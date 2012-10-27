package rosetta;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.webapp.WebAppContext;

import javax.inject.Inject;
import javax.inject.Provider;
import java.net.URL;
import java.security.ProtectionDomain;

public class ServerProvider implements Provider<Server> {
    private final Configuration configuration;

    @Inject
    public ServerProvider(Configuration configuration){
        this.configuration = configuration;
    }

    @Override public Server get() {
        Server server = new Server(configuration.getArguments().port());

        ProtectionDomain domain = Application.class.getProtectionDomain();
        URL location = domain.getCodeSource().getLocation();

        HandlerCollection handlers = new HandlerCollection();

        WebAppContext webapp = new WebAppContext();
        webapp.setContextPath("/");
        webapp.setDescriptor(location.toExternalForm() + "/WEB-INF/web.xml");
        webapp.setServer(server);
        webapp.setWar(location.toExternalForm());

        handlers.setHandlers(new Handler[] {webapp});

        server.setHandler(handlers);

        return server;
    }
}
