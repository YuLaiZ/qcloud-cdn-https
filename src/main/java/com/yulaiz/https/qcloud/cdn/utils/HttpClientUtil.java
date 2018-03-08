package com.yulaiz.https.qcloud.cdn.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by YuLai on 2018/3/5.
 */
public class HttpClientUtil {

    public static String doPost(String uri, Map<String, String> params, Map<String, String> headerMap) throws URISyntaxException, IOException {
        HttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(new URI(uri));
        // set header
        if (null != headerMap && headerMap.size() > 0) {
            for (String key : headerMap.keySet()) {
                request.setHeader(key, headerMap.get(key));
            }
        }
        // set entity
        if (null != params && params.size() > 0) {
            List<NameValuePair> pairs = new ArrayList<NameValuePair>();
            for (String key : params.keySet()) {
                NameValuePair pair = new BasicNameValuePair(key, params.get(key));
                pairs.add(pair);
            }
            HttpEntity entity = new UrlEncodedFormEntity(pairs, "utf-8");
            request.setEntity(entity);
        }
        HttpResponse response = client.execute(request);
        // 这里可以通过statusCode进行一些判断
        // response.getStatusLine().getStatusCode();
        HttpEntity entity = response.getEntity();
        String res = "";

        if (entity != null) {
            res = EntityUtils.toString(entity, "utf-8");
            EntityUtils.consume(entity);
        }
        return res;
    }
}
