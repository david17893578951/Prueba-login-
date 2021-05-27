package com.OCLogin.Prueba.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = {"*"})
public class pruebaController {
	
	@RequestMapping(value = "/p",method = RequestMethod.GET)
	public 	String getAllPersona(){
		return "Hola";
	}
	
	@Secured({"ROLE_CATALOG"})
	@RequestMapping(value = "/q",method = RequestMethod.GET)
	public 	String getAllPersonaa(){
		return "Hola soy parametros";
	}
	
	@Secured({"ROLE_ADMIN"})
	@RequestMapping(value = "/r/{id}",method = RequestMethod.GET)
	public 	String getAllPersona3(){
		return "hola solo rol Admin";
	}
	
	@Secured({"ROLE_ADMIN" ,"ROLE_USER"})
	@RequestMapping(value = "/s",method = RequestMethod.GET)
	public 	String getAllPersona4(){
		return "hola solo rol Admin y user";
	}


}
