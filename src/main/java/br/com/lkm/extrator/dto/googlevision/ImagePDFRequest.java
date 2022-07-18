package br.com.lkm.extrator.dto.googlevision;

import java.util.List;

//@Data
public class ImagePDFRequest{
    public InputConfig inputConfig;
    public List<Feature> features;
    
	public InputConfig getInputConfig() {
		return inputConfig;
	}
	public void setInputConfig(InputConfig inputConfig) {
		this.inputConfig = inputConfig;
	}
	public List<Feature> getFeatures() {
		return features;
	}
	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
    
}

