package pt.projetofinal.project.controller;

import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import pt.projetofinal.project.model.Login;
import pt.projetofinal.project.service.Loginrepository;

@Controller
public class Registoscontroller {
	
	@Autowired
	Loginrepository service;
	@Autowired
	Restaurantecontroller rc;
	
	
	@GetMapping("/introduzir")
	public String registar(Model m,String fragment,HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		m.addAttribute("fragment",fragment);
		return "main.html";
	}
	
	@PostMapping("/registar")
	public String add(Login ll,HttpSession request) { //receber o modelo inteiro
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		Funcoes.sendEmailReset("pedroalex.vicente52@gmail.com"); // email do objeto
		service.save(ll);
		
		if(ll.getTipo().equals("1")) {
			return "redirect:/painel?fragment=painel_admin";
		}
	
		return "redirect:/painel?fragment=painel_admin";
	}
	
	
	@GetMapping("/listagem_utilizadores")
	public String lista_rest(Model m, String fragment,HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		
		
		m.addAttribute("utilizadores",service.findAll());
		
		m.addAttribute("fragment",fragment);

		
		//funcoes.sendEmailReset();
		
		return "main.html";
	}
	
	
	@GetMapping("/delete_utilizadores")
	public String apagar(Login l, String id) {
		
		for(Login ll: service.findAll()) {
			if(ll.getId().equals(id)) {
				service.delete(ll);
			}
		}
		
		return "redirect:/listagem_utilizadores?fragment=listagem_users";
	}
	
	
	@PostMapping("/editar_utilizadores")
	public String editar(Model m,Login l, String id, String nome, String fragment) {
	
		for(Login ll: service.findAll()) {
			if(ll.getId().equals(id)) {
				m.addAttribute("utilizador",ll); //receber no th os dados deste objeto que tiver o mesmo id
				m.addAttribute("fragment",fragment);
			}
		}
		
		
		return "main.html";
	}
	
	@PostMapping("/procurar_utilizadores")
	public String proc(Model m,Login l,String search) {
		ArrayList<Login> arrutilizadores= new ArrayList<>();
		
		for(Login ll: service.findAll()) {
			if(ll.getNome().startsWith(search)) {
				arrutilizadores.add(ll);
						
				
			}
		}
		m.addAttribute("utilizadores",arrutilizadores);
		
		return "listar_utilizadores.html";
	}
	
	
	//Listar todos os utilizadores da aplicação
	
	@GetMapping("/lista_users_app")
	public String lista_app(Model m, String fragment, HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		ArrayList<Login> usersapp = new ArrayList<>();
		for(Login ll: service.findAll()) {
			
			if(ll.getTipo().equals("3")) {
				usersapp.add(ll);
				System.out.println("aquiii");
			}
		}

		m.addAttribute("fragment",fragment);
		m.addAttribute("utilizadores",usersapp);	
		
		return "main.html";
	}
	
	@GetMapping("/filtra")
	public String filta_user(Model m, String fragment,String search) {
		
		m.addAttribute("fragment",fragment);
		
		ArrayList<Login> arrutilizadores= new ArrayList<>();
		
		for(Login ll: service.findAll()) {
			if(ll.getNome().toLowerCase().startsWith(search.toLowerCase())) {
				arrutilizadores.add(ll);
						
				m.addAttribute("utilizadores",arrutilizadores);
				
			}
		}
		
		return "main.html";
	}



}
