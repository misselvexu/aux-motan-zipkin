package io.github.ivetech.auxiliaries.motan.filter;

import com.github.kristofa.brave.*;
import com.weibo.api.motan.core.extension.SpiMeta;
import com.weibo.api.motan.filter.Filter;
import com.weibo.api.motan.rpc.Caller;
import com.weibo.api.motan.rpc.Request;
import com.weibo.api.motan.rpc.Response;
import com.weibo.api.motan.rpc.RpcContext;
import io.github.ivetech.auxiliaries.motan.brave.BraveContextAware;
import io.github.ivetech.auxiliaries.motan.springsupport.MotanZipkinAutoConfiguration;

import java.util.Collection;
import java.util.Collections;

import static com.github.kristofa.brave.IdConversion.convertToLong;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * io.github.ivetech.auxiliaries.motan.filter
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 29/12/2016.
 */
@SpiMeta(name = "zipkinServer")
public class ZipkinMotanServerFilter implements Filter {


    private ServerRequestInterceptor serverRequestInterceptor;
    private ServerResponseInterceptor serverResponseInterceptor;
    private Brave brave;

    public ZipkinMotanServerFilter () {
    }

    @Override
    public Response filter (Caller<?> caller, Request request) {


        try {
            if (brave == null) {
                if (BraveContextAware.getApplicationContext().containsBean(MotanZipkinAutoConfiguration.BRAVE_ZIPKIN_BEAN_NAME)) {
                    brave = (Brave) BraveContextAware.getApplicationContext().getBean(MotanZipkinAutoConfiguration.BRAVE_ZIPKIN_BEAN_NAME);
                    this.serverRequestInterceptor = brave.serverRequestInterceptor();
                    this.serverResponseInterceptor = brave.serverResponseInterceptor();
                } else {
                    return caller.call(request);
                }
            }

            final RpcContext context = RpcContext.getContext();

            System.out.println(context.getRequest().getAttachments().size());
            serverRequestInterceptor.handle(new MotanServerRequestAdapter(caller, request, context));

            // DO MOTAN INVOKER
            Response response = caller.call(request);

            // END
            context.putAttribute(MotanConstants.MOTAN_STATUS_CODE, MotanStats.SUCCESS.name());

            serverResponseInterceptor.handle(new MotanServerResponseAdapter(response, context));

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            return caller.call(request);
        } finally {
//
        }
    }


    static final class MotanServerRequestAdapter implements ServerRequestAdapter {

        private final Caller caller;
        private final Request request;
        private final RpcContext context;

        MotanServerRequestAdapter (Caller caller, Request request, RpcContext context) {
            this.caller = checkNotNull(caller);
            this.request = checkNotNull(request);
            this.context = checkNotNull(context);
        }

        /**
         * Get the trace data from request.
         *
         * @return trace data.
         */
        @Override
        public TraceData getTraceData () {
            String sampled = request.getAttachments().get((BravePropagationKeys.Sampled));
            String parentSpanId = request.getAttachments().get((BravePropagationKeys.ParentSpanId));
            String traceId = request.getAttachments().get((BravePropagationKeys.TraceId));
            String spanId = request.getAttachments().get((BravePropagationKeys.SpanId));

            // Official sampled value is 1, though some old instrumentation send true
            Boolean parsedSampled = sampled != null
                    ? sampled.equals("1") || sampled.equalsIgnoreCase("true")
                    : null;

            if (traceId != null && spanId != null) {
                return TraceData.create(getSpanId(traceId, spanId, parentSpanId, parsedSampled));
            } else if (parsedSampled == null) {
                return TraceData.EMPTY;
            } else if (parsedSampled) {
                // Invalid: The caller requests the trace to be sampled, but didn't pass IDs
                return TraceData.EMPTY;
            } else {
                return TraceData.NOT_SAMPLED;
            }
        }

        /**
         * Gets the span name for request.
         *
         * @return Span name for request.
         */
        @Override
        public String getSpanName () {
            return request.getMethodName();
        }

        /**
         * Returns a collection of annotations that should be added to span
         * for incoming request.
         * <p>
         * Can be used to indicate more details about request next to span name.
         * For example for http requests an annotation containing the uri path could be added.
         *
         * @return Collection of annotations.
         */
        @Override
        public Collection<KeyValueAnnotation> requestAnnotations () {
            String address = caller.getUrl().getHost();
            if (address != null) {
                KeyValueAnnotation remoteAddrAnnotation = KeyValueAnnotation.create(
                        MotanConstants.CLIENT_ADDR, address);
                return Collections.singleton(remoteAddrAnnotation);
            } else {
                return Collections.emptyList();
            }
        }
    }


    static final class MotanServerResponseAdapter implements ServerResponseAdapter {

        final Response response;
        final RpcContext context;

        public MotanServerResponseAdapter (Response response, RpcContext context) {
            this.response = checkNotNull(response);
            this.context = checkNotNull(context);
        }

        @Override
        public Collection<KeyValueAnnotation> responseAnnotations () {
            MotanStats stats = MotanStats.parse(context.getAttribute(MotanConstants.MOTAN_STATUS_CODE));
            return Collections.singletonList(KeyValueAnnotation.create(MotanConstants.MOTAN_STATUS_CODE, stats.name()));
        }

    }


    static SpanId getSpanId (String traceId, String spanId, String parentSpanId, Boolean sampled) {
        return SpanId.builder()
                .traceIdHigh(traceId.length() == 32 ? convertToLong(traceId, 0) : 0)
                .traceId(convertToLong(traceId))
                .spanId(convertToLong(spanId))
                .sampled(sampled)
                .parentId(parentSpanId == null ? null : convertToLong(parentSpanId)).build();
    }

}
