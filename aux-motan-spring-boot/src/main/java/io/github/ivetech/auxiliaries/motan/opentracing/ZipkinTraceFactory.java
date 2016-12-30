package io.github.ivetech.auxiliaries.motan.opentracing;

import com.github.kristofa.brave.Brave;
import com.weibo.api.motan.filter.opentracing.TracerFactory;
import io.opentracing.Tracer;
import io.opentracing.impl.BraveTracer;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.libthrift.LibthriftSender;

/**
 * io.github.ivetech.auxiliaries.motan.opentracing
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 30/12/2016.
 */
public class ZipkinTraceFactory implements TracerFactory {

    private BraveTracer braveTracer;
    private Brave brave;

    public ZipkinTraceFactory (String host, String serviceName) {

        Brave.Builder builder = new Brave.Builder(serviceName)
                .reporter(
                        AsyncReporter
                                .builder(
                                        LibthriftSender.builder().host(host).connectTimeout(60 * 1000).socketTimeout(60 * 1000).build())
                                .build()
                );

        brave = builder.build();

        braveTracer = new BraveTracer(builder);
    }

    @Override
    public Tracer getTracer () {
        return braveTracer;
    }

    public Brave getBrave () {
        return brave;
    }
}
