/*
   For step-by-step instructions on connecting your Android application to this backend module,
   see "App Engine Java Endpoints Module" template documentation at
   https://github.com/GoogleCloudPlatform/gradle-appengine-templates/tree/master/HelloEndpoints
*/

package com.example.Ajax.myapplication.backend;

import com.example.HttpClient;
import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.ApiNamespace;

import javax.inject.Named;

/**
 * An endpoint class we are exposing
 */
@Api(
        name = "myApi",
        version = "v1",
        namespace = @ApiNamespace(
                ownerDomain = "backend.myapplication.Ajax.example.com",
                ownerName = "backend.myapplication.Ajax.example.com",
                packagePath = ""))
public class MyEndpoint {

    private static final String STATS_URL = "https://dl.dropboxusercontent" +
            ".com/u/20755008/response" + ".json";

    /**
     * A simple endpoint method that takes a name and says Hi back
     */


    @ApiMethod(name = "sayHi")
    public MyBean sayHi(@Named("name") final String name) {
        final MyBean response = new MyBean();
        response.setData("Hi, " + name);

        return response;
    }

    @ApiMethod(name = "getStats")
    public MyBean getStats() {
        final MyBean mean = new MyBean();
        final String data = HttpClient.get(STATS_URL);
        mean.setData(data);
        return mean;
    }

    @ApiMethod(name = "GetAuthorInfoById")
    public MyBean AuthorInfo(@Named("api") final String api, @Named("apikey") final String key, @Named("id") final
    Integer authorId) {
        final MyBean bean = new MyBean();
        final String request = api + "/author/list/" + authorId + "?" + "format=xml&" +
                "key=" + key;
        final String data = HttpClient.get(request);
        bean.setData(data);
        return bean;
    }


}
