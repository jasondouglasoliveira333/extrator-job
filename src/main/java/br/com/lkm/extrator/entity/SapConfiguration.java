package br.com.lkm.extrator.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

//@Data
@Entity
public class SapConfiguration {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	private String destinationName;
	private String entryPoint;
	private String host;
	private String username;
	private String passwd;
	private String sysnr;
	private String client;
	private String lang;

	//if this be through directory
	private String directory;
	
	@OneToOne
	private Corporation corporation;

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

	public String getEntryPoint() {
		return entryPoint;
	}

	public void setEntryPoint(String entryPoint) {
		this.entryPoint = entryPoint;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getSysnr() {
		return sysnr;
	}

	public void setSysnr(String sysnr) {
		this.sysnr = sysnr;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
	}

	public String getLang() {
		return lang;
	}

	public void setLang(String lang) {
		this.lang = lang;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public Corporation getCorporation() {
		return corporation;
	}

	public void setCorporation(Corporation corporation) {
		this.corporation = corporation;
	}

	@Override
	public String toString() {
		return "SapConfiguration [id=" + id + ", destinationName=" + destinationName + ", entryPoint=" + entryPoint
				+ ", host=" + host + ", username=" + username + ", passwd=" + passwd + ", sysnr=" + sysnr + ", client="
				+ client + ", lang=" + lang + ", directory=" + directory + ", corporation=" + corporation + "]";
	}
	
	
}
