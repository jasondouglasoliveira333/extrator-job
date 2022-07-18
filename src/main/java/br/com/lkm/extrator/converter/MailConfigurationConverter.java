package br.com.lkm.extrator.converter;

import java.util.ArrayList;
import java.util.List;

import br.com.lkm.extrator.dto.MailConfigurationResponseDTO;
import br.com.lkm.extrator.dto.MailInfoRequestResponseDTO;
import br.com.lkm.extrator.dto.MailOutlookInfoResponseDTO;
import br.com.lkm.extrator.entity.MailConfiguration;
import br.com.lkm.extrator.entity.MailInfo;
import br.com.lkm.extrator.entity.MailOutlookInfo;

public class MailConfigurationConverter {

	public static MailConfigurationResponseDTO convert(MailConfiguration mailConfiguration) {
		MailConfigurationResponseDTO mcr = new MailConfigurationResponseDTO();
		mcr.setMailConfigurationType(mailConfiguration.getMailConfigurationType().name());
		List<MailOutlookInfoResponseDTO> moirList = new ArrayList<>();
		for (MailOutlookInfo moi : mailConfiguration.getMailOutlookInfos()) {
			moirList.add(convert(moi));
		}
		mcr.setMailOutlookInfos(moirList);
		List<MailInfoRequestResponseDTO> mirList = new ArrayList<>();
		for (MailInfo mi : mailConfiguration.getMailInfos()) {
			mirList.add(convert(mi));
		}
		mcr.setMailInfos(mirList);
		return mcr;
	}

	private static MailInfoRequestResponseDTO convert(MailInfo mi) {
		MailInfoRequestResponseDTO mir = new MailInfoRequestResponseDTO();
		mir.setId(mi.getId());
		mir.setHost(mi.getHost());
		mir.setPort(mi.getPort());
		mir.setProtocol(mi.getProtocol());
		mir.setUsername(mi.getUsername());
		mir.setPassword(mi.getPassword());
		return mir;
	}

	public static MailInfo convert(MailInfoRequestResponseDTO mirr) {
		MailInfo mi = new MailInfo();
		mi.setId(mirr.getId());
		mi.setHost(mirr.getHost());
		mi.setPort(mirr.getPort());
		mi.setProtocol(mirr.getProtocol());
		mi.setUsername(mirr.getUsername());
		mi.setPassword(mirr.getPassword());
		return mi;
	}


	private static MailOutlookInfoResponseDTO convert(MailOutlookInfo moi) {
		MailOutlookInfoResponseDTO moir = new MailOutlookInfoResponseDTO();
		moir.setId(moi.getId());
		moir.setEmail(moi.getEmail());
		moir.setAccessToken(moi.getAccessToken());
		return moir;
	}

}
