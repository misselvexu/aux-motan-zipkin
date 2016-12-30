package io.github.ivetech.auxiliaries.resteasy3.resources.rest;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.jaxrs2.BraveClientRequestFilter;
import com.github.kristofa.brave.jaxrs2.BraveClientResponseFilter;
import io.github.ivetech.auxiliaries.resteasy3.resources.FooResources;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * io.github.ivetech.auxiliaries.resources.rest
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 29/12/2016.
 */
@Repository
@Path("/foo/resources")
public class FooResourcesRest implements FooResources {

    @Autowired
    private Brave brave;

    @Path("/api.xyz")
    @GET
    public Response api () {
        System.out.println("invoke FooResources/api method .");
//http://127.0.0.1:8080/demo/foo/resources/api.xyz
        final FooResources client =
                new ResteasyClientBuilder().build().target("http://127.0.0.1:8081/demo")
                        .register(BraveClientRequestFilter.create(brave))
                        .register(BraveClientResponseFilter.create(brave))
                        .proxy(FooResources.class);

        final Response response = client.call();

        System.out.println("Call Result Status: " + response.getStatus());
        
        return Response.status(Response.Status.OK).type(MediaType.TEXT_PLAIN).entity("api").build();
    }

    @Override
    public Response call () {
        System.out.println("CALL");
        return Response.status(Response.Status.OK).build();
    }
}
