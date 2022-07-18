package br.com.lkm.extrator.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import br.com.lkm.extrator.dto.ExtractorAttachment;
import br.com.lkm.extrator.dto.ExtractorMail;
import br.com.lkm.extrator.dto.TokenResult;
import br.com.lkm.extrator.dto.UserInfo;
import br.com.lkm.extrator.dto.outlook.Attachment;
import br.com.lkm.extrator.dto.outlook.CreateFolderRequest;
import br.com.lkm.extrator.dto.outlook.Folder;
import br.com.lkm.extrator.dto.outlook.Message;
import br.com.lkm.extrator.dto.outlook.MoveMailFolderRequest;
import br.com.lkm.extrator.dto.outlook.PagedResult;
import br.com.lkm.extrator.entity.Corporation;
import br.com.lkm.extrator.entity.MailConfiguration;
import br.com.lkm.extrator.entity.MailOutlookInfo;
import br.com.lkm.extrator.enums.MailConfigurationType;
import br.com.lkm.extrator.repository.CorporationRepository;
import br.com.lkm.extrator.repository.MailConfigurationRepository;
import br.com.lkm.extrator.repository.MailOutlookInfoRepository;

@Service
public class OutlookMailService implements MailService{
	
	private Logger log = LoggerFactory.getLogger(getClass()); 

	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private CorporationRepository corporationRepository;
	
	@Autowired
	private MailOutlookInfoRepository mailOutlookInfoRepository;  
	
	@Autowired
	private MailConfigurationRepository mailConfigurationRepository;

	@Value("${extrator.job.mail.message.amount}")
	private Integer messageAmount;

	public OutlookMailService() {
	}

	public List<ExtractorMail> listMails(Corporation corp) throws Exception {
        List<ExtractorMail> mails = new ArrayList<ExtractorMail>();
		List<MailOutlookInfo> mois = corp.getMailConfiguration().getMailOutlookInfos();
		for (MailOutlookInfo moi : mois) {
			checkTokenExpiration(moi);
			System.out.println("moi.getAccessToken():" + moi.getAccessToken().substring(moi.getAccessToken().length()-40));
			log.debug("moi.getAccessToken():" + moi.getAccessToken().substring(moi.getAccessToken().length()-40));
			OutlookService outlookService = OutlookServiceBuilder.getOutlookService(moi.getAccessToken());
			
	        PagedResult<Message> messages = outlookService.getMessages("inbox",  "receivedDateTime DESC", 
	        		"receivedDateTime, from, isRead, Subject, hasAttachments, body", messageAmount).execute().body();
	        
	        String receiver = outlookService.getCurrentUser().execute().body().getMail();
	        if (messages != null) {
		        for (Message m : messages.getValue()) {
		        	ExtractorMail em = new ExtractorMail();
		        	em.setReceiver(receiver);
		        	em.setSender(m.getFrom().getEmailAddress().getAddress());
		        	em.setDate(m.getReceivedDateTime());
		        	em.setSubject(m.getSubject());
		        	em.setOriginalMessage(m);
		        	em.setExtra(moi);
		        	em.setBody(m.getBody().getContent());
		        	PagedResult<Attachment> ats = outlookService.getAttachments(m.getId()).execute().body();
		        	List<ExtractorAttachment> eats = new ArrayList<>();
		        	for (Attachment at : ats.getValue()) {
		        		ExtractorAttachment eat = new ExtractorAttachment();
		        		eat.setFileName(at.getName());
		        		eat.setContent(at.getContentBytes());
		        		eat.setContentType(at.getContentType());
		        		eats.add(eat);
		        	}
		        	em.setAttachments(eats);
		        	mails.add(em);
		        }
	        }
		}
        return mails;
	}
	
	@SuppressWarnings("unused")
	private void checkTokenExpiration(MailOutlookInfo moi) throws Exception {
		System.out.println("moi.getExpiration():" + moi.getExpiration());
		if (moi.getExpiration().isBefore(LocalDateTime.now())) {
			log.info("Renewing token");
			TokenResult tokenResult = tokenService.generateMicrosoftAccessToken(moi.getRefreshToken(), "refresh_token");
			String token = tokenResult.getAccessToken();
			moi.setAccessToken(token);
			LocalDateTime expiration = LocalDateTime.now().plusMinutes(1); //tokenResult.getExpiresIn()
			moi.setExpiration(expiration);
			mailOutlookInfoRepository.save(moi);
		}
	}

