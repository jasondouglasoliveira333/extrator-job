package br.com.lkm.extrator.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NFSeUberaba {
    
    private static final Logger log = LoggerFactory.getLogger(NFSeUberaba.class);

    public static String[] ObterPDF(String dirDest, String url) {
        String [] retorno = null;
        try {
            String nomeArquivo = "";
            String data, hora;

            java.util.Date agora = new java.util.Date();
            SimpleDateFormat formata = new SimpleDateFormat("yyyyMMdd");
            data = formata.format(agora);							
            formata = new SimpleDateFormat("HHmmss");
            hora = formata.format(agora);
            nomeArquivo = data + "_" + hora + "_NFSe.pdf";
            
            CookieManager cookieManager = new CookieManager();
            cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
            CookieHandler.setDefault(cookieManager);
            List<HttpCookie> listadeCookies;
            HttpCookie cookie;

            URI conexaoURI = new URI(url);
            URL conexaoURL = conexaoURI.toURL();

            HttpURLConnection.setFollowRedirects(true);
            HttpURLConnection conexao = null;
            OutputStream outStream = null;
            try {
                conexao = (HttpURLConnection) conexaoURL.openConnection();
                log.info("Response code = " + conexao.getResponseCode());
                String header = conexao.getHeaderField("location");
                if (header != null)
                	log.info("Redirected to " + header);

                InputStream is = conexao.getInputStream();
                byte[] buffer = IOUtil.readAllBytes(is);
                
                File targetFile = new File(dirDest + nomeArquivo);
                outStream = new FileOutputStream(targetFile);
                outStream.write(buffer);
                
                log.info("Uberaba - Arquivo salvo em: " + dirDest + nomeArquivo);
                
                retorno = new String[2];
                retorno[0] = new String (buffer, "UTF-8");
                retorno[1] = nomeArquivo;
                
            } catch (Exception e) {
            	log.info("EXCEPTION: " + e.getMessage() );
                if( conexao != null ) {
                    log.info("Response code = " + conexao.getResponseCode());
                }
                String header = conexao.getHeaderField("location");
                if (header != null)
                    log.info("Redirected to " + header);

                listadeCookies = cookieManager.getCookieStore().getCookies();
                if( listadeCookies.isEmpty() ) {
                    log.info("-->NO<-- cookie!");
                } else {
                    cookie = listadeCookies.get(0);
                    log.info("COOKIE:\n  Domain: " +
                          cookie.getDomain() + "\n  Name: " +
                          cookie.getName() + "\n  Value: " +
                          cookie.getValue() );
                }
                retorno = null;
            }finally {
            	if (outStream != null) { try { outStream.close(); } catch (Exception e) {}}
            }
        } catch (Exception e) {
            log.error("============> Ocorreu ao tentar obter o XML da URL no formato HTML: " + url, e);
            retorno = null;
        }
        return retorno;
    }
}