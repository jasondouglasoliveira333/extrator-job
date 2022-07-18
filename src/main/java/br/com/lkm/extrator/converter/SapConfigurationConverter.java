package br.com.lkm.extrator.converter;

import br.com.lkm.extrator.dto.CorporationDTO;
import br.com.lkm.extrator.entity.SapConfiguration;

public class SapConfigurationConverter {

	public static SapConfiguration convert(CorporationDTO cDTO) {
		SapConfiguration sc = new SapConfiguration();
		sc.setId(cDTO.getSapConfId());
		sc.setDestinationName(cDTO.getSapConfDestinationName());
		sc.setEntryPoint(cDTO.getSapConfEntryPoint());
		return sc;
	}

}
