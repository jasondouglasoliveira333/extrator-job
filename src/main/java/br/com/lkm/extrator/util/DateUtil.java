package br.com.lkm.extrator.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {
	
	public static String formatDateyyyyMMddHHmmss(Date date) {
		DateFormat SDF_yyyyMMddHHmmss = new SimpleDateFormat("yyyyMMddHHmmss");
		return SDF_yyyyMMddHHmmss.format(date);
	}

	public static String getTimeStampYYMMdd_HHmmss_SSS() {
		DateFormat SDF_YYMMdd_HHmmss_SSS = new SimpleDateFormat("YYMMdd_HHmmss_SSS");
		return SDF_YYMMdd_HHmmss_SSS.format(new Date());
	}
	
	public static String generateDatedFileName() {
        SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMdd_HHmmss");
        return SDF.format(new Date()) + "_NFSe";
	}
	
	public static String generateDatedFileNameXML() {
		return generateDatedFileName() + ".xml";
	}
	
	public static String generateDatedFileNamePDF() {
		return generateDatedFileName() + ".pdf";
	}

}
