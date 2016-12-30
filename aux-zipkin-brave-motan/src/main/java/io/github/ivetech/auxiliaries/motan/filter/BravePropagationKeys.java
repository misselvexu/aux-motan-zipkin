package io.github.ivetech.auxiliaries.motan.filter;

/**
 * Metadata keys that allow a span to join across process boundaries.
 */
class BravePropagationKeys {

    public static String ParentSpanId = BraveMotanHeaders.ParentSpanId.getName();
    public static String SpanId = BraveMotanHeaders.SpanId.getName();
    public static String TraceId = BraveMotanHeaders.TraceId.getName();
    public static String Sampled = BraveMotanHeaders.Sampled.getName();


}
