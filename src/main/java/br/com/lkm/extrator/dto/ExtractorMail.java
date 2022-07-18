package br.com.lkm.extrator.dto;

import java.util.Date;
import java.util.List;

//@Data
public class ExtractorMail {
	private String sender;
	private String receiver;
	private String subject;
	private Date date;
	private String body;
	private List<ExtractorAttachment> attachments;
	private Object originalMessage;
	private Object extra;

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getReceiver() {
		return receiver;
	}

	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public List<ExtractorAttachment> getAttachments() {
		return attachments;
	}

	public void setAttachments(List<ExtractorAttachment> attachments) {
		this.attachments = attachments;
	}

	public Object getOriginalMessage() {
		return originalMessage;
	}

	public void setOriginalMessage(Object originalMessage) {
		this.originalMessage = originalMessage;
	}

	public Object getExtra() {
		return extra;
	}

	public void setExtra(Object extra) {
		this.extra = extra;
	}

}
