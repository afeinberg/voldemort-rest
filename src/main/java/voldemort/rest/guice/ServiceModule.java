package voldemort.rest.guice;

import com.google.inject.servlet.ServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.eclipse.jetty.server.Server;
import voldemort.client.StoreClientFactory;
import voldemort.rest.config.RESTConfig;
import voldemort.rest.resource.StatusResource;
import voldemort.rest.resource.StoreResource;
import voldemort.rest.server.RESTServer;

import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

public class ServiceModule extends ServletModule {

    private final RESTConfig config;

    public ServiceModule(RESTConfig config) {
        this.config = config;
    }

    protected void configureServlets() {
        bind(RESTConfig.class).toInstance(config);
        bind(Server.class).toProvider(JettyProvider.class).asEagerSingleton();
        bind(RESTServer.class).asEagerSingleton();

        bind(StoreClientFactory.class).toProvider(VoldemortClientProvider.class).asEagerSingleton();
        bind(StoreResource.class).asEagerSingleton();
        bind(StatusResource.class).asEagerSingleton();

        bind(ObjectMapper.class).asEagerSingleton();
        bind(MessageBodyReader.class).to(JacksonJsonProvider.class).asEagerSingleton();
        bind(MessageBodyWriter.class).to(JacksonJsonProvider.class).asEagerSingleton();

        serve("*").with(GuiceContainer.class);
    }
}
