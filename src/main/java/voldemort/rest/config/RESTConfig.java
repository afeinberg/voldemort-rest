package voldemort.rest.config;

import voldemort.utils.ConfigurationException;
import voldemort.utils.Props;
import voldemort.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class RESTConfig {

    private final String bootstrapUrls;
    private final String home;
    private final int port;

    public static class Builder {
        private String bootstrapUrls;
        private String home;
        private int port;

        public String getBootstrapUrls() {
            return bootstrapUrls;
        }

        public Builder setBootstrapUrls(String bootstrapUrls) {
            this.bootstrapUrls = bootstrapUrls;
            return this;
        }

        public String getHome() {
            return home;
        }

        public Builder setHome(String home) {
            this.home = home;
            return this;
        }

        public int getPort() {
            return port;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public RESTConfig build() {
            return new RESTConfig(this);
        }
    }

    public RESTConfig(Builder builder) {
        this.bootstrapUrls = builder.getBootstrapUrls();
        this.home = builder.getHome();
        this.port = builder.getPort();

    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public static Builder newBuilderFromProps(Props props) {
        Builder builder = new Builder();
        builder.setBootstrapUrls(props.getString("bootstrap.urls"));
        builder.setPort(props.getInt("rest.server.port"));
        return builder;
    }

    public static Builder newBuilderFromProperties(Properties properties) {
        return newBuilderFromProps(new Props(properties));
    }

    public static RESTConfig loadFromHome(String voldemortRestHome) {
        if(!Utils.isReadableDir(voldemortRestHome))
            throw new ConfigurationException("Attempt to load configuration from VOLDEMORT_REST_HOME, "
                                             + voldemortRestHome
                                             + " failed. That is not a readable directory.");
        String propertiesFile = voldemortRestHome + File.separator + "config" + File.separator
                                + "rest-server.properties";

        Props properties = null;
        try {
            properties = new Props(new File(propertiesFile));
        } catch(IOException e) {
            throw new ConfigurationException(e);
        }

        return RESTConfig.newBuilderFromProps(properties)
                         .setHome(voldemortRestHome)
                         .build();
    }

    public String getBootstrapUrls() {
        return bootstrapUrls;
    }

    public int getPort() {
        return port;
    }

    public String getHome() {
        return home;
    }
}

