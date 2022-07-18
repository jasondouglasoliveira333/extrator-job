package br.com.lkm.extrator.sap.connector;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoException;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;
import com.sap.conn.jco.JCoRepository;

import br.com.lkm.extrator.entity.Corporation;
import br.com.lkm.extrator.entity.SapConfiguration;
import br.com.lkm.extrator.repository.ParameterRepository;

@Component
public class SAPConnector {
	
	private Logger log = LoggerFactory.getLogger(getClass()); 

	@Value("${extrator.filedownloader.pe_sysid}")
	private String peSysId;
	@Value("${extrator.filedownloader.storeNFSXml}")
	private Boolean storeNFSXml;


	private String fallbackDestination = "RFC_BTP_LK3";
	private String fallbackEntryPoint = "EDOC_BR_RECEIVE_XML";
	
	@Autowired
	private ParameterRepository parameterRepository; 
	
//	public void sendNFECTE(String fileName, byte[] data, String email, Corporation corp) throws Exception {
//		String fallbackS = parameterRepository.findByName("SAP_FALLBACK").get(0).getValue();
//		if (Boolean.valueOf(fallbackS)) {
//			callSapEdoc(data, corp);
//		}else {
//		sendNFECTE(fileName, data, email, corp);
//		}
//
//	}

	@SuppressWarnings("unused")
	private void callPing() throws JCoException {
		JCoDestination destination = JCoDestinationManager.getDestination("RFC_BTP_LK3");// Destination_sanmobile
		
		// make an invocation of STFC_CONNECTION in the backend;
		JCoRepository repo = destination.getRepository();
		JCoFunction stfcConnection = repo.getFunction("STFC_CONNECTION");
		
		JCoParameterList imports = stfcConnection.getImportParameterList();
		imports.setValue("REQUTEXT", "SAP Cloud Platform Connectivity runs with JCo");
		
		stfcConnection.execute(destination);
		JCoParameterList exports = stfcConnection.getExportParameterList();
		String echotext = exports.getString("ECHOTEXT");
		String resptext = exports.getString("RESPTEXT");
		System.out.println("Return of STFC_CONNECTION: echoText" + echotext + " - respText:" + resptext);
	}

	public void callSapEdoc(byte[] data, Corporation corp) throws JCoException {
		log.info("In callSapEdoc");
		JCoDestination destination = null;
		try {
			destination = JCoDestinationManager.getDestination(fallbackDestination);
			JCoContext.begin(destination);
			JCoFunction func = destination.getRepository().getFunction(fallbackEntryPoint);
			func.getImportParameterList().setValue("IV_XML", new String(data));
			log.info("Vai executar");
			func.execute(destination);
			log.info("Executou");
			Object o = func.getExportParameterList().getValue("ET_BAPIRET2");
			System.out.println("o:" + o);
		} finally {
			JCoContext.end(destination);
		}
		log.info("Saindo da callSapEdoc");
	}

	public void sendNFECTE(String fileName, byte[] content, String email, Corporation corp) throws Exception {
		log.info("Enviando XML de NFE/CTE ao SAP...");
		JCoDestination destination = JCoDestinationManager
				.getDestination(corp.getSapConfiguration().getDestinationName());
		JCoContext.begin(destination);
		JCoFunction func = destination.getRepository().getFunction(corp.getSapConfiguration().getEntryPoint());
		StringBuilder sb = new StringBuilder();
		sb.append(Inet4Address.getLocalHost().getHostAddress());
		sb.append("/");
		sb.append(Inet4Address.getLocalHost().getHostName());
		String stXmlParm = (new StringBuilder(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><parm funct=\"EXTRATOR_EMAIL\" email=\"" + email
						+ "\" hostn=\"" + sb + "\" flnam=\"")).append(fileName).append("\"/></root>").toString();

        func.getImportParameterList().setValue("PE_COMMD", "EDOC_BR_RECEIVE_XML");
		func.getImportParameterList().setValue("PE_SYSID", "QAS");
		func.getImportParameterList().setValue("PE_APLIC", "NFE");
		func.getImportParameterList().setValue("PE_ROTIN", "NFE00");
		func.getImportParameterList().setValue("PE_XMLT_PARM", stXmlParm);
		func.getImportParameterList().setValue("PE_XMLT_ENTR", new String(content));
		log.info(">>>>>>>>>>>>>");
		log.info("Vai executar");
		func.execute(destination);
		log.info("Executou");
		log.info(">>>>>>>>>>>>>");
		JCoContext.end(destination);
		log.info("XML de NFE/CTE enviado ao SAP...");
	}
	
