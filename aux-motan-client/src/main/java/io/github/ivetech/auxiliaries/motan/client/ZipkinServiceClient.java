package io.github.ivetech.auxiliaries.motan.client;

import com.weibo.api.motan.config.springsupport.annotation.MotanReferer;
import io.github.ivetech.auxiliaries.motan.api.ZipkinService;
import io.github.ivetech.auxiliaries.motan.api.ZipkinService2;
import io.github.ivetech.auxiliaries.motan.exception.MServiceException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * io.github.ivetech.auxiliaries.motan.client
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 29/12/2016.
 */
@RestController
public class ZipkinServiceClient {

    @MotanReferer
    private ZipkinService zipkinService;
    
    @MotanReferer
    private ZipkinService2 zipkinService2;


    @RequestMapping("/test/{params}")
    public String test (@PathVariable("params") String params) {
        try {
            System.out.println(this.zipkinService2.test2(params));
            return this.zipkinService.test(params);
        } catch (MServiceException e) {
            e.printStackTrace();
        }
        return "NULL";
    }

}
