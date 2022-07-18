//package br.com.lkm.extrator.util;
//
//import com.gargoylesoftware.htmlunit.WebClient;
//import com.gargoylesoftware.htmlunit.html.HtmlImage;
//import com.gargoylesoftware.htmlunit.html.HtmlPage;
//import com.itextpdf.text.Document;
//import com.itextpdf.text.DocumentException;
//import com.itextpdf.text.Image;
//import com.itextpdf.text.PageSize;
//import com.itextpdf.text.pdf.PdfWriter;
//import java.io.BufferedInputStream;
//import java.io.BufferedReader;
//import java.io.File;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.MalformedURLException;
//import java.net.URL;
//import java.net.URLConnection;
//import java.text.SimpleDateFormat;
//import org.apache.commons.io.IOUtils;
//import org.apache.log4j.Logger;
//
//public class GeradorPDF {
//
//    private static final Logger log = Logger.getLogger(GeradorPDF.class);
//
//    public static void obterPDF(String dirDest, String nomeArquivo, String textoEmail) {
//        File dir = new File(dirDest);
//        if (!dir.exists()) {
//            dir.mkdirs();
//        }
//
//        String url = getUrlParaDownloadNfse(textoEmail);
//
//        try {
//            if (url != null && !url.trim().isEmpty()) {
//                createPdf(new URL(url), new File(dir, nomeArquivo));
//            } else {
//                log.info("============> URL encontrada no e-mail não identificada como sendo URL de Nota Fiscal de Serviço : " + url);
//            }
//        } catch (IOException | NumberFormatException e) {
//            log.info("============> Ocorreu ao tentar obter o PDF da URL: " + url);
//            log.error(e);
//        }
//    }
//
//    public static String[] obterPDFHTML(String dirDest, String url) {
//        String[] retorno = null;
//        try {
//            Thread.sleep(500);
//            String executavel = new File("").getAbsolutePath() + "\\addon\\wkhtmltopdf.exe";
//            String linhaComando;
//            String nomeArquivo;
//
//            String data = "yyyyMMdd";
//            String hora = "HHmmss";
//            String data1, hora1;
//            java.util.Date agora = new java.util.Date();
//            SimpleDateFormat formata = new SimpleDateFormat(data);
//            data1 = formata.format(agora);
//            formata = new SimpleDateFormat(hora);
//            hora1 = formata.format(agora);
//            nomeArquivo = data1 + "_" + hora1 + "_NFSe.pdf";
//
//            linhaComando = "\"" + executavel + "\" "
//                    + "\"" + url + "\" "
//                    + "\"" + dirDest + nomeArquivo + "\"";
//
//            retorno = new String[2];
//            retorno[0] = "";
//            retorno[1] = nomeArquivo;
//            log.info("Arquivo de NFse salvo = " + retorno[1]);
//
//            log.info("Execução de comando para obter download PDF = " + linhaComando);
//
//            Runtime.getRuntime().exec(linhaComando);
//
//            Thread.sleep(8000);
//            log.info("Execução de comando de NFSe com sucesso");
//
//        } catch (IOException | InterruptedException e) {
//            log.info("============> Ocorreu ao tentar obter o PDF da URL no formato HTML: " + url);
//            log.error(e);
//        }
//        return retorno;
//    }
//
//    public static String[] obterXMLDOWNLOAD(String dirDest, String url) {
//        String[] retorno;
//        try {
//            String nomeArquivo;
//
//            String data = "yyyyMMdd";
//            String hora = "HHmmss";
//            String data1, hora1;
//            java.util.Date agora = new java.util.Date();
//            SimpleDateFormat formata = new SimpleDateFormat(data);
//            data1 = formata.format(agora);
//            formata = new SimpleDateFormat(hora);
//            hora1 = formata.format(agora);
//            nomeArquivo = data1 + "_" + hora1 + "_NFSe.xml";
//
//            BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
//            byte[] buffer = new byte[in.available()];
//            in.read(buffer);
//
//            retorno = new String[2];
//            retorno[0] = new String(buffer, "UTF-8");
//            retorno[1] = nomeArquivo;
//
//            log.info("Arquivo de NFse salvo = " + retorno[0]);
//        } catch (IOException e) {
//            log.info("============> Ocorreu ao tentar obter o XML da URL no formato HTML: " + url);
//            log.error(e);
//            retorno = null;
//        }
//        return retorno;
//    }
//
//    public static String[] obterPDFDOWNLOAD(String dirDest, String url) {
//        String[] retorno;
//        try {
//            String nomeArquivo;
//
//            String data = "yyyyMMdd";
//            String hora = "HHmmss";
//            String data1, hora1;
//            java.util.Date agora = new java.util.Date();
//            SimpleDateFormat formata = new SimpleDateFormat(data);
//            data1 = formata.format(agora);
//            formata = new SimpleDateFormat(hora);
//            hora1 = formata.format(agora);
//            nomeArquivo = data1 + "_" + hora1 + "_NFSe.pdf";
//
//            BufferedInputStream in = new BufferedInputStream(new URL(url).openStream());
//            byte[] buffer = new byte[in.available()];
//            in.read(buffer);
//
//            retorno = new String[2];
//            retorno[0] = new String(buffer, "UTF-8");
//            retorno[1] = nomeArquivo;
//
//            log.info("Arquivo de NFse salvo = " + retorno[0]);
//        } catch (IOException e) {
//            log.info("============> Ocorreu ao tentar obter o XML da URL no formato HTML: " + url);
//            log.error(e);
//            retorno = null;
//        }
//        return retorno;
//    }
//
//    public static String[] obterXMLHTML(String dirDest, String url) {
//        String[] retorno = null;
//        String urlEncontrada = "";
//        try {
//            // Obter o conteúdo da URL para obter o link do download...
//            StringBuilder conteudo = new StringBuilder();
//            String dados;
//            URL _url = new URL(url);
//            URLConnection urlConnection = _url.openConnection();
//            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()))) {
//                while ((dados = bufferedReader.readLine()) != null) {
//                    conteudo.append(dados);
//                }
//            }
//            if (conteudo.toString().trim().length() > 0) {
//                // Obter os hyperlinks de download ...
//                String pagina = conteudo.toString().trim();
//
//                int posicaoInicial = pagina.toLowerCase().indexOf("<a href");
//                if (posicaoInicial > -1) {
//                    int posicaoFinal = pagina.toLowerCase().indexOf(".xml");
//                    if (posicaoFinal > - 1) {
//                        urlEncontrada = pagina.substring(posicaoInicial + "<a href".length(), posicaoFinal + ".xml".length());
//                        urlEncontrada = urlEncontrada.replace("./files", "http://www.primaxonline.com.br/issqn/nfea/files");
//                        urlEncontrada = urlEncontrada.replace("=", "");
//                        urlEncontrada = urlEncontrada.replace("\"", "");
//                    }
//                }
//
//                if (urlEncontrada.trim().length() > 0) {
//                    // Thread.sleep(1000);
//                    String nomeArquivo;
//
//                    String data = "yyyyMMdd";
//                    String hora = "HHmmss";
//                    String data1, hora1;
//                    java.util.Date agora = new java.util.Date();
//                    SimpleDateFormat formata = new SimpleDateFormat(data);
//                    data1 = formata.format(agora);
//                    formata = new SimpleDateFormat(hora);
//                    hora1 = formata.format(agora);
//                    nomeArquivo = data1 + "_" + hora1 + "_NFSe.xml";
//
//                    BufferedInputStream in = new BufferedInputStream(new URL(urlEncontrada).openStream());
//                    byte[] buffer = new byte[in.available()];
//                    in.read(buffer);
//
//                    File targetFile = new File(dirDest + nomeArquivo);
//                    OutputStream outStream = new FileOutputStream(targetFile);
//                    outStream.write(buffer);
//
//                    retorno = new String[2];
//                    retorno[0] = new String(buffer, "UTF-8");
//                    retorno[1] = nomeArquivo;
//                    System.out.println("obterXMLHTML = " + retorno[0]);
//                }
//            }
//        } catch (IOException e) {
//            log.info("============> Ocorreu ao tentar obter o XML da URL no formato HTML: " + urlEncontrada);
//            log.error(e.getStackTrace());
//            retorno = null;
//        }
//        return retorno;
//    }
//
//    public static boolean possuiUrlParaDownloadNfse(String textoEmail) {
//        for (PrefixUrlPrefeituras urlPrefeitura : PrefixUrlPrefeituras.values()) {
//            if (textoEmail.contains(urlPrefeitura.getUrl())) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    public static String[] createPdfFromImage(String dirDest, String url) {
//        Document documentoPDF;
//        String[] retorno = null;
//        String nomeArquivo;
//        try {
//            String data = "yyyyMMdd";
//            String hora = "HHmmss";
//            String data1, hora1;
//            java.util.Date agora = new java.util.Date();
//            SimpleDateFormat formata = new SimpleDateFormat(data);
//            data1 = formata.format(agora);
//            formata = new SimpleDateFormat(hora);
//            hora1 = formata.format(agora);
//            nomeArquivo = data1 + "_" + hora1 + "_NFSe.pdf";
//
//            Image imagemNota = Image.getInstance(new URL(url));
//            if (imagemNota != null) {
//                imagemNota.scalePercent(50F);
//                documentoPDF = new Document();
//                PdfWriter.getInstance(documentoPDF, new FileOutputStream(new File(dirDest + nomeArquivo)));
//                documentoPDF.open();
//                documentoPDF.add(imagemNota);
//                documentoPDF.close();
//            } else {
//                log.error("DownloadPDF - Erro - Código de verificação está vazio");
//            }
//            retorno = new String[2];
//            retorno[0] = "";
//            retorno[1] = nomeArquivo;
//            log.info("Arquivo de NFse salvo = " + retorno[1]);
//        } catch (DocumentException | IOException e) {
//            e.printStackTrace();
//        }
//        return retorno;
//    }
//
//    public static void createPdf(URL url, File dest) throws IOException {
//        WebClient webClient = new WebClient();
//
//        try {
//            HtmlPage page = webClient.getPage(url);
//            HtmlImage htmlImage = (HtmlImage) page.getElementById("ctl00_cphBase_img");
//            InputStream is = htmlImage.getWebResponse(true).getContentAsStream();
//
//            Document document = new Document(PageSize.A4, 20, 20, 20, 20);
//
//            FileOutputStream fos = new FileOutputStream(dest);
//
//            Image image = Image.getInstance(IOUtils.toByteArray(is));
//
//            float w = image.getWidth() * 0.478f;
//            float h = image.getHeight() * 0.478f;
//
//            image.scaleAbsolute(w, h);
//
//            float posH = 125;
//            float posW = (PageSize.A4.getWidth() - w) / 2;
//
//            image.setAbsolutePosition(posW, posH);
//
//            PdfWriter writer = PdfWriter.getInstance(document, fos);
//            writer.open();
//            document.open();
//            document.add(image);
//            document.close();
//            writer.close();
//        } catch (DocumentException | IOException e) {
//            e.printStackTrace();
//        }
//    }
//
//    private static String getUrlParaDownloadNfse(String textoEmail) {
//        String url = null;
//
//        for (PrefixUrlPrefeituras urlPrefeitura : PrefixUrlPrefeituras.values()) {
//            if (textoEmail.contains(urlPrefeitura.getUrl())) {
//                url = textoEmail.substring(textoEmail.indexOf(urlPrefeitura.getUrl()), textoEmail.length());
//                if (url.indexOf(" ") > 0) {
//                    url = url.substring(0, url.indexOf(" "));
//                }
//                if (url.indexOf("\n") > 0) {
//                    url = url.substring(0, url.indexOf("\n"));
//                }
//            }
//            return url;
//        }
//        return url;
//    }
//
//    enum PrefixUrlPrefeituras {
//        SAOPAULO_SP("https://nfe.prefeitura.sp.gov.br/nfe.aspx");
//
//        private final String url;
//
//        PrefixUrlPrefeituras(String envUrl) {
//            this.url = envUrl;
//        }
//
//        public String getUrl() {
//            return url;
//        }
//    }
//
//    public static void main(String arg[]) throws MalformedURLException, IOException {
//        String url = "https://nfe.prefeitura.sp.gov.br/nfe.aspx?ccm=27263339&nf=56012&cod=VZVTSNWH";
//        obterPDF("d:\\", "notaFiscal.pdf", url);
//    }
//}
