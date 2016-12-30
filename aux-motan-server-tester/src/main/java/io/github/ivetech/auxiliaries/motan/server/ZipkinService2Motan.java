package io.github.ivetech.auxiliaries.motan.server;

import com.weibo.api.motan.config.springsupport.annotation.MotanService;
import io.github.ivetech.auxiliaries.motan.api.ZipkinService2;
import io.github.ivetech.auxiliaries.motan.exception.MServiceException;

/**
 * io.github.ivetech.auxiliaries.motan.server
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 29/12/2016.
 */
@MotanService
public class ZipkinService2Motan implements ZipkinService2 {


    /**
     * Test Service Api
     *
     * @return return result
     * @throws MServiceException
     */
    @Override
    public String test2 (String params) throws MServiceException {

        System.out.println(" Motan Invoke : test2() ; params : " + params);

        return params;
    }
}
