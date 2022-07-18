package br.com.lkm.extrator.service;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Request.Builder;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class OutlookServiceBuilder {

    public static OutlookService getOutlookService(String accessToken) {
        
        // Create a request interceptor to add headers that belong on every request
        Interceptor requestInterceptor = (Interceptor.Chain chain) -> {
            Request original = chain.request();
            Builder builder = original.newBuilder()
                    .header("Authorization", String.format("Bearer %s", accessToken))
                    .method(original.method(), original.body());
            
            Request request = builder.build();
            return chain.proceed(request);
        };

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .build();

        // Create and configure the Retrofit object
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://graph.microsoft.com")
                .client(client)
                .addConverterFactory(JacksonConverterFactory.create())
                .build();

        // Generate the token service
        return retrofit.create(OutlookService.class);
    }
}
