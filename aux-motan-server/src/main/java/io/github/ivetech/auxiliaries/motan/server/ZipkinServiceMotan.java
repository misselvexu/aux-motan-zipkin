package io.github.ivetech.auxiliaries.motan.server;

import com.weibo.api.motan.config.springsupport.annotation.MotanService;
import io.github.ivetech.auxiliaries.motan.api.ZipkinService;
import io.github.ivetech.auxiliaries.motan.exception.MServiceException;

/**
 * io.github.ivetech.auxiliaries.motan.server
 *
 * @author Elve.xu [xuhw@yyft.com]
 * @version v1.0 - 29/12/2016.
 */
@MotanService
public class ZipkinServiceMotan implements ZipkinService {


    /**
     * Test Service Api
     *
     * @return return result
     * @throws MServiceException
     */
    @Override
    public String test (String params) throws MServiceException {

        System.out.println(" Motan Invoke : test() ; params : " + params);

//        ZipkinTraceFactory tracerFactory = (ZipkinTraceFactory) OpenTracingContext.tracerFactory;
//        System.out.println(tracerFactory.getBrave());
        
        // http client 
//        String url = "http://127.0.0.1:8081/demo/foo/resources/api.xyz";
//        HttpClientBuilder builder = HttpClientBuilder.create();
//        builder.addInterceptorFirst(BraveHttpRequestInterceptor.create(brave));
//        builder.addInterceptorLast(BraveHttpResponseInterceptor.create(brave));
//
//        CloseableHttpClient httpClient = builder.build();
//        HttpGet httpGet = new HttpGet(url);
//        try {
//            String result = httpClient.execute(httpGet, new BasicResponseHandler());
//            System.out.println("request remote server.");
//            System.out.println(result);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                httpClient.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

        return params;
    }
}
