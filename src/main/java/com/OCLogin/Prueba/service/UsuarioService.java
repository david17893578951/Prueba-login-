package com.OCLogin.Prueba.service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.OCLogin.Prueba.cache.WSCacheDAO;
import com.openkm.sdk4j.OKMWebservices;
import com.openkm.sdk4j.bean.CommonUser;
import com.openkm.sdk4j.bean.SqlQueryResultColumns;
import com.openkm.sdk4j.bean.SqlQueryResults;

@Service
public class UsuarioService implements IUsuarioService, UserDetailsService {
	private Logger logger = LoggerFactory.getLogger(UsuarioService.class);

	@Autowired
	private WSCacheDAO wsCache;

	@Override
	@Transactional(readOnly=true)
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();

		try {
			System.out.println("estoy aqui Openkm");
			OKMWebservices ws = wsCache.getOKMWebservices(WSCacheDAO.ADMIN_USER);

			// Get roles
			List<String> roles = ws.getRolesByUser(username);
			System.out.println(roles);

			// set roles for spring-security according to those received from
			// OKM ws response
			Iterator<String> it = roles.iterator();
			String role;
			while (it.hasNext()) {
				role = it.next();
				authorities.add(new SimpleGrantedAuthority(role));
				System.out.println(authorities);
			}

			// Get Password
			String sql = "SELECT USR_PASSWORD FROM OKM_USER WHERE USR_ID='" + username + "'";
			InputStream is = new ByteArrayInputStream(sql.getBytes(StandardCharsets.UTF_8));
			SqlQueryResults result = ws.executeSqlQuery(is);
			

			String password = "";
			for (SqlQueryResultColumns row : result.getResults()) {
				password = row.getColumns().get(0);
			}
			IOUtils.closeQuietly(is);
			System.out.println(username);
			System.out.println(password);
			

			 return new User(username, password, true, true, true, true, authorities);
		} catch (Exception e) {
			logger.error("Error: {}", e.getMessage(), e);
			return new User(username, "", true, true, true, true, authorities);
		}

	}

	@Override
	@Transactional(readOnly=true)
	public CommonUser findFullName(String Usuername) {
		try {
			OKMWebservices ws = wsCache.getOKMWebservices(WSCacheDAO.ADMIN_USER);
			CommonUser commonUser = ws.getUser(Usuername);
			
			return commonUser;
		} catch (Exception e) {
			return null;
		}
	}


}
