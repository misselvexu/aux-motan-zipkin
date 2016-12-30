package io.github.ivetech.auxiliaries.motan.brave;

import com.github.kristofa.brave.Brave;
import com.github.kristofa.brave.http.DefaultSpanNameProvider;
import com.github.kristofa.brave.http.SpanNameProvider;
import org.springframework.beans.factory.annotation.Value;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.libthrift.LibthriftSender;

//@Configuration
@Deprecated
public class BraveConfig {

//    @Bean
//    @Scope(value = "singleton")
    public Brave brave (@Value("${motan.zipkin.host}") String host,
                        @Value("${motan.zipkin.application}") String serviceName) {

        Brave.Builder builder = new Brave.Builder(serviceName)
                .reporter(
                        AsyncReporter
                                .builder(
                                        LibthriftSender.builder().host(host).connectTimeout(60 * 1000).socketTimeout(60 * 1000).build())
                                .build()
                );
        return builder.build();
    }

//    @Bean
    public SpanNameProvider spanNameProvider () {
        return new DefaultSpanNameProvider();
    }
}
