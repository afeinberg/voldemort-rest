package voldemort.rest.resource;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/v1")
@Produces(MediaType.TEXT_PLAIN)
@Consumes(MediaType.TEXT_PLAIN)
public class StatusResource {

    @GET
    @Path("/status")
    public Response showStatus() {
        return Response.ok("ok").build();
    }

    @PUT
    @Path("/echo")
    public Response echo(String aString) {
        return Response.ok(aString).build();
    }
}
