package it.geosolutions.geoserver;

import it.geosolutions.geoserver.rest.HTTPUtils;
import net.sf.json.JSON;
import org.junit.Test;

/**
 * @author huquanzhi
 * @version 1.0.0
 * @class SeedTest
 * @date 2021/7/16  13:53
 * @Desc Todo
 */

public class SeedTest {
    @Test
    public void mySeedTest() throws Exception {
        JSON asJSON = HTTPUtils.getAsJSON("http://localhost:8080/geoserver/gwc/rest/seed.json", "admin", "geoserver");
        System.out.println(asJSON.toString());
        System.out.println("================================");
        JSON asJSON1 = HTTPUtils.getAsJSON("http://localhost:8080/geoserver/gwc/rest/seed/test1:word.json", "admin", "geoserver");
        System.out.println(asJSON1);
    }

    @Test
    public void myStyleListTest() throws Exception {
        JSON asJSON = HTTPUtils.getAsJSON("http://localhost:8080/geoserver/rest/styles", "admin", "geoserver");
        System.out.println(asJSON.toString());
        System.out.println("================================");
        String get = HTTPUtils.get("http://localhost:8080/geoserver/rest/styles", "admin", "geoserver");
        // JSON asJSON = HTTPUtils.getAsJSON("http://localhost:8080/geoserver/gwc/rest/seed.json", "admin", "geoserver");
        System.out.println(get);
    }
}
