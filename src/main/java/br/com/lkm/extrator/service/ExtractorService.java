package br.com.lkm.extrator.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sap.conn.jco.JCoContext;
import com.sap.conn.jco.JCoDestination;
import com.sap.conn.jco.JCoDestinationManager;
import com.sap.conn.jco.JCoFunction;
import com.sap.conn.jco.JCoParameterList;

import br.com.lkm.extrator.dto.ExtractorAttachment;
import br.com.lkm.extrator.dto.ExtractorMail;
import br.com.lkm.extrator.entity.Corporation;
import br.com.lkm.extrator.enums.MailConfigurationType;
import br.com.lkm.extrator.repository.CorporationRepository;
import br.com.lkm.extrator.sap.connector.SAPConnector;
import br.com.lkm.extrator.util.ByteUtil;
import br.com.lkm.extrator.util.DateUtil;
import br.com.lkm.extrator.util.FileUtils;
import br.com.lkm.extrator.util.MailBodyService;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;
import okhttp3.RequestBody;

@Service
public class ExtractorService {
	
	private Logger log = LoggerFactory.getLogger(getClass());
	
	private static final String NFE_XML_NS = "http://www.portalfiscal.inf.br/nfe";
	
	@Value("${extrator.job.mail.backup.folder}")
	private String backupFolder;
	
	@Value("${extrator.job.xml.directory}")
	private String xmlDiretory; 
	
	@Value("${extrator.job.other.files.directory}")
	private String otherFileDirectory;

	@Value("${extrator.job.pdf.size.limit}")
	private int pdfSizeLimit;

	@Autowired
	private SimpleMailService simpleMailService;
	
	@Autowired
	private OutlookMailService outlookMailService;
	
	@Autowired
	private SAPConnector sapConnector;

	@Autowired
	private InvoiceService invoiceService;
	
	@Autowired
	private CorporationRepository corporationRepository;
	
	@Autowired
	private MailBodyService mailBodyService; 
	
	
	@Transactional
	public void process() {
		List<Corporation> corps = corporationRepository.findByMailConfigurationNotNull();
		
		//This will be threaded
		corps.forEach(corp -> {
			log.info("Processando empresa:" + corp.getName());
			try {
				List<ExtractorMail> ems = null;
				if (corp.getMailConfiguration().getMailConfigurationType().equals(MailConfigurationType.JAVA_MAIL)) {
					ems = simpleMailService.listMails(corp);
				}else {
					ems = outlookMailService.listMails(corp);
				}
				log.info(" =====> Processando " + ems.size() + " mensage" + ((ems.size() == 1) ? "m" : "ns") + "...");
				for (ExtractorMail em : ems) {
					try {
				        log.info("============> Informações do E-mail");
				        log.info("Assunto do E-mail........: " + em.getSubject());
				        log.info("Remetente do E-mail......: " + em.getSender());
				        log.info("Data do E-mail...........: " + em.getDate());
				        log.info("Tem anexo?...............: " + ((em.getAttachments().size() > 0) ? "Sim" : "Não"));
	
						for (ExtractorAttachment ea : em.getAttachments()) {
					        log.info("============> Informações do anexo");
					        log.info("Nome do arquivo..........: " + ea.getFileName());
					        log.info("Content Type.............: " + ea.getContentType());
					        log.info("Tamanho..................: " + FileUtils.byteCountToDisplaySize(BigInteger.valueOf(ea.getContent().length)));
					        String fillName = ea.getFileName().toLowerCase();
							if (fillName.endsWith(".xml")) {
								if ((ByteUtil.search(ea.getContent(), "<NFe".getBytes()) && ByteUtil.search(ea.getContent(), NFE_XML_NS.getBytes()))   
										|| ByteUtil.search(ea.getContent(), "<cte".getBytes())) {
									handleNFECTE(ea.getFileName(), ea.getContent(), em.getReceiver(), corp);
								}else if (ByteUtil.search(ea.getContent(), "<NFe".getBytes()) || ByteUtil.search(ea.getContent(), "<Nfse".getBytes())){
									handleNFS(ea.getFileName(), ea.getContent(), em.getReceiver(), corp);
								}
							}else if(fillName.endsWith(".pdf")) {
								handlePDF(ea.getFileName(), ea.getContent(), em.getReceiver(), corp);
							}else if(fillName.endsWith(".png")) {
//								String received = DateUtil.formatDateyyyyMMddHHmmss(em.getDate());
//								String timestamp = DateUtil.getTimeStampYYMMdd_HHmmss_SSS();
								handlePDF(ea.getFileName(), ea.getContent(), em.getReceiver(), corp);
							}else if(fillName.endsWith(".zip")) {
								handleZip(ea.getFileName(), ea.getContent(), corp, em.getReceiver(), em.getDate());
							}else {
								String timestamp = DateUtil.getTimeStampYYMMdd_HHmmss_SSS();
								Files.write(Paths.get(otherFileDirectory, timestamp + "_" + ea.getFileName()), ea.getContent());	
							}
						}
						
						if(em.getAttachments().size() == 0) {
							mailBodyService.downloadFiles(Arrays.asList(em.getBody()), em.getReceiver(), corp.getSapConfiguration());
						}
					}catch (Throwable e) {
						log.error("Erro processando mensagem do usuario:" + corp.getName(), e);
					}finally {
						if (corp.getMailConfiguration().getMailConfigurationType().equals(MailConfigurationType.JAVA_MAIL)) {
							simpleMailService.moveToBackup(backupFolder, em);
						}else {
							outlookMailService.moveToBackup(backupFolder, em);
						}
					}
				}
			}catch(Exception e) {
				log.error("Erro processanento mensagem do usuario:" + corp.getName(), e);
			}
		});
	}

