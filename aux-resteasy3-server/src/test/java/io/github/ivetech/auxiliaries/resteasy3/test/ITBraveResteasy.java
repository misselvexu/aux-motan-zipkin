package io.github.ivetech.auxiliaries.resteasy3.test;

import com.github.kristofa.brave.jaxrs2.BraveTracingFeature;
import io.github.ivetech.auxiliaries.resteasy3.context.BraveContextAware;
import io.github.ivetech.auxiliaries.resteasy3.resources.FooResources;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.bio.SocketConnector;
import org.eclipse.jetty.webapp.WebAppContext;
import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;

import javax.ws.rs.core.Response;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class ITBraveResteasy {

    private Server server;
    private ApplicationContext appContext;

    @Before
    public void setup () {
        server = new Server();

        final SocketConnector connector = new SocketConnector();

        connector.setMaxIdleTime(1000 * 60 * 60);
        connector.setPort(8081);
        server.setConnectors(new Connector[]{connector});

        final WebAppContext context = new WebAppContext();
        context.setServer(server);
        context.setContextPath("/demo");
        context.setWar("src/main/webapp");

        server.setHandler(context);

        try {
            server.start();
        } catch (final Exception e) {
            throw new IllegalStateException("Failed to start server.", e);
        }

        appContext = BraveContextAware.getApplicationContext();
    }

    @After
    public void tearDown () throws Exception {
        server.stop();
        server.join();
    }

    @Test
    public void test () throws IOException, InterruptedException {

        // this initialization only needs to be done once per VM
        RegisterBuiltin.register(ResteasyProviderFactory.getInstance());

        // Create our client. The beans below are configured by scanning
        // com.github.kristofa.brave.resteasy3 in our test web.xml.
        ResteasyClient client = new ResteasyClientBuilder()
                .register(appContext.getBean(BraveTracingFeature.class))
                .build();

        FooResources resouce =
                client.target("http://127.0.0.1:8081/demo")
                        .proxy(FooResources.class);

        @SuppressWarnings("unchecked")
        final Response response = resouce.api();
        try {
            assertEquals(200, response.getStatus());

        } finally {
            response.close();
        }

        Thread.sleep(100000);

    }
}
