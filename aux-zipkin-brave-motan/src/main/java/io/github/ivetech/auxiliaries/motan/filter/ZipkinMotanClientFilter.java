package io.github.ivetech.auxiliaries.motan.filter;

import com.github.kristofa.brave.*;
import com.github.kristofa.brave.internal.Nullable;
import com.twitter.zipkin.gen.Endpoint;
import com.twitter.zipkin.gen.Span;
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

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * io.github.ivetech.auxiliaries.motan.filter
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 29/12/2016.
 */
@SpiMeta(name = "zipkinClient")
public class ZipkinMotanClientFilter implements Filter {


    private volatile ClientRequestInterceptor clientRequestInterceptor;
    private volatile ClientResponseInterceptor clientResponseInterceptor;
    private volatile ClientSpanThreadBinder clientSpanThreadBinder;
    private volatile Brave brave;

    public ZipkinMotanClientFilter () {
    }


    @Override
    public Response filter (Caller<?> caller, Request request) {

        if (brave == null) {
            if (BraveContextAware.getApplicationContext().containsBean(MotanZipkinAutoConfiguration.BRAVE_ZIPKIN_BEAN_NAME)) {
                brave = (Brave) BraveContextAware.getApplicationContext().getBean(MotanZipkinAutoConfiguration.BRAVE_ZIPKIN_BEAN_NAME);
                this.clientRequestInterceptor = brave.clientRequestInterceptor();
                this.clientResponseInterceptor = brave.clientResponseInterceptor();
                this.clientSpanThreadBinder = brave.clientSpanThreadBinder();
            } else {
                return caller.call(request);
            }
        }


        RpcContext context = RpcContext.getContext();
        Span currentClientSpan = null;
        Response response = null;
        try {

            clientRequestInterceptor.handle(new MotanClientRequestAdapter(caller, request, context));
            currentClientSpan = clientSpanThreadBinder.getCurrentClientSpan();

            // DO SERVICE INVOKER
            response = caller.call(request);
            // END
            context.putAttribute(MotanConstants.MOTAN_STATUS_CODE, MotanStats.SUCCESS.name());
            clientSpanThreadBinder.setCurrentSpan(currentClientSpan);
            clientResponseInterceptor.handle(new MotanClientResponseAdapter(response, context));

            return response;
        } catch (Exception e) {
            e.printStackTrace();

            context.putAttribute(MotanConstants.MOTAN_STATUS_CODE, MotanStats.EXCEPTION.name());
            clientSpanThreadBinder.setCurrentSpan(currentClientSpan);
            clientResponseInterceptor.handle(new MotanClientResponseAdapter(response, context));

            return caller.call(request);
        } finally {
            if (response != null) {
                if (context.getAttribute(MotanConstants.MOTAN_STATUS_CODE).equals(MotanStats.SUCCESS)) {
                    clientSpanThreadBinder.setCurrentSpan(null);
                }
            }
        }
    }


    static final class MotanClientRequestAdapter implements ClientRequestAdapter {

        private final Caller caller;
        private final Request request;
        private final RpcContext context;

        public MotanClientRequestAdapter (Caller caller, Request request, RpcContext context) {
            this.caller = checkNotNull(caller);
            this.request = checkNotNull(request);
            this.context = checkNotNull(context);
        }

        @Override
        public String getSpanName () {
            System.out.println(request.getMethodName());
            return request.getMethodName();
        }

        @Override
        public void addSpanIdToRequest (@Nullable SpanId spanId) {
            if (spanId == null) {
                request.getAttachments().put(BravePropagationKeys.Sampled, "0");
            } else {
                request.getAttachments().put(BravePropagationKeys.Sampled, "1");
                request.getAttachments().put(BravePropagationKeys.TraceId, spanId.traceIdString());
                request.getAttachments().put(BravePropagationKeys.SpanId, IdConversion.convertToString(spanId.spanId));
                if (spanId.nullableParentId() != null) {
                    context.putAttribute(BravePropagationKeys.ParentSpanId, IdConversion.convertToString(spanId.parentId));
                }
            }
        }

        @Override
        public Collection<KeyValueAnnotation> requestAnnotations () {
            return Collections.emptyList();
        }

        @Override
        public Endpoint serverAddress () {
            return null;
        }
    }

    static final class MotanClientResponseAdapter implements ClientResponseAdapter {

        private final Response response;
        private final RpcContext context;

        public MotanClientResponseAdapter (Response response, RpcContext context) {
            this.response = checkNotNull(response);
            this.context = checkNotNull(context);
        }

        @Override
        public Collection<KeyValueAnnotation> responseAnnotations () {
            MotanStats stats = MotanStats.parse(context.getAttribute(MotanConstants.MOTAN_STATUS_CODE));
            return Collections.singletonList(KeyValueAnnotation.create(MotanConstants.MOTAN_STATUS_CODE, stats.name()));
        }
    }

}
