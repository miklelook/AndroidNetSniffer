package com.zanfou.polaris;

import org.junit.Test;

import java.net.URI;

/**
 * @author shuxin.wei email:weishuxin@maoyan.com
 * @version v1.0.0
 * @date 2019-09-17
 * <p>
 * 各种URI解析结果
 * {@code
 * zanfou://www.zanfou.com/product/detail ---> Scheme:zanfou Host:www.zanfou.com Path:/product/detail
 * zanfou:/www.zanfou.com/product/detail ---> Scheme:zanfou Host:null Path:/www.zanfou.com/product/detail
 * zanfou://product/detail ---> Scheme:zanfou Host:product Path:/detail
 * www.zanfou.com/product/detail ---> Scheme:null Host:null Path:www.zanfou.com/product/detail
 * /product/detail ---> Scheme:null Host:null Path:/product/detail
 * }
 */
public class URIParseTest {

    @Test
    public void testUri() {
        parseUri("zanfou://www.zanfou.com/product/detail");
        parseUri("zanfou:/www.zanfou.com/product/detail");
        parseUri("zanfou://product/detail");
        parseUri("www.zanfou.com/product/detail");
        parseUri("/product/detail");
    }

    public void parseUri(String uriStr) {
        URI uri = URI.create(uriStr);
        System.out.println(uriStr + " ---> Scheme:" + uri.getScheme() + " Host:" + uri.getHost() + " Path:" + uri.getPath());
    }
}
