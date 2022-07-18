package br.com.lkm.extrator.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

//@Data
@Entity
public class Corporation {
	
	@Id
	@GeneratedValue
	private Integer id;
	
	private String name;
	
	private String rootCnpj; 
	
	private int retroativeDays;
	
	private LocalDateTime lastJobExecution;

	@OneToOne(mappedBy = "corporation")
	private MailConfiguration mailConfiguration;
	
	@OneToOne(mappedBy = "corporation")
	private SapConfiguration sapConfiguration;

	@OneToMany(mappedBy = "corporation")
	private List<Subsidiary> subsidiaries;
	
	
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

	public LocalDateTime getLastJobExecution() {
		return lastJobExecution;
	}

	public void setLastJobExecution(LocalDateTime lastJobExecution) {
		this.lastJobExecution = lastJobExecution;
	}

	public MailConfiguration getMailConfiguration() {
		return mailConfiguration;
	}

	public void setMailConfiguration(MailConfiguration mailConfiguration) {
		this.mailConfiguration = mailConfiguration;
	}

	public SapConfiguration getSapConfiguration() {
		return sapConfiguration;
	}

	public void setSapConfiguration(SapConfiguration sapConfiguration) {
		this.sapConfiguration = sapConfiguration;
	}

	public List<Subsidiary> getSubsidiaries() {
		return subsidiaries;
	}

	public void setSubsidiaries(List<Subsidiary> subsidiaries) {
		this.subsidiaries = subsidiaries;
	} 
	
}
