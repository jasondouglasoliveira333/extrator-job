package br.com.lkm.extrator.entity;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import br.com.lkm.extrator.enums.MailConfigurationType;

//@Data
@Entity
public class MailConfiguration {
	
	@Id
	@GeneratedValue
	private Integer id;

	@Enumerated(EnumType.STRING)
	private MailConfigurationType mailConfigurationType;
	
	@OneToMany(mappedBy = "mailConfiguration")
	private List<MailOutlookInfo> mailOutlookInfos;
	
	@OneToMany(mappedBy = "mailConfiguration")
	private List<MailInfo> mailInfos;
	
	@OneToOne
	private Corporation corporation;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public MailConfigurationType getMailConfigurationType() {
		return mailConfigurationType;
	}

	public void setMailConfigurationType(MailConfigurationType mailConfigurationType) {
		this.mailConfigurationType = mailConfigurationType;
	}

	public List<MailOutlookInfo> getMailOutlookInfos() {
		return mailOutlookInfos;
	}

	public void setMailOutlookInfos(List<MailOutlookInfo> mailOutlookInfos) {
		this.mailOutlookInfos = mailOutlookInfos;
	}

	public List<MailInfo> getMailInfos() {
		return mailInfos;
	}

	public void setMailInfos(List<MailInfo> mailInfos) {
		this.mailInfos = mailInfos;
	}

	public Corporation getCorporation() {
		return corporation;
	}

	public void setCorporation(Corporation corporation) {
		this.corporation = corporation;
	}

	
}
