package voldemort.rest.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import voldemort.client.CachingStoreClientFactory;
import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClientFactory;
import voldemort.rest.config.RESTConfig;

import java.io.File;

public class VoldemortClientProvider implements Provider<StoreClientFactory> {

    private final RESTConfig config;

    @Inject
    public VoldemortClientProvider(RESTConfig config) {
        this.config = config;

    }

    public StoreClientFactory get() {
        String propertiesFileName = config.getHome() + File.separator + "config" + File.separator
                                    + "client.properties";
        File propertiesFile = new File(propertiesFileName);

        ClientConfig clientConfig;
        if(propertiesFile.exists())
            clientConfig = new ClientConfig(propertiesFile);
        else
            clientConfig = new ClientConfig();

        clientConfig.setBootstrapUrls(config.getBootstrapUrls())
                    .setEnableLazy(false);
        return new CachingStoreClientFactory(new SocketStoreClientFactory(clientConfig));
    }
}
