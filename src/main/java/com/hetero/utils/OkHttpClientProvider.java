package com.hetero.utils;

import okhttp3.OkHttpClient;
import org.springframework.stereotype.Component;

@Component
public class OkHttpClientProvider {
    private final OkHttpClient client = new OkHttpClient();

    public OkHttpClient getClient() {
        return client;
    }
}
