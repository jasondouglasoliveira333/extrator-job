package br.com.lkm.extrator.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Flags.Flag;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Message.RecipientType;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;

import org.springframework.stereotype.Service;

import br.com.lkm.extrator.dto.ExtractorAttachment;
import br.com.lkm.extrator.dto.ExtractorMail;
import br.com.lkm.extrator.entity.Corporation;
import br.com.lkm.extrator.entity.MailInfo;
import br.com.lkm.extrator.util.IOUtil;

@Service
public class SimpleMailService implements MailService{
	
	private Map<Integer, Map<String, Store>> stores = new HashMap<>(); 
	
	public synchronized List<ExtractorMail> listMails(Corporation corp) throws Exception {
		Map<String, Store> userStores = stores.get(corp.getId());
		if (userStores == null) {
			userStores  = createStores(corp);
			stores.put(corp.getId(), userStores);
		}
		return listEmails(userStores);
	}
	
	private Map<String, Store> createStores(Corporation corp) throws Exception {
		Map<String, Store> userStores = new HashMap<>();
		for (MailInfo mi : corp.getMailConfiguration().getMailInfos()){
			Store store = createStore(mi);
			userStores.put(mi.getUsername(), store);
		}
		return userStores;
	}

	private Store createStore(MailInfo mi) throws Exception {
		Properties p = new Properties();
		
		p.setProperty("mail.imap.ssl.enable", "true");
		p.setProperty("mail.imap.host", mi.getHost());
		p.setProperty("mail.imap.port", String.valueOf(mi.getPort()));
		p.setProperty("mail.imap.user", mi.getUsername());

		Session session = Session.getDefaultInstance(p);
		Store store = session.getStore(mi.getProtocol().toLowerCase());
		store.connect(mi.getUsername(), mi.getPassword());
		return store;
	}

	private List<ExtractorMail> listEmails(Map<String, Store> userStores) throws Exception{
		List<ExtractorMail> mails = new ArrayList<>();
		for (Store store : userStores.values()) {
			Folder f = store.getDefaultFolder().getFolder("INBOX");
			f.open(Folder.READ_ONLY);
			Message[] ms = f.getMessages();
			for (Message m : ms) {
				System.out.println("ms:" + m.getSubject());
				ExtractorMail em = new ExtractorMail();
				em.setSubject(m.getSubject());
				em.setSender(m.getFrom()[0].toString());
				em.setReceiver(m.getRecipients(RecipientType.TO)[0].toString());
				em.setDate(m.getReceivedDate());
				System.out.println("m.get Content():" + m.getContent());
				if (m.getContent() instanceof Multipart) {
					List<ExtractorAttachment> eas = listAttachment((Multipart)m.getContent());
					em.setAttachments(eas);
					String body = extractBody((Multipart)m.getContent());
					em.setBody(body);
				}
				em.setOriginalMessage(m);
				mails.add(em);
			}
			f.close(false);
		}
		return mails;
	}

	private List<ExtractorAttachment> listAttachment(Multipart multipart) throws Exception {
		List<ExtractorAttachment> attachments = new ArrayList<>();
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);
            if (part.getDisposition() != null) {
            	ExtractorAttachment attachment = new ExtractorAttachment();
            	attachment.setFileName(part.getFileName());
            	byte[] data = readPartContent(part);
            	attachment.setContent(data);
            	attachment.setContentType(part.getContentType());
            	attachments.add(attachment);
            }
        }
		return attachments;
	}

	private String extractBody(Multipart multipart) throws Exception {
        for (int i = 0; i < multipart.getCount(); i++) {
            BodyPart part = multipart.getBodyPart(i);
            if (part.getContentType().toLowerCase().contains("text")) {
            	byte[] data = readPartContent(part);
            	return new String(data);
            }
        }
		return null;
	}

	private byte[] readPartContent(Part part) throws Exception {
        byte[] data = null;
        if (part.getContentType().contains("text")) {
        	if (part.getContent() instanceof com.sun.mail.util.BASE64DecoderStream) {
        		data = IOUtil.readAllBytes((com.sun.mail.util.BASE64DecoderStream)part.getContent());
        	}else {
        		data = ((String)part.getContent()).getBytes();
        	}
        } else {
        	data = IOUtil.readAllBytes(part.getInputStream());
        }
        return data;
	}

	public void moveToBackup(String backupFolder, ExtractorMail em) {
		try {
			Message message = (Message) em.getOriginalMessage();
			Store store = message.getFolder().getStore();
			Folder bkFolder = store.getFolder(backupFolder);
			if (!bkFolder.exists()) {
				bkFolder.create(Folder.HOLDS_MESSAGES);
			}
			Folder inbox = message.getFolder();
			inbox.open(Folder.READ_WRITE);
			inbox.copyMessages(new Message[] {message}, bkFolder);
			message.setFlag(Flag.DELETED, true);
			inbox.close(false);
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		
	}

}
