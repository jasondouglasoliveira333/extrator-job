package br.com.lkm.extrator.service;

import java.net.URI;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.lkm.extrator.dto.TokenResult;
import br.com.lkm.extrator.dto.UserInfo;

@Service
public class TokenService {
	
	@Value("${extrator.microsoft.token.url}")
	private String microsoftTokenUrl;
	
	@Value("${extrator.microsoft.token.params}")
	private String microsoftTokenParams;
	
	@Value("${extrator.microsoft.token.scopes}")
	private String microsoftScopes;
	
	@Value("${extrator.microsoft.userInfo.url}")
	private String microsoftUserInfoUrl;

	@Value("${extrator.google.token.url}")
	private String googleTokenUrl;
	
	@Value("${extrator.google.token.params}")
	private String googleTokenParams;
	
	@Value("${extrator.google.token.scopes}")
	private String googleScopes;

	@Autowired
	private RestTemplate restTemplate;
	
	public TokenResult generateMicrosoftAccessToken(String code, String codeName) throws Exception {
		return generateAccessToken(microsoftTokenUrl, microsoftTokenParams, microsoftScopes, code, codeName);
	}
	
	public TokenResult generateGoogleAccessToken(String code, String codeName) throws Exception {
		return generateAccessToken(googleTokenUrl, googleTokenParams, googleScopes, code, codeName);
	}
	
	public TokenResult generateAccessToken(String tokenUrl, String tokenParams, String scopes, String code, String codeName) throws Exception {
		String tokenParamsGrantType = null;
		if (codeName.equals("code")) {
			tokenParamsGrantType = tokenParams.replace("{grantType}", "authorization_code");
		}else {
			tokenParamsGrantType = tokenParams.replace("{grantType}", "refresh_token");
		}
		String requestParams = tokenParamsGrantType + "&" + codeName + "="+ code + scopes;
		HttpHeaders headers = new HttpHeaders();
		headers.put("Content-Type", Arrays.asList("application/x-www-form-urlencoded"));
		RequestEntity<String> request = new RequestEntity<String>(requestParams, headers, HttpMethod.POST, new URI(tokenUrl));
		ResponseEntity<TokenResult> response = restTemplate.postForEntity(tokenUrl, request, TokenResult.class);
		return response.getBody();
	}

	public UserInfo getUserInfo(String accessToken) throws Exception {
		HttpHeaders headers = new HttpHeaders();
		headers.put("Authorization", Arrays.asList("Bearer " + accessToken));
		RequestEntity<String> request = new RequestEntity<String>(headers, HttpMethod.GET, new URI(microsoftUserInfoUrl));
		ResponseEntity<String> response = restTemplate.exchange(microsoftUserInfoUrl, HttpMethod.GET, request, String.class);
		String responseS = response.getBody();
		System.out.println("responseUserInfo:" + responseS);
		ObjectMapper m = new ObjectMapper();
		return m.readerFor(UserInfo.class).readValue(responseS);
	}

}
