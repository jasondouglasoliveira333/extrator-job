package br.com.lkm.extrator.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Subsidiary {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	private String cnpj;
	
	private String provinceCode;
	
	private String townhallUrl;
	
	@ManyToOne
	private Corporation corporation;

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

	public Corporation getCorporation() {
		return corporation;
	}

	public void setCorporation(Corporation corporation) {
		this.corporation = corporation;
	} 
	

}
