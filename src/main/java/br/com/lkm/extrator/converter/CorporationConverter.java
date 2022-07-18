package br.com.lkm.extrator.converter;

import br.com.lkm.extrator.dto.CorporationDTO;
import br.com.lkm.extrator.entity.Corporation;

public class CorporationConverter {

	public static CorporationDTO convert(Corporation corp) {
		CorporationDTO cDTO = new CorporationDTO();
		cDTO.setId(corp.getId());
		cDTO.setName(corp.getName());
		cDTO.setRootCnpj(corp.getRootCnpj());
		cDTO.setRetroativeDays(corp.getRetroativeDays());
		cDTO.setSapConfId(corp.getSapConfiguration().getId());
		cDTO.setSapConfDestinationName(corp.getSapConfiguration().getDestinationName());
		cDTO.setSapConfEntryPoint(corp.getSapConfiguration().getEntryPoint());
		return cDTO;
	}

	public static Corporation convert(CorporationDTO cDTO) {
		Corporation corp = new Corporation();
		corp.setId(cDTO.getId());
		corp.setName(cDTO.getName());
		corp.setRootCnpj(cDTO.getRootCnpj());
		corp.setRetroativeDays(cDTO.getRetroativeDays());
		return corp;
	}

	


}
