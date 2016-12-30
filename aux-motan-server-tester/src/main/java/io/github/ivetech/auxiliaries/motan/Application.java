package io.github.ivetech.auxiliaries.motan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * io.github.ivetech.auxiliaries.motan.server
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 29/12/2016.
 */
@SpringBootApplication
@EnableAutoConfiguration
public class Application {

    public static void main (String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
