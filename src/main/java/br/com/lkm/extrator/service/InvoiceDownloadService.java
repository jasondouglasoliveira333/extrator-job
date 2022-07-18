package br.com.lkm.extrator.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lkm.extrator.entity.Corporation;
import br.com.lkm.extrator.repository.CorporationRepository;
import br.com.lkm.extrator.sap.connector.SAPConnector;
import okhttp3.ResponseBody;
import retrofit2.Response;

@Service
public class InvoiceDownloadService {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private CorporationRepository corporationRepository;
	
	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private SAPConnector sapConnector;

	@Transactional
	public void download() {
		LocalDateTime now = LocalDateTime.now();
		List<Corporation> corps = corporationRepository.findAllRequired();
		
		//This will be threaded
		corps.forEach(corp -> {
			log.info("Processando empresa:" + corp.getName()  + " - now.toLocalDate().toString():" + now.toLocalDate().toString());
			try {
				LocalDateTime startDate = corp.getLastJobExecution();
				corp.getSubsidiaries().forEach(s -> {
					try {
						log.info("s:" + s);
						Response<ResponseBody> r = invoiceService.findInvoices(corp.getRootCnpj(), s.getCnpj(), startDate.toLocalDate(), now.toLocalDate(), 
								s.getProvinceCode(), s.getTownhallUrl()).execute();
						if (!r.isSuccessful()) {
							log.error("r.errorBody()" + r.errorBody().string());
							return;
						}
						String invoices = r.body().string();
						log.info("invoices:" + invoices);
						if (invoices != null) {
							sapConnector.sendNFECTE("retrived_nfse.xml", invoices.getBytes(), "jason_oliviera@lkm.com.br", corp);
						}
					}catch (Exception e) {
						e.printStackTrace();
						throw new RuntimeException(e);
					}
				});
			}catch (Exception e) {
				log.error("Erro processando o download das notas", e);
			}		
		});
	}

}
