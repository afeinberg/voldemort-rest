package voldemort.rest.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import voldemort.rest.config.RESTConfig;
import voldemort.rest.server.RESTServer;

public class RESTServerProvider implements Provider<RESTServer> {

    private final RESTConfig config;

    @Inject
    public RESTServerProvider(RESTConfig config) {
        this.config = config;
    }

    public RESTServer get() {
        return new RESTServer(config);
    }
}
