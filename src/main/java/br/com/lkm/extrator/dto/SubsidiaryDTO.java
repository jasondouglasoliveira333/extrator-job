package br.com.lkm.extrator.dto;

public class SubsidiaryDTO {
	
	private Integer id;
	
	private String cnpj;
	
	private String provinceCode;
	
	private String townhallUrl;
	
	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getCnpj() {
		return cnpj;
	}

	public void setCnpj(String cnpj) {
		this.cnpj = cnpj;
	}

	public String getProvinceCode() {
		return provinceCode;
	}

	public void setProvinceCode(String provinceCode) {
		this.provinceCode = provinceCode;
	}

	public String getTownhallUrl() {
		return townhallUrl;
	}

	public void setTownhallUrl(String townhallUrl) {
		this.townhallUrl = townhallUrl;
	}

	@Override
	public String toString() {
		return "SubsidiaryDTO [id=" + id + ", cnpj=" + cnpj + ", provinceCode=" + provinceCode + ", townhallUrl="
				+ townhallUrl + "]";
	}

	
}
