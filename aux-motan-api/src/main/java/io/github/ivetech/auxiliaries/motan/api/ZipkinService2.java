package io.github.ivetech.auxiliaries.motan.api;

import io.github.ivetech.auxiliaries.motan.exception.MServiceException;

/**
 * io.github.ivetech.auxiliaries.motan.api
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 29/12/2016.
 */
public interface ZipkinService2 {

    /**
     * Test Service Api
     *
     * @return return result
     * @throws MServiceException
     */
    public String test2 (String params) throws MServiceException;

}
