package br.com.lkm.extrator.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

//@Data
public class UserInfo {
	private String sub;
	private String name;
	@JsonProperty("family_name")
	private String familyName;
	@JsonProperty("given_name")
	private String givenName;
	private String picture;
	private String email;
	
	public String getSub() {
		return sub;
	}
	public void setSub(String sub) {
		this.sub = sub;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getFamilyName() {
		return familyName;
	}
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}
	public String getGivenName() {
		return givenName;
	}
	public void setGivenName(String givenName) {
		this.givenName = givenName;
	}
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	@Override
	public String toString() {
		return "UserInfo [sub=" + sub + ", name=" + name + ", familyName=" + familyName + ", givenName=" + givenName
				+ ", picture=" + picture + ", email=" + email + "]";
	}
	
	
}
