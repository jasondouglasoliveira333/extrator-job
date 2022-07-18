package br.com.lkm.extrator.util;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class PropertiesUtill {
	private static Map<String, String> URL_DOWNLOAD_INFO = null;
	
	@Value("${extrator.filedownloader.listaPrefixoURL}")
	private String listaPrefixoURL;
	
	@Value("${extrator.filedownloader.listaPrefixoURLTipoDocumento}")
	private String listaPrefixoURLTipoDocumento;
	
	public Map<String, String> loadInfoUrlTratavel() {
		if (URL_DOWNLOAD_INFO == null) {
			URL_DOWNLOAD_INFO = new HashMap<>(); 
			String[] listaPrefixoURLs = listaPrefixoURL.split(","); 
			String[] listaPrefixoURLTipoDocumentos = listaPrefixoURLTipoDocumento.split(",");
			for (int x=0; x < listaPrefixoURLs.length; x++) {
				URL_DOWNLOAD_INFO.put(listaPrefixoURLs[x].trim(), listaPrefixoURLTipoDocumentos[x].trim());
			}
		}
		return URL_DOWNLOAD_INFO;
	}

}
