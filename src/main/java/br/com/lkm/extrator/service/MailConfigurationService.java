package br.com.lkm.extrator.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.lkm.extrator.converter.MailConfigurationConverter;
import br.com.lkm.extrator.dto.MailConfigurationResponseDTO;
import br.com.lkm.extrator.dto.MailInfoRequestResponseDTO;
import br.com.lkm.extrator.entity.MailConfiguration;
import br.com.lkm.extrator.entity.MailInfo;
import br.com.lkm.extrator.enums.MailConfigurationType;
import br.com.lkm.extrator.entity.Corporation;
import br.com.lkm.extrator.repository.MailConfigurationRepository;
import br.com.lkm.extrator.repository.MailInfoRepository;
import br.com.lkm.extrator.repository.CorporationRepository;

@Service
public class MailConfigurationService {
	
	@Autowired
	private MailConfigurationRepository mailConfigurationRepository;

	@Autowired
	private MailInfoRepository mailInfoRepository;

	@Autowired
	private CorporationRepository corporationRepository;

	public MailConfigurationResponseDTO findByCorporationId(Integer corpId) {
		MailConfigurationResponseDTO mcr = null;
		MailConfiguration mailConfiguration = mailConfigurationRepository.findByCorporationId(corpId);
		if (mailConfiguration != null) {
			mcr = MailConfigurationConverter.convert(mailConfiguration);
		}
		return mcr;
	}

	public void saveMainInfo(Integer userId, MailInfoRequestResponseDTO mainInfo) {
		Corporation corp = corporationRepository.getOne(userId);
		MailConfiguration mc = corp.getMailConfiguration();
		if (mc == null) {//creates a configuration
			mc = new MailConfiguration();
			mc.setMailConfigurationType(MailConfigurationType.OFFICE_365);
			mc.setCorporation(corp);
			mailConfigurationRepository.save(mc);
			mc.setMailInfos(new ArrayList<>());
			mc.setMailOutlookInfos(new ArrayList<>());
		}
		MailInfo mi = MailConfigurationConverter.convert(mainInfo);
		System.out.println("mi:" + mi);
		mi.setMailConfiguration(mc);
		mailInfoRepository.save(mi);
		
		mc.getMailInfos().add(mi);
	}

}