	//Will be changed
	public void sendNFSE(String invoices, Corporation corp) throws Exception {
        log.info("Enviando XML de NFSE ao SAP...");
		JCoDestination destination = JCoDestinationManager.getDestination(corp.getSapConfiguration().getDestinationName());
		JCoContext.begin(destination);
		JCoFunction func = destination.getRepository().getFunction(corp.getSapConfiguration().getEntryPoint());
//		func.getImportParameterList().setValue("PE_XMLT_PARM", invoices);
		
		StringBuilder sb = new StringBuilder();
		sb.append(Inet4Address.getLocalHost().getHostAddress());
		sb.append("/");
		sb.append(Inet4Address.getLocalHost().getHostName());
		String stXmlParm = (new StringBuilder(
				"<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><parm funct=\"EXTRATOR_EMAIL\" email=\"" + "jasondouglasoliveira333@gmail.com"
						+ "\" hostn=\"" + sb + "\" flnam=\"")).append("blablabla").append("\"/></root>").toString();

        func.getImportParameterList().setValue("PE_COMMD", "EDOC_BR_RECEIVE_XML");
		func.getImportParameterList().setValue("PE_SYSID", "QAS");
		func.getImportParameterList().setValue("PE_APLIC", "NFE");
		func.getImportParameterList().setValue("PE_ROTIN", "NFE00");
		func.getImportParameterList().setValue("PE_XMLT_PARM", stXmlParm);
		func.getImportParameterList().setValue("PE_XMLT_ENTR", invoices);
		
		log.info(">>>>>>>>>>>>>");
		log.info("Vai executar");
		func.execute(destination);
		log.info("Executou");
		log.info(">>>>>>>>>>>>>");
		JCoContext.end(destination);
        log.info("XML de NFSE enviado ao SAP...");
	}
	
	
    public void sendNFSe(String xml, String fileName, String uf, String municipio, SapConfiguration sapCon) throws Exception {
        log.info("Enviando XML de NFSE ao SAP...");
        if (storeNFSXml) {
        	Files.write(Paths.get("xml_received_" + fileName), xml.getBytes());
        }
        JCoDestination destination = JCoDestinationManager.getDestination(sapCon.getDestinationName());
        JCoContext.begin(destination);
        JCoFunction func = destination.getRepository().getFunction(sapCon.getEntryPoint());
        log.info("Passando Parametros para funcao de retorno SAP...");
        setParametrosRetornoNfse(func, xml, fileName, uf, municipio);
		log.info(">>>>>>>>>>>>>");
		log.info("Vai executar");
        func.execute(destination);
		log.info("Executou");
		log.info(">>>>>>>>>>>>>");
        log.info(func.toXML());
        if (storeNFSXml) {
        	Files.write(Paths.get("xml_function_" + fileName), func.toXML().getBytes());
        }
        JCoContext.end(destination);
        log.info("XML de NFSE enviado ao SAP...");
    }

    private void setParametrosRetornoNfse(JCoFunction fcn, String xml, String fileName, String uf, String municipio) {

        StringBuilder sb = new StringBuilder();
        try {
            sb.append(Inet4Address.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            sb.append("null");
        }
        sb.append("/");
        try {
            sb.append(Inet4Address.getLocalHost().getHostName());
        } catch (UnknownHostException e) {
            sb.append("null");
        }

        try {
            fcn.getImportParameterList().setValue("PE_SYSID", peSysId);
            fcn.getImportParameterList().setValue("PE_APLIC", "NFS");
            fcn.getImportParameterList().setValue("PE_ROTIN", "NFS00");
            if (uf.trim().length() > 0) {
                String xmlParam = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                        + "<parm>"
                        + "<coduf>" + uf + "</coduf>"
                        + "<codmu>" + municipio + "</codmu>"
                        + "</parm>";
                fcn.getImportParameterList().setValue("PE_XMLT_PARM", xmlParam);
            }
            fcn.getImportParameterList().setValue("PE_XMLT_ENTR", xml);
            fcn.getImportParameterList().setValue("PE_COMMD", "XML_UPLD");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
