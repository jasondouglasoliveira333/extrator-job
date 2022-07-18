package br.com.lkm.extrator.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;

//@Data
@Entity
public class MailOutlookInfo {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	@Column(columnDefinition = "LONGVARCHAR")
	private String accessToken;
	@Column(columnDefinition = "LONGVARCHAR")
	private String refreshToken;
	private LocalDateTime expiration;
	private String email;
	
	@OneToOne
	private MailConfiguration mailConfiguration;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public LocalDateTime getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDateTime expiration) {
		this.expiration = expiration;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public MailConfiguration getMailConfiguration() {
		return mailConfiguration;
	}

	public void setMailConfiguration(MailConfiguration mailConfiguration) {
		this.mailConfiguration = mailConfiguration;
	}

	@Override
	public String toString() {
		return "MailOutlookInfo [id=" + id + ", accessToken=" + accessToken + ", refreshToken=" + refreshToken
				+ ", expiration=" + expiration + ", email=" + email + ", mailConfiguration=" + mailConfiguration + "]";
	}
	
	
}
