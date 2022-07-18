package br.com.lkm.extrator.dto.googlevision;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

//@Data
public class GoogleVisionImagePDFRequestDTO{
	@JsonProperty("requests")
    public List<ImagePDFRequest> imagePDFRequests;

	public List<ImagePDFRequest> getImagePDFRequests() {
		return imagePDFRequests;
	}

	public void setImagePDFRequests(List<ImagePDFRequest> imagePDFRequests) {
		this.imagePDFRequests = imagePDFRequests;
	}
	
	
}

