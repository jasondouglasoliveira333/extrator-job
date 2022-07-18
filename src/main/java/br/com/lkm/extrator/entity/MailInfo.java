package br.com.lkm.extrator.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

//@Data
/**
 * @author jason
 *
 */
@Entity
public class MailInfo {

	@Id
	@GeneratedValue
	private Integer id;
	
	private String host;
	
	private Integer port;

	private String protocol;

	private String username;
	
	private String password;
	
	@ManyToOne
	private MailConfiguration mailConfiguration;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public Integer getPort() {
		return port;
	}

	public void setPort(Integer port) {
		this.port = port;
	}

	public String getProtocol() {
		return protocol;
	}

	public void setProtocol(String protocol) {
		this.protocol = protocol;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public MailConfiguration getMailConfiguration() {
		return mailConfiguration;
	}

	public void setMailConfiguration(MailConfiguration mailConfiguration) {
		this.mailConfiguration = mailConfiguration;
	}

	@Override
	public String toString() {
		return "MailInfo [id=" + id + ", host=" + host + ", port=" + port + ", protocol=" + protocol + ", username="
				+ username + ", password=" + password + ", mailConfiguration=" + mailConfiguration + "]";
	}

	
}
