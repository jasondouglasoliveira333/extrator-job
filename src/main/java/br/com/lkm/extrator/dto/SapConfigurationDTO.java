package br.com.lkm.extrator.dto;

public class SapConfigurationDTO {

	private Integer id;
	private String destinationName;
	private String entrySap;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getEntrySap() {
		return entrySap;
	}

	public void setEntrySap(String entrySap) {
		this.entrySap = entrySap;
	}

}
