package br.com.lkm.extrator.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class StringUtil {
	
	public static String appendBase64(String xml, byte[] encoded) {
		int index = xml.lastIndexOf("</");
		StringBuilder sb = new StringBuilder();
		sb.append(xml.subSequence(0, index));
		sb.append("<base64>");
		sb.append(new String(encoded));
		sb.append("</base64>");
		sb.append(xml.substring(index));
		return sb.toString();
	}

	public static String extratCompleteText(String content) {
		int index = content.lastIndexOf("\"text\": ");
		int endIndex = content.indexOf("}", index);
		String completeText = content.substring(index+9, endIndex);
		return completeText;
	}
	
	public static void main(String... args) {
		try {
			byte[] data = Files.readAllBytes(Paths.get("C:\\jason\\work\\atividades\\hack2build\\OCR_full.txt"));
			System.out.println(extratCompleteText(new String(data)));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	

}
