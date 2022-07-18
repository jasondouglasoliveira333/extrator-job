package br.com.lkm.extrator.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.lkm.extrator.dto.TokenResult;
import br.com.lkm.extrator.service.OutlookMailService;
import br.com.lkm.extrator.service.TokenService;

@CrossOrigin
@RestController
public class ExtratorController {
	
	private Logger log = LoggerFactory.getLogger(getClass()); 

	@Autowired
	private TokenService tokenService;
	
	@Autowired
	private OutlookMailService outlookMailService;
	
	
	@GetMapping("ping")
	public String ping() {
		log.info("IN ExtratorController.ping");
		return "Hi FOLKS";
	}
	
	//Callback do Oauth2 Microsoft
	@GetMapping("accessCodeCallback")
	public String retriveCode(@RequestParam("code") String code, @RequestParam("state") String state) {
		System.out.println("In retriveToken - code:" + code + " - state:" + state);
		try {
			TokenResult mAccessToken = tokenService.generateMicrosoftAccessToken(code, "code");
			System.out.println("mAccessToken.getAccessToken():" + mAccessToken.getAccessToken());
			System.out.println("mAccessToken.getRefreshToken():" + mAccessToken.getRefreshToken());
			boolean jaCadastrado = outlookMailService.save(mAccessToken, state);
			if (!jaCadastrado) {
				return "Token gerado com sucesso!";
			}else {
				return "Email já cadastrado!";
			}
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}

//	@GetMapping("accessCodeCallback")
//	public String logout() {
//		return "Usuário deslogado!";
//	}
	
	//Local
	@GetMapping("extrator-job/accessCodeCallback")
	public String retriveCodeLocal(@RequestParam("code") String code, @RequestParam("state") String state) {
		return retriveCode(code, state);
	}

	
	//Callback do Oauth2 Google
	@GetMapping
	public String retriveCodeRoot(@RequestParam("code") String code, @RequestParam("state") String state) {
		try {
			TokenResult mAccessToken = tokenService.generateGoogleAccessToken(code, "code");
			System.out.println("accessToken:" + mAccessToken.getAccessToken());
//			mailService.listMails(null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "Hi FOLKS 333";
	}

}
