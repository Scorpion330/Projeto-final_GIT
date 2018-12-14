package pt.projetofinal.project.controller;

import java.util.Optional;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.projetofinal.project.model.Login;
import pt.projetofinal.project.service.Loginrepository;

@Controller
public class Logincontroller {
	
	@Autowired
	Loginrepository service;
	
	//PÁGINA DE ERRO GERAL PARA TUDO
	@GetMapping("/error")
	public String erro() {
						
		return "error.html";
	}

	@RequestMapping(value="/inicio",method=RequestMethod.GET)
		public String inicio(Model m,String fragment) {
		
		m.addAttribute("fragment",fragment); //mandar para o html com o nome "fragment" no index.html
		
			return "main.html";
		}
	
	@GetMapping("/login")
	public String login() {
	
		return "login.html";
	}
	
	@GetMapping("/registar")
	public String registo() {
	
		return "register.html";
	}
	
	
	@PostMapping("/registar_conta") //clicar em registar (register.html)
	public String adduser(@ModelAttribute("registo")Login l, Model m, String username,String password,String email) { //receber o modelo inteiro
		if(username.isEmpty() || password.isEmpty() || email.isEmpty()) {
			System.out.println("Nao introduziu dados");
			//dar return de pagina de erro
		}else {
			for(Login ll: service.findAll()) {
				if(ll.getUsername().equals(username) || ll.getEmail().equals(email)) {
					System.out.println("Username ou email já existe");
				}
			}
			
			service.save(l);
			m.addAttribute("registo", l);	//usado para listas.
		}
		return "index.html";
	}
	
	@RequestMapping(value="/painel_admin", method=RequestMethod.POST)//clicar em login (login.html)
	public String login(Model m,String password, String username, String fragment,HttpSession session) {
		
		
		m.addAttribute("teste",service.findAll());
		
		for(Login l: service.findAll()) {
			

			if(l.getUsername().equals(username) && l.getPassword().equals(password)) {
				System.out.println(l.getTipo()+"tipo");
				if(l.getTipo().equals("0")) {
					session.setAttribute("user", l);
					return "redirect:/inicio?fragment=painel_admin";
				}else{
					System.out.println(l.getTipo()+"tipo else");
					session.setAttribute("user", l);
					System.out.println("ola");
					return "redirect:/listagem?fragment=listar_rest";
				}
				
				
				//session.setAttribute("id", l.getId());
				
			/*	if(l.getTipo().equals("3")) {
					System.out.println("ola");
					return "redirect:/listagem_utilizadores?fragment=listagem_users";
				}		*/
				
			}
			
			
			
			
		}	

		//return "error.html"; //DAR RETURN PARA ERRO
		return "redirect:/login?err=fail";
	}
	
		//Login u=(Login).session.geta
     /*   
        Optional<User> us = userRepo.findByHashes(fuser.login(u.getEmail(), password));
        
        if(us.isPresent()) {
            


            //page.addAttribute("User",us.get());
            session.setAttribute("User", us.get());

            
            
            page.addAttribute("User",us.get());


            return "redirect:/feed?frag=feed";
            
        }
        
        
        page.addAttribute(toast);

        return "redirect://"+WebServices.SERVER;   
    }*/
	@GetMapping("/logout")
	public String sair(HttpSession session) {
		
		Login l = (Login)session.getAttribute("user");
		session.invalidate();
		
		return "redirect:/login";	
	}
	
	@GetMapping("/erro_session")
	public String session() {
		
		
		return "login.html";
	}
	
	
	
	
}
