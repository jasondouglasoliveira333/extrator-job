package br.com.lkm.extrator.service;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

@Configuration
public class InvoiceServiceBuilder {

	@Value("${extrator.invoice.api.base.url}")
	private String invoiceServiceApiUrl;
	
	@Bean
	public InvoiceService createService() {
	
	    OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
	    clientBuilder.readTimeout(5, TimeUnit.MINUTES);
	    clientBuilder.writeTimeout(5, TimeUnit.MINUTES);
	    OkHttpClient client = clientBuilder.build();
	
	    // Create and configure the Retrofit object
	    Retrofit retrofit = new Retrofit.Builder()
	            .baseUrl(invoiceServiceApiUrl)
	            .client(client)
	            .addConverterFactory(JacksonConverterFactory.create())
	            .build();
	
	    // Generate the token service
	    return retrofit.create(InvoiceService.class);
	}
}
