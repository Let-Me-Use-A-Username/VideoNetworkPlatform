package com.wahwahnow;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import com.google.api.client.http.*;
import com.google.api.client.http.apache.ApacheHttpTransport;

public class HttpRouter {

    private HttpRequestFactory requestFactory;// = transport.createRequestFactory(new MyInitializer());

    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String PUT = "PUT";
    public static final String DELETE = "DELETE";

    public HttpRouter(){
        requestFactory = new ApacheHttpTransport.Builder().build().createRequestFactory();
    }

    public HttpResponseData sendRequest(String requestMethod, String uri, Map<String, String> headerValues, String contentType, String requestBody) throws IOException {
        HttpRequest request = requestMethod.equals(GET)?
                requestFactory.buildRequest(requestMethod, new GenericUrl(uri), null)
                : requestFactory.buildRequest(requestMethod, new GenericUrl(uri), ByteArrayContent.fromString(contentType,requestBody));


        // Set header
        if(headerValues != null) {
            HttpHeaders headers = request.getHeaders();
            for (String header : headerValues.keySet()) headers.set(header, headerValues.get(header));
            request.setHeaders(headers);
        }

        HttpResponseData httpResponseData = new HttpResponseData();
        try {
            HttpResponse response = request.execute();
            httpResponseData.contentType = response.getContentType();
            httpResponseData.content = response.parseAsString();
            httpResponseData.statusCode = response.getStatusCode();
            httpResponseData.headers = response.getHeaders();
        }catch (HttpResponseException resE){
            httpResponseData.contentType = "unknown";
            httpResponseData.content = resE.getContent();
            httpResponseData.statusCode = resE.getStatusCode();
            httpResponseData.headers = resE.getHeaders();
        }

        return httpResponseData;
    }

}
