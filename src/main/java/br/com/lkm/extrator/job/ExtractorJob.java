package br.com.lkm.extrator.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.lkm.extrator.service.ExtractorService;

@Component
public class ExtractorJob {
	
	private Logger log = LoggerFactory.getLogger(getClass()); 
	
	@Autowired
	private ExtractorService extractorService;
	
	@Scheduled(cron = "${extrator.job.cron}")
	public void extractMail() {
		log.info("Inicio processamento do job");
		extractorService.process();
		log.info("Fim processamento do job");
	}
}
