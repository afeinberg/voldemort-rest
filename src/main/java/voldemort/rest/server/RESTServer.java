package voldemort.rest.server;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.apache.log4j.Logger;
import org.eclipse.jetty.server.Server;
import voldemort.VoldemortException;
import voldemort.rest.config.RESTConfig;
import voldemort.rest.guice.ServiceModule;
import voldemort.server.AbstractService;
import voldemort.server.ServiceType;
import voldemort.utils.Utils;

public class RESTServer extends AbstractService {

    private static final Logger logger = Logger.getLogger(RESTServer.class);

    private final Server jettyServer;

    @Inject
    public RESTServer(Server jettyServer) {
        super(ServiceType.HTTP);
        this.jettyServer = jettyServer;
    }

    @Override
    protected void startInner() {
        logger.info("Starting REST service.");
        long start = System.currentTimeMillis();
        startServer();
        long end = System.currentTimeMillis();
        logger.info("Startup completed in " + (end - start) + " ms.");
    }

    @Override
    protected void stopInner() {
        VoldemortException exc = null;
        try {
            stopServer();
        } catch(VoldemortException e) {
            exc = e;
            logger.error(e);
        }
        logger.info("REST service stopped");

        if(exc != null)
            throw exc;
    }

    public void startServer() {
        try {
            jettyServer.start();
        } catch(Exception e) {
            throw new VoldemortException(e);
        }
    }

    public void stopServer() {
        try {
            jettyServer.stop();
        } catch(Exception e) {
            throw new VoldemortException(e);
        }
    }

    public static void main(String[] args) {
        RESTConfig config = null;
        try {
            if(args.length == 1)
                config = RESTConfig.loadFromHome(args[0]);
            else
                Utils.croak("USAGE: java " + RESTServer.class.getName() + " [home_dir]");
        } catch(Exception e) {
            logger.error(e);
            Utils.croak("Error while loading configuration: " + e.getMessage());
        }
        Injector parentInjector = Guice.createInjector(new ServiceModule(config));

        final RESTServer server = parentInjector.getInstance(RESTServer.class);
        if(!server.isStarted())
            server.start();

        Runtime.getRuntime().addShutdownHook(new Thread() {

            @Override
            public void run() {
                if(server.isStarted())
                    server.stop();
            }
        });
    }
}
