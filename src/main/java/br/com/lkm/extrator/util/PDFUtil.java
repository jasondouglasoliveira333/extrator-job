package br.com.lkm.extrator.util;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PDFUtil {
	
	private static final Logger log = LoggerFactory.getLogger(PDFUtil.class); 
	
    public static String[] downloadAndStore(String file, String url) {
        String[] retorno = null;
        try {
            String executavel = new File("C:\\jason\\work\\workspace_old\\ExtratorDeEmails").getAbsolutePath() + "\\addon\\wkhtmltopdf.exe";
            String linhaComando = "\"" + executavel + "\" "
				                    + "\"" + url + "\" "
				                    + "\"" + file + "\"";
            log.info("Arquivo de NFse salvo = " + file);
            log.info("Execução de comando para obter download PDF = " + linhaComando);

            Runtime.getRuntime().exec(linhaComando);

            Thread.sleep(8000); //apparently it take some times
            log.info("Execução de comando de NFSe com sucesso");
        } catch (IOException | InterruptedException e) {
            log.error("============> Ocorreu ao tentar obter o PDF da URL no formato HTML: " + url, e);
        }
        return retorno;
    }

}
