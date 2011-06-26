package voldemort.rest.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.servlet.GuiceFilter;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import voldemort.rest.config.RESTConfig;


public class JettyProvider implements Provider<Server> {

    private final RESTConfig config;

    @Inject
    public JettyProvider(RESTConfig config) {
        this.config = config;
    }

    public Server get() {
        Server server = new Server(config.getPort());
        ServletContextHandler root = new ServletContextHandler(server,
                                                               "/",
                                                               ServletContextHandler.NO_SESSIONS);

        root.addFilter(GuiceFilter.class, "/*", 0);
        root.addServlet(DefaultServlet.class, "/*");

        return server;
    }
}
