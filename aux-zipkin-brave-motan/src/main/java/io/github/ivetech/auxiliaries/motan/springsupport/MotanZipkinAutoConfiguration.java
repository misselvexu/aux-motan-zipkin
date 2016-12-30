package io.github.ivetech.auxiliaries.motan.springsupport;

import com.github.kristofa.brave.Brave;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import zipkin.reporter.AsyncReporter;
import zipkin.reporter.okhttp3.OkHttpSender;

/**
 * io.github.ivetech.auxiliaries.motan.filter
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 30/12/2016.
 */
@ComponentScan
public class MotanZipkinAutoConfiguration {

    public static final String BRAVE_ZIPKIN_BEAN_NAME = "_spring-boot-brave-of-zipkin_";

    @Bean(name = BRAVE_ZIPKIN_BEAN_NAME)
    public Brave brave (MotanZipkinConfigProperties zipkinConfigProperties) {
        System.out.println("\t读取配置文件:" + zipkinConfigProperties.toString());
        Brave.Builder builder = new Brave.Builder(zipkinConfigProperties.getApplication())
                .reporter(
                        AsyncReporter
                                .builder(
                                        // thrift 
//                                        LibthriftSender.builder().host(zipkinConfigProperties.getHost()).connectTimeout(60 * 1000).socketTimeout(60 * 1000).build()
                                        
                                        // okhttp3
                                        OkHttpSender.builder().endpoint("http://" + zipkinConfigProperties.getHost() + ":9411/api/v1/spans").compressionEnabled(true).build()
                                )
                                .build()
                );
        Brave brave = builder.build();
        System.out.println("\t初始化 Brave : " + brave);
        
        return brave;
    }


}
