package br.com.lkm.extrator.util;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpUtil {
	
	public static byte[] downloadUrl(String url) throws Exception {
		InputStream is = null;
		try {
			HttpURLConnection con = (HttpURLConnection) new URL(url).openConnection();
			con.setDoOutput(true);
			con.setDoInput(true);
			con.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/92.0.4515.131 Safari/537.36");
			is = con.getInputStream();
			return IOUtil.readAllBytes(is);
		}finally {
			if (is != null) { try { is.close();} catch (Exception e_) {}}
		}
		
	}

}