	private void handlePDF(String fileName, byte[] content, String email, Corporation corp) throws Exception {
		log.info("In handlePDF");
		log.info("Calling invoice service (ocr).");
		MediaType mt = MediaType.parse(org.springframework.http.MediaType.APPLICATION_PDF_VALUE);
		Part p = Part.createFormData("file", fileName, RequestBody.create(mt, content));
		invoiceService.ocr(p).execute().body();
		log.info("Called invoice service (ocr).");
		Files.write(Paths.get(otherFileDirectory, fileName), content);
	}


	private void handleNFECTE(String fileName, byte[] content, String email, Corporation corp) throws Exception {
		log.info("In handleNFECTE");
		log.info(new String(content));
		Files.write(Paths.get(xmlDiretory, fileName), content);
		sapConnector.sendNFECTE(fileName, content, email, corp);
	}

	private void handleNFS(String fileName, byte[] content, String email, Corporation corp) throws Exception {
		String sContent = new String(content);
		log.info("In handleNFS");
		log.info(sContent);
		log.info("Calling invoice service (validate)");
		Boolean valid = false; //invoiceService.validate(sContent).execute().body();
		log.info("Called invoice service (validate). Return:" + valid);
		Files.write(Paths.get(xmlDiretory, fileName), content);
		sapConnector.sendNFECTE(fileName, content, email, corp);
	}

	@SuppressWarnings("unused")
	private void handlePDF(String fileName, byte[] content, String cnpj, String timestamp, String sender, String received) throws Exception {
		log.info("In handlePDF");
		if (content.length / 1024 <= pdfSizeLimit) {
			Files.write(Paths.get(otherFileDirectory, timestamp + "_" + fileName), content);
			generateMetadataFile(timestamp, fileName, sender, received, cnpj);
			log.info("O arquivo atende aos requisitos de tamanho estabelecidos: " + pdfSizeLimit);
		}else {
			log.info("Arquivo acima dos limites de tamanho estabelecidos: " + pdfSizeLimit);
		}
	}
	
	private void handleZip(String fileName, byte[] content, Corporation corp, String receiver, Date date) {
		log.info("In handleZIP");
		try (ZipInputStream zip = new ZipInputStream(new ByteArrayInputStream(content))){
			byte[] data = new byte[8192];
			int read = 0;
			ZipEntry ze = zip.getNextEntry();
			while (ze != null) {
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				String entryFileName = ze.getName();
				while((read = zip.read(data)) != -1) {
					baos.write(data, 0, read);
				}
				if (entryFileName.toLowerCase().endsWith(".xml")) {
					if ((ByteUtil.search(baos.toByteArray(), "<NFe".getBytes()) && ByteUtil.search(baos.toByteArray(), NFE_XML_NS.getBytes()))   
							|| ByteUtil.search(baos.toByteArray(), "<cte".getBytes())) {
						handleNFECTE(fileName, baos.toByteArray(), receiver, corp);
					}else if (ByteUtil.search(baos.toByteArray(), "<NFe".getBytes()) || ByteUtil.search(baos.toByteArray(), "<Nfse".getBytes())){
						handleNFS(fileName, baos.toByteArray(), receiver, corp);
					}
				}else if (entryFileName.toLowerCase().endsWith(".pdf")) {
					handlePDF(entryFileName, baos.toByteArray(), receiver, corp);
				}else{
					String timestamp = DateUtil.getTimeStampYYMMdd_HHmmss_SSS();
					Files.write(Paths.get(otherFileDirectory, timestamp + "_" + entryFileName), baos.toByteArray());	
				}
				ze = zip.getNextEntry();
			}
			Files.write(Paths.get(otherFileDirectory, fileName), content);
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void generateMetadataFile(String timestamp, String fileName, String from, String received, String cnpj) throws IOException {
		String newPath = otherFileDirectory + File.separator + timestamp + "_" + fileName;
		StringBuilder content = new StringBuilder();
		content.append("Arquivo=" + fileName + "\r\n");
		content.append("Diretório=" + newPath + "\r\n");
		content.append("EmailRemetente=" + from + "\r\n");
		content.append("EmailDtHr=" + received + "\r\n");
		content.append("CNPJ=" + cnpj);

        int index = fileName.lastIndexOf(".");
        if (index != -1) {
        	fileName = fileName.substring(0, index) + ".txt";
        }else {
        	fileName += ".txt"; 
        }
//        Files.writeString(Paths.get(otherFileDirectory, timestamp + "_" + fileName), content, Charset.forName("UTF-8"));
        Files.write(Paths.get(otherFileDirectory, timestamp + "_" + fileName), Arrays.asList(content), Charset.forName("UTF-8"));
	}


	public void processTeste() {
		log.info("In processTeste");
		try {
			log.info("Inicio JCO");
	        JCoDestination destination = JCoDestinationManager.getDestination("RFC_BTP_LK3");
	        JCoContext.begin(destination);
	        JCoFunction func = destination.getRepository().getFunction("STFC_CONNECTION");
	        func.getImportParameterList().setValue("REQUTEXT", "HI FOLKS");
	        log.info("Vai executar");
			func.execute(destination);
			log.info("Executou");
			JCoParameterList exports = func.getExportParameterList();
			String echotext = exports.getString("ECHOTEXT");
			String resptext = exports.getString("RESPTEXT");
			log.info("ECHOTEXT:" + echotext + " - RESPTEXT:" + resptext);
			JCoContext.end(destination);
			log.info("Fim JCO");
		}catch (Exception e) {
			e.printStackTrace();
		}
	}

}
