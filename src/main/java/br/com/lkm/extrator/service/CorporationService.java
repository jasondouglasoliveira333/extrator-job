package br.com.lkm.extrator.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.lkm.extrator.converter.CorporationConverter;
import br.com.lkm.extrator.converter.SapConfigurationConverter;
import br.com.lkm.extrator.converter.SubsidiaryConverter;
import br.com.lkm.extrator.dto.CorporationDTO;
import br.com.lkm.extrator.dto.PageResponse;
import br.com.lkm.extrator.dto.SubsidiaryDTO;
import br.com.lkm.extrator.entity.Corporation;
import br.com.lkm.extrator.entity.MailConfiguration;
import br.com.lkm.extrator.entity.SapConfiguration;
import br.com.lkm.extrator.entity.Subsidiary;
import br.com.lkm.extrator.repository.CorporationRepository;
import br.com.lkm.extrator.repository.MailConfigurationRepository;
import br.com.lkm.extrator.repository.MailInfoRepository;
import br.com.lkm.extrator.repository.MailOutlookInfoRepository;
import br.com.lkm.extrator.repository.SapConfigurationRepository;
import br.com.lkm.extrator.repository.SubsidiaryRepository;

@Service
public class CorporationService {

	@Autowired
	private CorporationRepository corporationRepository;

	@Autowired
	private SubsidiaryRepository subsidiaryRepository;

	@Autowired
	private SapConfigurationRepository sapConfigurationRepository;
	
	@Autowired
	private MailConfigurationRepository mailConfigurationRepository;
	
	@Autowired
	private MailOutlookInfoRepository mailOutlookInfoRepository;

	@Autowired
	private MailInfoRepository mailInfoRepository;

	public PageResponse<CorporationDTO> findAll(Pageable pageable) {
		PageResponse<CorporationDTO> prCorp = new PageResponse<>();  
		Page<Corporation> pCorp = corporationRepository.findAll(pageable);
		prCorp.setContent(pCorp.getContent().stream().map(CorporationConverter::convert).collect(Collectors.toList()));
		prCorp.setTotalPages(pCorp.getTotalPages());
		return prCorp;
	}

	public CorporationDTO get(Integer id) {
		Corporation corp = corporationRepository.getOne(id);
		return CorporationConverter.convert(corp);
	}  

	public void save(CorporationDTO cDTO) {
		Corporation c = CorporationConverter.convert(cDTO);
		c.setLastJobExecution(LocalDateTime.now().minusDays(c.getRetroativeDays()));
		if (c.getId() == 0) {//solve this later
			c.setId(null);
		}
		SapConfiguration sc = SapConfigurationConverter.convert(cDTO);
		System.out.println("sc.getId():" + sc.getId());
		if (sc.getId() == 0) {//solve this later
			sc.setId(null);
		}
		System.out.println("sc:" + sc);
		corporationRepository.save(c);
		sc.setCorporation(c);
		sapConfigurationRepository.save(sc);
		if (c.getSubsidiaries() != null) {
			c.getSubsidiaries().stream().forEach(s -> {
				subsidiaryRepository.save(s);
			});
		}
	}

	public PageResponse<SubsidiaryDTO> findAllSubsidiaries(Integer id, Pageable pageable) {
		PageResponse<SubsidiaryDTO> prSub = new PageResponse<>();  
		Page<Subsidiary> pSub = subsidiaryRepository.findByCorporationId(id, pageable);
		prSub.setContent(pSub.getContent().stream().map(SubsidiaryConverter::convert).collect(Collectors.toList()));
		prSub.setTotalPages(pSub.getTotalPages());
		return prSub;
	}

	public void saveSubsidiaries(Integer corpId, List<SubsidiaryDTO> subsidiaries) {
		subsidiaries.stream().forEach(subsidiary -> {
			Subsidiary s = SubsidiaryConverter.convert(subsidiary);
			Corporation c = new Corporation();
			c.setId(corpId);
			s.setCorporation(c);
			subsidiaryRepository.save(s);
		});
	}

	public void deleteSubsidiaryById(Integer id) {
		subsidiaryRepository.deleteById(id);
	}

	@Transactional
	public void deleteById(Integer id) {
		Corporation c = corporationRepository.getOne(id);
		MailConfiguration mc = c.getMailConfiguration();
		if (mc != null) {
			mc.getMailInfos().forEach(mi -> {
				mailInfoRepository.delete(mi);
			});
			mc.getMailOutlookInfos().forEach(moi -> {
				mailOutlookInfoRepository.delete(moi);
			});
			mailConfigurationRepository.delete(mc);
		}
		sapConfigurationRepository.delete(c.getSapConfiguration());
		c.getSubsidiaries().forEach(s -> {
			subsidiaryRepository.delete(s);
		});
		corporationRepository.delete(c);
	}

	
}
