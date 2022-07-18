package br.com.lkm.extrator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.lkm.extrator.entity.Corporation;
import br.com.lkm.extrator.entity.SapConfiguration;
import br.com.lkm.extrator.sap.connector.SAPConnector;

@CrossOrigin
@RestController
@RequestMapping("/api/extrator/invoices")
public class InvoiceControler {

	private static final Logger log = LoggerFactory.getLogger(InvoiceControler.class);
	
	@Autowired
	private SAPConnector sapConnector;
	
	@PostMapping("sendSAP")
	private ResponseEntity<?> sendSap(@RequestBody String invoices) {
		log.info("In sendSap:" + invoices);
		try {
			//Mock Corporation
			Corporation corp = new Corporation();
			SapConfiguration sc = new SapConfiguration();
			sc.setDestinationName("BTP_H19");
			sc.setEntryPoint("/LKMT/COM_JCO_SAP");
			corp.setSapConfiguration(sc);
			sapConnector.sendNFSE(invoices, corp);
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			log.error("Error send the xml to SAP", e);
			return ResponseEntity.badRequest().build();
		}
	}
}
