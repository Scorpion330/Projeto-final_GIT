package pt.projetofinal.project.controller;

import java.text.SimpleDateFormat;
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

@Controller
public class Contactoscontroller {
	
	@Autowired
	Contactorepository service;

	@GetMapping("/nova_mensagem")
	public String nova(Model m,String fragment) {
		
		m.addAttribute("fragment",fragment);
		
		return "main.html";
	}

	@PostMapping("/enviar_mensagem") 
	public String addmensagem(Contacto c, Model m, String nome,String email,String mensagem, HttpSession request) {
		String dia;
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
			/*if(email=="findlechef_support@hotmail.com"){
				c.setTipo("1");
			}else {
				c.setTipo("0");
			}*/
		
		Date date = new Date();	
		Calendar calendar1 = new GregorianCalendar();
		
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
		
		
		service.save(c);
			
		
		return "redirect:/painel?fragment=painel_admin";
	}
	
	@GetMapping("/caixa_entrada")
	public String lista_mensagens(Model m,String fragment) {
		m.addAttribute("mensagem",service.findAll());
		m.addAttribute("fragment",fragment);
		
		return "main.html";
	}

}
