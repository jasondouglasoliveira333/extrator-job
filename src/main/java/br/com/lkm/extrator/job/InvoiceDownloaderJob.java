package br.com.lkm.extrator.job;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import br.com.lkm.extrator.service.InvoiceDownloadService;

@Component
public class InvoiceDownloaderJob {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private InvoiceDownloadService invouceDownloadService;

//	@Scheduled(cron = "${extrator.invoicedownload.job.cron}")
	public void invoiceDownload() {
		log.info("Inicio processamento do job");
		invouceDownloadService.download();
		log.info("Fim processamento do job");
	}
}
