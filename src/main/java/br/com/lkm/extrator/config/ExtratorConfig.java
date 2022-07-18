package br.com.lkm.extrator.config;

import java.io.File;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ExtratorConfig {

	private Logger log = LoggerFactory.getLogger(getClass()); 

	@Value("${extrator.job.xml.directory}")
	private String xmlDiretory; 
	
	@Value("${extrator.job.other.files.directory}")
	private String otherFileDirectory;
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}
	
	@PostConstruct
	public void createDirectories() {
		try {
			File xmlDiretoryDir = new File(xmlDiretory);
			if (!xmlDiretoryDir.exists()) {
				xmlDiretoryDir.mkdirs();
			}
			File otherFileDirectoryDir = new File(otherFileDirectory);
			if (!otherFileDirectoryDir.exists()) {
				otherFileDirectoryDir.mkdirs();
			}
			log.info("Diretprios criados");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
