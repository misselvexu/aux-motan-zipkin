package io.github.ivetech.auxiliaries.motan.springsupport;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * io.github.ivetech.auxiliaries.motan.filter
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 30/12/2016.
 */
@Configuration
@ConfigurationProperties(prefix = "motan.zipkin")
public class MotanZipkinConfigProperties {

    /**
     * 地址
     */
    private String host;

    /**
     * 端口
     */
    private String port = "9411";

    private String application;


    public String getApplication () {
        return application;
    }

    public void setApplication (String application) {
        this.application = application;
    }

    public String getHost () {
        return host;
    }

    public void setHost (String host) {
        this.host = host;
    }

    public String getPort () {
        return port;
    }

    public void setPort (String port) {
        this.port = port;
    }

    @Override
    public String toString () {
        return "MotanZipkinConfigProperties{" +
                "host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", application='" + application + '\'' +
                '}';
    }
}
