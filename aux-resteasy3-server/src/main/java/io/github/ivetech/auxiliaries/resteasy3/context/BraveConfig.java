package io.github.ivetech.auxiliaries.resteasy3.context;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;
import com.github.kristofa.brave.http.SpanNameProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.libthrift.LibthriftSender;

@Configuration
public class BraveConfig {

    @Autowired
    private ApplicationConfig applicationConfig;

    @Bean
    @Scope(value = "singleton")
    public Brave brave () {

        String zipkinServer = applicationConfig.environment.getProperty("zipkin.server");
        String serviceName = applicationConfig.environment.getProperty("application.service.name");


        Brave.Builder builder = new Brave.Builder(serviceName)
                .reporter(
                        AsyncReporter
                                .builder(
                                        LibthriftSender.builder().host(zipkinServer).connectTimeout(60 * 1000).socketTimeout(60 * 1000).build())
                                .build()
                );
        
        return builder.build();
    }

    @Bean
    public SpanNameProvider spanNameProvider () {
        return new DefaultSpanNameProvider();
    }
}
