package voldemort.rest.config;

import voldemort.utils.ConfigurationException;
import voldemort.utils.Props;
import voldemort.utils.Utils;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

public class RESTConfig {

    private final String boostrapUrls;
    private final int port;

    public RESTConfig(Properties properties) {
        this(new Props(properties));
    }

    public RESTConfig(Props props) {
        this.port = props.getInt("rest.server.port");
        this.boostrapUrls = props.getString("bootstrap.urls");
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

        return new RESTConfig(properties);
    }

    public String getBoostrapUrls() {
        return boostrapUrls;
    }

    public int getPort() {
        return port;
    }
}

