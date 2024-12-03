package com.pantrypro.util;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class URLBuilder {

    private String baseUrl;
    private Map<String, String> params = new LinkedHashMap<>();

    public URLBuilder(String baseUrl) {
        this.baseUrl = baseUrl;
    }

    public URLBuilder addQueryParameter(String name, String value) {
        params.put(name, value);
        return this;
    }

    public URLBuilder addPathParameter(String name) {
        baseUrl += "/" + name;
        return this;
    }

    public URI build() {
        if (params.isEmpty()) {
            return URI.create(baseUrl);
        } else {
            StringBuilder url = new StringBuilder(baseUrl);
            // Check if the base URL already has query parameters
            char appendChar = baseUrl.contains("?") ? '&' : '?';
            url.append(appendChar);
            for (Map.Entry<String, String> entry : params.entrySet()) {
                String encodedName = encode(entry.getKey());
                String encodedValue = encode(entry.getValue());
                url.append(encodedName).append("=").append(encodedValue).append("&");
            }
            // Remove the trailing '&'
            url.deleteCharAt(url.length() - 1);
            return URI.create(url.toString());
        }
    }

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // UTF-8 should always be supported; rethrow as unchecked exception
            throw new RuntimeException(e);
        }
    }

}
