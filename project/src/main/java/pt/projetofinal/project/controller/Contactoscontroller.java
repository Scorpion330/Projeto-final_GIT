package pt.projetofinal.project.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import pt.projetofinal.project.model.Contacto;
import pt.projetofinal.project.model.Login;
import pt.projetofinal.project.service.Contactorepository;
import pt.projetofinal.project.service.Loginrepository;

@Controller
public class Contactoscontroller {
	
	@Autowired
	Contactorepository service;
	
	@Autowired
	Loginrepository svlogin;
	
	String dia;
	
	Date date = new Date();	
	Calendar calendar1 = new GregorianCalendar();

	@GetMapping("/nova_mensagem")
	public String nova(Model m,String fragment, String idm, HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null || l.getTipo().compareTo("1")!=0 && l.getTipo().compareTo("0")!=0) {return "redirect:/login";}
		
		ArrayList<Login> arlogin = new ArrayList<>();
		
		System.out.println("fosga-se o id "+idm);
		
		m.addAttribute("idm",idm);
		m.addAttribute("contacto",l);
		m.addAttribute("fragment",fragment);
		
		if(l.getTipo().equals("0")) {
			System.out.println("main if");
			m.addAttribute("fragment",fragment);
			System.out.println("fragment "+fragment);
			return "main.html";
		}
		
		else if (l.getTipo().equals("1")) {
			return "mainownerprofile.html";
		}
			System.out.println("lmao fui aqui ter");
		return "main.html";
	}

	@PostMapping("/enviar_mensagem") 
	public String addmensagem(Contacto c, Model m, String idm, HttpSession request) { //String nome,String email,String mensagem,
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		calendar1.setTime(date);

		//int ano = calendar1.get(Calendar.YEAR);
		int min=calendar1.get(Calendar.MINUTE);
		int sec=calendar1.get(Calendar.SECOND);
		int hour=calendar1.get(Calendar.HOUR_OF_DAY);	
		int day=calendar1.get(Calendar.DAY_OF_MONTH);
		
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 1);
		SimpleDateFormat data1 = new SimpleDateFormat("yyyy-MM-dd");
		
		String data = data1.format(cal.getTime());
							
		dia=data+"-"+hour+":"+min+":"+sec;
		
		c.setData(dia);
		
		if(l.getTipo().equals("1")){ // tipo do user se é dono
			
			c.setId_dono(l.getId());
			c.setTipo("1"); //para a mensagem
			service.save(c);
			
			for(Contacto t: service.findAll()) {
				
				if(idm!=null) {
					
					if(t.getId().compareTo(idm)==0) {
						
						t.setTipo("3");
						service.save(t);
					}
					
				}
				
			}
			return "mainownerprofile.html";
			
		}else if(l.getTipo().equals("0")) {
			for(Contacto cc: service.findAll()) {
				
				if(cc.getId().compareTo(idm)==0) {
				
					cc.setTipo("3");
					service.save(cc);
					c.setId_dono(cc.getId_dono());
					c.setTipo("0");
					service.save(c);
					return "redirect:/painel?fragment=painel_admin";
				}
				
			}	
		}
	
			
		
		return "redirect:/painel?fragment=painel_admin";
	}
	
	@GetMapping("/caixa_entrada")
	public String lista_mensagens(Contacto c,Model m,String fragment,HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null || l.getTipo().compareTo("2")==0) {return "redirect:/login";}
		
		ArrayList<Contacto> arrc = new ArrayList<>();
		
		for(Contacto cc: service.findAll()) {
			if(cc.getTipo().equals("1") && l.getTipo().compareTo("0")==0) {
			
				arrc.add(cc);
			}else if(cc.getTipo().equals("0") && l.getTipo().compareTo("1")==0 && l.getId().compareTo(cc.getId_dono())==0) {
				arrc.add(cc);
			}
		}
		
		m.addAttribute("mensagem",arrc);
		
		m.addAttribute("fragment",fragment);
		
		return "main.html";
	}
	
}
