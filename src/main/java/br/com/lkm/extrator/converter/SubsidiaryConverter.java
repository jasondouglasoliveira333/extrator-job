package br.com.lkm.extrator.converter;

import br.com.lkm.extrator.dto.SubsidiaryDTO;
import br.com.lkm.extrator.entity.Subsidiary;

public class SubsidiaryConverter {

	public static SubsidiaryDTO convert(Subsidiary s) {
		SubsidiaryDTO sDTO = new SubsidiaryDTO();
		sDTO.setId(s.getId());
		sDTO.setCnpj(s.getCnpj());
		sDTO.setProvinceCode(s.getProvinceCode());
		sDTO.setTownhallUrl(s.getTownhallUrl());
		return sDTO;
	}

	public static Subsidiary convert(SubsidiaryDTO sDTO) {
		Subsidiary s = new Subsidiary();
		s.setId(sDTO.getId());
		s.setCnpj(sDTO.getCnpj());
		s.setProvinceCode(sDTO.getProvinceCode());
		s.setTownhallUrl(sDTO.getTownhallUrl());
		return s;
	}


}
