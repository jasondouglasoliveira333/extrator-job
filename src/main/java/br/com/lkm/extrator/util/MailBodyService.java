package br.com.lkm.extrator.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.lkm.extrator.entity.SapConfiguration;
import br.com.lkm.extrator.sap.connector.SAPConnector;

@Service
public class MailBodyService {
	
	private static Logger log = LoggerFactory.getLogger(MailBodyService.class);
	
	@Value("${extrator.filedownloader.nfseDirPath}")
	private String nfseDirPath; 
	@Value("${extrator.filedownloader.cnpj}")
	private String cnpj; 
	
	@Autowired
	private PropertiesUtill propertiesUtill;
	
	@Autowired
	private SAPConnector sapConnector;
	
	public void downloadFiles(List<String> bodies, String from, SapConfiguration sapCon) {

        Set<String> urls = new HashSet<>();
        //Extrai url dos corpo do email.
        for (String body : bodies) {
            List<String> lines = Arrays.asList(body.split("\r"));
            List<String> urlsBody = HtmlUtil.extractUrls(lines);
            urls.addAll(urlsBody);
        }

        //resolve the barueri's email problem
        List<String> urlsClean = new ArrayList<>();
        for (String url : urls) {
        	int idx = url.lastIndexOf("http"); 
        	if (idx != 0) {
        		url = url.substring(idx);
        	}
        	urlsClean.add(url);
        }
        
    	urlsClean.forEach(url -> {
    		try {
            	if ((!url.contains("notacarioca") || isNotaCariocaValid(url))) {
            		String tipoDocumento =  getTipoDocumento(url);
            		if (tipoDocumento != null) {
            			tipoDocumento = tipoDocumento.toUpperCase();
                        log.info("URL encontrada para download de documento = " + url);

                        if (url.toUpperCase().contains("UBERABA")) {
                            String[] dados = NFSeUberaba.ObterPDF(nfseDirPath, url);
                            if (dados != null) {
                                Date sentDate = new Date();
                                gerarArquivoLog(from, sentDate, dados[0], nfseDirPath, dados[1]);
                            }
                        } else if (tipoDocumento.equals("XMLDOWNLOAD")) {
                        	byte[] data = HttpUtil.downloadUrl(url);
                        	String fileName = DateUtil.generateDatedFileNameXML();
                        	sapConnector.sendNFSe(new String(data), fileName, "35", "05708", sapCon);
                        } else if (tipoDocumento.equals("XMLHTML")) {
                            byte[] dataPage = HttpUtil.downloadUrl(url);
							String urlDownload = HtmlUtil.extractUrl(new String(dataPage));
							byte[] data = HttpUtil.downloadUrl(urlDownload);
							String fileName = DateUtil.generateDatedFileNameXML();
							Files.write(Paths.get(nfseDirPath,  fileName), data);
							sapConnector.sendNFSe(new String(data), fileName, "35", "13108", sapCon);
                        } else if (tipoDocumento.equals("PDF")) {
                            String fileName = DateUtil.generateDatedFileNamePDF();
                            PDFUtil.downloadAndStore(nfseDirPath + fileName, url);
                            gerarArquivoLog(from, new Date(), "", nfseDirPath, fileName);
                        } else if (tipoDocumento.toUpperCase().equals("PDFDOWNLOAD")) {
                        	byte[] data = HttpUtil.downloadUrl(url);
                        	String fileName = DateUtil.generateDatedFileNameXML();
                        	gerarArquivoLog(from, new Date(), new String(data), nfseDirPath, fileName);
//                            String[] dados= GeradorPDF.obterPDFDOWNLOAD(path, url);
//                            if (new File(path + dados[1]).exists()) {
//                                Date sentDate = new Date();
//                                GerarArquivoNFSeTXT(from, sentDate, dados[0], path, dados[1]);
//                            }
                        }
            		}
            	}
        		} catch (Exception e) {
        			log.error("processContent - Erro ao localizar urls = ", e);
        		}
        });
		
	}
	
	private String getTipoDocumento(String url) {
    	Map<String, String> urlsInfo = propertiesUtill.loadInfoUrlTratavel();
    	for (String urlInfo : urlsInfo.keySet()) {
    		if (url.contains(urlInfo)) {
    			return urlsInfo.get(urlInfo);
    		}
    	}
		return null;
	}

	private static boolean isNotaCariocaValid(String url) {
        String urlLower = url.trim().toLowerCase(); 
    	if (urlLower.contains("ccm") && urlLower.contains("nf") && urlLower.contains("cod")) {
	        int idx = urlLower.indexOf("cod=");
	        if (idx > -1) {
	            String cod = url.substring(idx);
	            if (cod.length() > 12) {
	                url = url.substring(0, idx);
	                url = url.concat(cod.substring(0, 12));
	            }
	        }
            return true;
        }
		return false;
	}

	private void gerarArquivoLog(String from, Date sendDate, String xml, String dir, String fileName) {
        try {
            log.info("");
            String txtFile = fileName.toUpperCase().replace(".PDF", ".TXT");

            String file = dir + txtFile;

            String received = new SimpleDateFormat("yyyyMMddHHmmss").format(sendDate);

            String sb = "Arquivo=" + fileName + "\r\n"
                    + "Diretório=" + dir+ "\r\n"
                    + "EmailRemetente=" + from + "\r\n"
                    + "EmailDtHr=" + received + "\r\n"
                    + "CNPJ=" + cnpj;
            Files.write(Paths.get(file), sb.getBytes("UTF-8"));
        } catch (Exception e) {
            log.error("Erro = GerarArquivoNFSeTXT - " + e.getMessage());
        }
    }
	

}
