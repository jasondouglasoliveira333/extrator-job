package br.com.lkm.extrator.service;

import java.net.URI;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import br.com.lkm.extrator.dto.googlevision.Feature;
import br.com.lkm.extrator.dto.googlevision.GoogleVisionImagePDFRequestDTO;
import br.com.lkm.extrator.dto.googlevision.GoogleVisionImageRequestDTO;
import br.com.lkm.extrator.dto.googlevision.Image;
import br.com.lkm.extrator.dto.googlevision.ImagePDFRequest;
import br.com.lkm.extrator.dto.googlevision.InputConfig;
import br.com.lkm.extrator.dto.googlevision.Request;
import br.com.lkm.extrator.dto.googlevision.Source;

@Service
public class GoogleVisionService {

	@Value("${extrator.google.apiKey}")
	private String apiKey; 
	
	@Value("${extrator.google.vision.image.url}")
	private String googleVisionImage; 
	
	@Value("${extrator.google.vision.image.pdf.url}")
	private String googleVisionImagePDF; 

	public String extractImageText(String url) throws Exception{
		RestTemplate restTemplate = new RestTemplate();
		GoogleVisionImageRequestDTO gvImageRequest = new GoogleVisionImageRequestDTO();
		Request gRequest = new Request();
		gRequest.setImage(new Image(new Source(url)));
		Feature feature = new Feature("TEXT_DETECTION");
		gRequest.setFeatures(Arrays.asList(feature));
		gvImageRequest.setRequests(Arrays.asList(gRequest));
		String googleVisionImageWithAPIKey = googleVisionImage + "?key=" + apiKey; 
		RequestEntity<GoogleVisionImageRequestDTO> request = new RequestEntity<GoogleVisionImageRequestDTO>(gvImageRequest, HttpMethod.POST, new URI(googleVisionImageWithAPIKey));
		String response = restTemplate.postForObject(googleVisionImageWithAPIKey, request, String.class);
		return response;
	}

	public String extractText(String content, String contentType) throws Exception {
		RestTemplate restTemplate = new RestTemplate();
		GoogleVisionImagePDFRequestDTO gvImageRequest = new GoogleVisionImagePDFRequestDTO();
		ImagePDFRequest gRequest = new ImagePDFRequest();
		gRequest.setInputConfig(new InputConfig(content, contentType));
		Feature feature = new Feature("TEXT_DETECTION");
		gRequest.setFeatures(Arrays.asList(feature));
		gvImageRequest.setImagePDFRequests(Arrays.asList(gRequest));
		String googleVisionImagePDFWithAPIKey = googleVisionImagePDF + "?key=" + apiKey; 
		RequestEntity<GoogleVisionImagePDFRequestDTO> request = new RequestEntity<GoogleVisionImagePDFRequestDTO>(gvImageRequest, HttpMethod.POST, new URI(googleVisionImagePDFWithAPIKey));
		String response = restTemplate.postForObject(googleVisionImagePDFWithAPIKey, request, String.class);
		return response;
	}
}
