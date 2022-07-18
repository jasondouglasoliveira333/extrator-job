package br.com.lkm.extrator.dto;

import java.util.List;

public class CorporationDTO {
	
	private Integer id;
	
	private String name;
	
	private String rootCnpj; 
	
	private int retroativeDays;

	private String certificatePassword;
	
	private String certificateFileName; 

	private Integer sapConfId;
	
	private String sapConfDestinationName;
	
	private String sapConfEntryPoint;
	
	private List<SubsidiaryDTO> subsidiaries;
	
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getRootCnpj() {
		return rootCnpj;
	}

	public void setRootCnpj(String rootCnpj) {
		this.rootCnpj = rootCnpj;
	}

	public int getRetroativeDays() {
		return retroativeDays;
	}

	public void setRetroativeDays(int retroativeDays) {
		this.retroativeDays = retroativeDays;
	}

	public String getCertificatePassword() {
		return certificatePassword;
	}

	public void setCertificatePassword(String certificatePassword) {
		this.certificatePassword = certificatePassword;
	}

	public String getCertificateFileName() {
		return certificateFileName;
	}

	public void setCertificateFileName(String certificateFileName) {
		this.certificateFileName = certificateFileName;
	}

	public Integer getSapConfId() {
		return sapConfId;
	}

	public void setSapConfId(Integer sapConfId) {
		this.sapConfId = sapConfId;
	}

	public String getSapConfDestinationName() {
		return sapConfDestinationName;
	}

	public void setSapConfDestinationName(String sapConfDestinationName) {
		this.sapConfDestinationName = sapConfDestinationName;
	}

	public String getSapConfEntryPoint() {
		return sapConfEntryPoint;
	}

	public void setSapConfEntryPoint(String sapConfEntryPoint) {
		this.sapConfEntryPoint = sapConfEntryPoint;
	}

	public List<SubsidiaryDTO> getSubsidiaries() {
		return subsidiaries;
	}

	public void setSubsidiaries(List<SubsidiaryDTO> subsidiaries) {
		this.subsidiaries = subsidiaries;
	}

	@Override
	public String toString() {
		return "CorporationDTO [id=" + id + ", name=" + name + ", rootCnpj=" + rootCnpj + ", retroativeDays="
				+ retroativeDays + ", certificatePassword=" + certificatePassword + ", certificateFileName="
				+ certificateFileName + ", sapConfId=" + sapConfId + ", sapConfEntryPoint=" + sapConfEntryPoint
				+ ", subsidiaries=" + subsidiaries + "]";
	} 

	
}
