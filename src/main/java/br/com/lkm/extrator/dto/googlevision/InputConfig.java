package br.com.lkm.extrator.dto.googlevision;

public class InputConfig {

	private String content;
	private String mimeType;
	
	public InputConfig(String content, String mimeType) {
		this.content = content;
		this.mimeType = mimeType;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	
}
