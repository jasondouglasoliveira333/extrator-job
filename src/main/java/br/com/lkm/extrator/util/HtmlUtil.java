package br.com.lkm.extrator.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HtmlUtil {
	
	private static String GREATER_CHAR = ">";
	private static String QUOTE_CHAR = "\"";
	
	public static List<String> extractUrls(List<String> lines) {
		List<String> urls = new ArrayList<>(); //just for remove duplicates
		lines.forEach(line -> {
			int idx = line.indexOf("http");
			while (idx != -1) {
				int idxGE = line.indexOf(GREATER_CHAR, idx);
				int idxQE = line.indexOf(QUOTE_CHAR, idx);
				//sometimes ends with one char and other with other
				if (idxGE == -1) { idxGE = Integer.MAX_VALUE; }
				if (idxQE == -1) { idxQE = Integer.MAX_VALUE; }
				int idxE = Math.min(idxGE, idxQE);
				if (idxE == Integer.MAX_VALUE) {
//					System.out.println("invalid line:" + line);
					break;
				}
				String url = line.substring(idx, idxE);
				url = url.replace("&amp;", "&");
				urls.add(url);
				idx = line.indexOf("http", idx+1);
			}
		});
		return urls;
	}

    public static String extractUrl(String content) {
    	int posicaoInicial = content.toLowerCase().indexOf("<a href");
        if (posicaoInicial > -1) {
            int posicaoFinal = content.toLowerCase().indexOf(".xml");
            if (posicaoFinal > - 1) {
            	String url = content.substring(posicaoInicial + "<a href".length(), posicaoFinal + ".xml".length());
                url = url.replace("./files", "http://www.primaxonline.com.br/issqn/nfea/files");
                url = url.replace("=", "");
                url = url.replace("\"", "");
                return url;
            }
        }
        return null;
    }

}
