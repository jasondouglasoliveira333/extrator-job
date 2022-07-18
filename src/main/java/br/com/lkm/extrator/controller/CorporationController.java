package br.com.lkm.extrator.controller;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.lkm.extrator.dto.CertificateDTO;
import br.com.lkm.extrator.dto.CorporationDTO;
import br.com.lkm.extrator.dto.MailConfigurationResponseDTO;
import br.com.lkm.extrator.dto.MailInfoRequestResponseDTO;
import br.com.lkm.extrator.dto.PageResponse;
import br.com.lkm.extrator.dto.SubsidiaryDTO;
import br.com.lkm.extrator.job.InvoiceDownloaderJob;
import br.com.lkm.extrator.repository.MailInfoRepository;
import br.com.lkm.extrator.repository.MailOutlookInfoRepository;
import br.com.lkm.extrator.service.CorporationService;
import br.com.lkm.extrator.service.InvoiceService;
import br.com.lkm.extrator.service.MailConfigurationService;
import okhttp3.MediaType;
import okhttp3.MultipartBody.Part;

@CrossOrigin
@RestController
@RequestMapping("/api/extrator/corporations")
public class CorporationController {
	
	private Logger log = LoggerFactory.getLogger(getClass()); 

	@Autowired
	private CorporationService corporationService; 
	
	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private MailConfigurationService mailConfigurationService;
	
	@Autowired
	private MailInfoRepository mailInfoRepository;

	@Autowired
	private MailOutlookInfoRepository mailOutlookInfoRepository;
	
	
	@Autowired
	private InvoiceDownloaderJob invoiceDownloaderJob;
	

	@GetMapping
	public ResponseEntity<?> listCorporations(@RequestParam(name="page", defaultValue = "0") Integer page, 
			@RequestParam(name="size", defaultValue = "10") Integer size){
		try {
			PageRequest pageable = PageRequest.of(page, size);
			PageResponse<CorporationDTO> pr = corporationService.findAll(pageable);
			return ResponseEntity.ok(pr);
		}catch (Exception e) {
			log.error("Erro listando os certificados ", e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("{id}/subsidiaries")
	public ResponseEntity<?> listCorporations(@PathVariable("id") Integer id, @RequestParam(name="page", defaultValue = "0") Integer page, 
			@RequestParam(name="size", defaultValue = "10") Integer size){
		try {
			PageRequest pageable = PageRequest.of(page, size);
			PageResponse<SubsidiaryDTO> pr = corporationService.findAllSubsidiaries(id, pageable);
			return ResponseEntity.ok(pr);
		}catch (Exception e) {
			log.error("Erro listando os certificados ", e);
			return ResponseEntity.badRequest().build();
		}
	}

	@GetMapping("{id}")
	public ResponseEntity<?> get(@PathVariable("id") Integer id){
		try {
			CorporationDTO cDTO = corporationService.get(id);
			CertificateDTO certDTO = invoiceService.get(cDTO.getRootCnpj()).execute().body();
			cDTO.setCertificateFileName(certDTO.getName());
			cDTO.setCertificatePassword(certDTO.getPassword());
			return ResponseEntity.ok(cDTO);
		}catch (Exception e) {
			log.error("Erro listando os certificados ", e);
			return ResponseEntity.badRequest().build();
		}
	}

	@PostMapping
	@Transactional
	public ResponseEntity<?> saveCorporation(@RequestParam(name="file", required = false) MultipartFile file, CorporationDTO cDTO){
		try {
			log.info("cDTO:" + cDTO);
			corporationService.save(cDTO);
			MediaType mt = MediaType.parse(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM_VALUE);
			if (file !=  null) { //in tbe update can come null
				invoiceService.deleteCertificate(cDTO.getRootCnpj()).execute().body();
				Part p = Part.createFormData("file", file.getOriginalFilename(), okhttp3.RequestBody.create(mt, file.getBytes()));
				invoiceService.saveCertificate(p, cDTO.getId(), cDTO.getRootCnpj(), file.getOriginalFilename(), cDTO.getCertificatePassword()).execute().body();
			}else {
				invoiceService.updateCertificatePassword(cDTO.getRootCnpj(), cDTO.getCertificatePassword()).execute().body();
			}
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			log.error("Erro validando a nota fiscal", e);
			return ResponseEntity.badRequest().build();
		}
	}
	
	@PostMapping(path="{id}/subsidiaries", consumes = "*")
	public ResponseEntity<?> saveSubsiriaies(@PathVariable("id") Integer id, @RequestBody String subsidiariesData){
		//@RequestBody String subsidiariesData problema with CORS in BTP
		try {
			log.info("subsidiariesData:" + subsidiariesData);
			ObjectMapper mapper = new ObjectMapper();
		    SubsidiaryDTO[] subsidiaries = mapper.readValue(subsidiariesData, SubsidiaryDTO[].class);
			corporationService.saveSubsidiaries(id, Arrays.asList(subsidiaries));
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			log.error("Erro listando os subsidiarios ", e);
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping(path="{id}/D")//GetMappint(../D) problema with CORS in BTP
	public ResponseEntity<?> deleteCorporation(@PathVariable("id") Integer id){
		try {
			CorporationDTO c = corporationService.get(id);
			invoiceService.deleteCertificate(c.getRootCnpj()).execute().body();
			corporationService.deleteById(id);
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			log.error("Erro listando os subsidiarios ", e);
			return ResponseEntity.badRequest().build();
		}
	}
	
	@GetMapping(path="{id}/subsidiaries/{subId}/D")//GetMappint(../D) problema with CORS in BTP
	public ResponseEntity<?> deleteSubsiriaies(@PathVariable("subId") Integer id){
		try {
			corporationService.deleteSubsidiaryById(id);
			return ResponseEntity.ok().build();
		}catch (Exception e) {
			log.error("Erro listando os subsidiarios ", e);
			return ResponseEntity.badRequest().build();
		}
	}

	//Reestruturar 
	@GetMapping("{id}/mailConfigurations")
	public MailConfigurationResponseDTO getMailConfiguration(@PathVariable("id") Integer id) {
//		log.info("IN getMailConfiguration");
		MailConfigurationResponseDTO mc = mailConfigurationService.findByCorporationId(id);
		return mc;
	}
	
	@PostMapping("{id}/mailConfigurations")
	public void createMainInfo(@PathVariable("id") Integer id, MailInfoRequestResponseDTO mainInfo) {
		log.info("IN criaMainInfo");
		mailConfigurationService.saveMainInfo(id, mainInfo);
	}

	@PutMapping("{id}/mailConfigurations")
	public void updateMainInfo(@PathVariable("id") Integer id, MailInfoRequestResponseDTO mainInfo) {
		log.info("IN updateMainInfo");
		mailConfigurationService.saveMainInfo(id, mainInfo);
	}
	
	@Transactional
	@GetMapping("{id}/mailConfigurations/{mcId}/mailInfos/{miId}")
	public void deleteMailInfo(@PathVariable("miId") Integer miId) {
		log.info("IN deleteMailInfo:" + miId);
		mailInfoRepository.deleteById(miId);
	}

	@Transactional
	@GetMapping("{id}/mailConfigurations/{mcId}/mailOutlooks/{moiId}")
	public void deleteMailOutlook(@PathVariable("moiId") Integer moiId) {
		log.info("IN deleteMailOutlook:" + moiId);
		mailOutlookInfoRepository.deleteById(moiId);
	}

	
	@GetMapping("{id}/runJobs")
	public void runJobs() {
		invoiceDownloaderJob.invoiceDownload();
	}
	
	

}
