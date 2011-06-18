package voldemort.rest.resource;

import com.google.inject.Inject;
import org.apache.log4j.Logger;
import voldemort.VoldemortException;
import voldemort.client.StoreClient;
import voldemort.client.StoreClientFactory;
import voldemort.rest.utils.SerializationUtils;
import voldemort.serialization.json.JsonReader;
import voldemort.versioning.ObsoleteVersionException;
import voldemort.versioning.Version;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.StringReader;

@Path("/v1/store")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class StoreResource {

    private static final Logger logger = Logger.getLogger(StoreResource.class);

    private final StoreClientFactory storeClientFactory;

    @Inject
    public StoreResource(StoreClientFactory factory) {
        this.storeClientFactory = factory;
    }

    @GET
    @Path("/{store}/{key}")
    public Response getValue(@PathParam("store") String store,
                             @PathParam("key") String key) {
        StoreClient<Object, Object> client = null;
        try {
            client = storeClientFactory.getStoreClient(store);
        } catch(VoldemortException e) {
            logger.error("Exception when getting a store client", e);
        }

        if(client == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        Object keyDeSer = SerializationUtils.tightenNumericTypes(new JsonReader(new StringReader(key))
                                                                         .read());
        Object value = client.getValue(keyDeSer);
        if(value == null)
            return Response.status(Response.Status.NO_CONTENT).build();
        return Response.ok(value).build();
    }

    @POST
    @Path("/{store}/{key}")
    public Response clobberingPut(@PathParam("store") String store,
                                  @PathParam("key") String key,
                                  String value) {
        StoreClient<Object, Object> client = null;
        try {
            client = storeClientFactory.getStoreClient(store);
        } catch(VoldemortException e) {
            logger.error("Exception when getting a store client", e);
        }

        if(client == null)
            return Response.status(Response.Status.NOT_FOUND).build();
        Object keyDeSer = SerializationUtils.tightenNumericTypes(new JsonReader(new StringReader(key))
                                                                         .read());
        Object valueDeSer = SerializationUtils.tightenNumericTypes(new JsonReader(new StringReader(value))
                                                                           .read());
        Version version = null;
        try {
            version = client.put(keyDeSer, valueDeSer);
        } catch(ObsoleteVersionException ove) {
            logger.warn(ove);
            return Response.status(Response.Status.FORBIDDEN).build();
        } catch(VoldemortException e) {
            logger.error("Error in clobberingPut:", e);
            throw e;
        }

        //VectorClock clock = (VectorClock) version;
        // return Response.ok(VersionUtils.clockToMap(clock)).build();
        return Response.ok().build();
    }
}
