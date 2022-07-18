package br.com.lkm.extrator.dto.googlevision;

import java.util.List;

public class Request{
    public Image image;
    public List<Feature> features;
	public Image getImage() {
		return image;
	}
	public void setImage(Image image) {
		this.image = image;
	}
	public List<Feature> getFeatures() {
		return features;
	}
	public void setFeatures(List<Feature> features) {
		this.features = features;
	}
    
    
}

