package com.OCLogin.Prueba.config.auth;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.stereotype.Component;

import com.OCLogin.Prueba.service.IUsuarioService;
import com.openkm.sdk4j.bean.CommonUser;


@Component
public class InfoAdicionalToken implements TokenEnhancer {
	
	@Autowired
	private IUsuarioService usuarioService;


	@Override
	public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
		
		try {
			
			// Get full Name
			//CommonUser commonUser = ws.getUser(authentication.getName());
			CommonUser usuario = usuarioService.findFullName(authentication.getName());
			
			
			Map<String, Object> info = new HashMap<>();
			info.put("ID", usuario.getId());
			info.put("Name", usuario.getName());
			info.put("Mail", usuario.getEmail());
			info.put("Active", usuario.getActive());
			
			
			((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(info);
			
			return accessToken;
			
		} catch (Exception e) {
			return accessToken;
		}
		
		
	}
	
}
