package br.com.lkm.extrator.dto.googlevision;

import java.util.List;

//@Data
public class GoogleVisionImageRequestDTO{
    public List<Request> requests;

	public List<Request> getRequests() {
		return requests;
	}

	public void setRequests(List<Request> requests) {
		this.requests = requests;
	}
    
}

