package br.com.lkm.extrator.dto;

import java.util.List;

public class MailConfigurationResponseDTO {

	private String mailConfigurationType;
	private List<MailOutlookInfoResponseDTO> mailOutlookInfos;
	private List<MailInfoRequestResponseDTO> mailInfos;
	
	public String getMailConfigurationType() {
		return mailConfigurationType;
	}
	public void setMailConfigurationType(String mailConfigurationType) {
		this.mailConfigurationType = mailConfigurationType;
	}
	public List<MailOutlookInfoResponseDTO> getMailOutlookInfos() {
		return mailOutlookInfos;
	}
	public void setMailOutlookInfos(List<MailOutlookInfoResponseDTO> mailOutlookInfos) {
		this.mailOutlookInfos = mailOutlookInfos;
	}
	public List<MailInfoRequestResponseDTO> getMailInfos() {
		return mailInfos;
	}
	public void setMailInfos(List<MailInfoRequestResponseDTO> mailInfos) {
		this.mailInfos = mailInfos;
	}

	
}
