package br.com.lkm.extrator.service;

import java.util.List;

import br.com.lkm.extrator.dto.ExtractorMail;
import br.com.lkm.extrator.entity.Corporation;

public interface MailService {
	
	public List<ExtractorMail> listMails(Corporation corp) throws Exception;

	public void moveToBackup(String backupFolder, ExtractorMail em) throws Exception;
	
}
