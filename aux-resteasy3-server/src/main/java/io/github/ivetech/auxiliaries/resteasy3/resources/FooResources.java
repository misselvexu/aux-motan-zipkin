package io.github.ivetech.auxiliaries.resteasy3.resources;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

/**
 * io.github.ivetech.auxiliaries.api
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 29/12/2016.
 */
@Path("/foo/resources")
public interface FooResources {

    @Path("/api.xyz")
    @GET
    Response api ();

    @Path("/call.xyz")
    @GET
    Response call();
}
