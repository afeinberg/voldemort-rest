package voldemort.rest.guice;

import com.google.inject.Inject;
import com.google.inject.Provider;
import voldemort.client.CachingStoreClientFactory;
import voldemort.client.ClientConfig;
import voldemort.client.SocketStoreClientFactory;
import voldemort.client.StoreClientFactory;
import voldemort.rest.config.RESTConfig;

public class VoldemortClientProvider implements Provider<StoreClientFactory> {

    private final RESTConfig config;

    @Inject
    public VoldemortClientProvider(RESTConfig config) {
        this.config = config;

    }
    public StoreClientFactory get() {
        ClientConfig clientConfig = new ClientConfig().setBootstrapUrls(config.getBoostrapUrls())
                                                      .setEnableLazy(false);
        return new CachingStoreClientFactory(new SocketStoreClientFactory(clientConfig));
    }
}