	public boolean save(TokenResult mAccessToken, String state) throws Exception {
		//verify if the user is not already in database
		UserInfo userInfo = tokenService.getUserInfo(mAccessToken.getAccessToken());
		//Thne shit of microsoft doesnt return the email from some accounts.
		String tokenKey = null;
		if (userInfo.getEmail() != null) {
			tokenKey = userInfo.getEmail();
		}else {
			tokenKey = userInfo.getSub();
		}
		System.out.println("mailOutlooksSize:" + mailOutlookInfoRepository.findByEmail(tokenKey));
		Corporation u = corporationRepository.getOne(Integer.parseInt(state));
		List<MailOutlookInfo> moiList = mailOutlookInfoRepository.findByEmail(tokenKey);
		if (moiList.size() == 0) {
			MailOutlookInfo moi = new MailOutlookInfo();
			moi.setAccessToken(mAccessToken.getAccessToken());
			moi.setRefreshToken(mAccessToken.getRefreshToken());
//			moi.setExpiration(LocalDateTime.now().plusMinutes(mAccessToken.getExpiresIn())); //Dont trust in this shit
			LocalDateTime expiration = LocalDateTime.now().plusMinutes(1); 
			moi.setExpiration(expiration);
			System.out.println(">>>moi.getExpiration():" + moi.getExpiration());
			moi.setEmail(tokenKey);
			MailConfiguration mc = u.getMailConfiguration();
			if (mc == null) {//creates a configuration
				mc = new MailConfiguration();
				mc.setMailConfigurationType(MailConfigurationType.OFFICE_365);
				mc.setCorporation(u);
				mailConfigurationRepository.save(mc);
				mc.setMailInfos(new ArrayList<>());
				mc.setMailOutlookInfos(new ArrayList<>());
			}
			moi.setMailConfiguration(mc);
			mc.getMailOutlookInfos().add(moi);
			mailOutlookInfoRepository.save(moi);
			return false;
		}else {
			String email = moiList.get(0).getEmail();
			MailConfiguration mc = u.getMailConfiguration();
			MailOutlookInfo moi = mc.getMailOutlookInfos().stream().filter(moiI -> moiI.getEmail().equals(email)).collect(Collectors.toList()).get(0);
			moi.setAccessToken(mAccessToken.getAccessToken());
			moi.setRefreshToken(mAccessToken.getRefreshToken());
			moi.setExpiration(LocalDateTime.ofInstant(Instant.ofEpochMilli(mAccessToken.getExpiresIn()), ZoneId.systemDefault()));
			mailOutlookInfoRepository.save(moi);
			return true;
		}
	}
	
	public void moveToBackup(String backupFolder, ExtractorMail em) throws Exception {
		MailOutlookInfo moi = (MailOutlookInfo) em.getExtra();
//		checkTokenExpiration(moi);
		OutlookService outlookService = OutlookServiceBuilder.getOutlookService(moi.getAccessToken());
		String backupFolderId = checkBackupFolder(outlookService, backupFolder);
		MoveMailFolderRequest moveMailFolderRequest = new MoveMailFolderRequest(backupFolderId);
		Message message = (Message) em.getOriginalMessage();
		outlookService.moveMailFolder(message.getId(), moveMailFolderRequest).execute();
	}

	private String checkBackupFolder(OutlookService outlookService, String backupFolder) throws Exception {
		String backupFolderId = null;
		Folder[] folders = outlookService.getFolders().execute().body().getValue();
		for (Folder f : folders) {
			if (f.getDisplayName().equals(backupFolder)) {
				backupFolderId = f.getId();
				break;
			}
		}
		if (backupFolderId == null) {
			CreateFolderRequest folderRequest = new CreateFolderRequest(backupFolder);
			backupFolderId = outlookService.createFolder(folderRequest).execute().body().getId();
		}
		return backupFolderId;
	}

}
