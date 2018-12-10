package pt.projetofinal.project.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import pt.projetofinal.project.model.Login;
import pt.projetofinal.project.model.Restaurante;
import pt.projetofinal.project.service.Loginrepository;

@Controller
public class Registoscontroller {
	
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";
	
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
	public String add(Login ll,HttpSession request,@RequestParam(value="files",defaultValue="null") MultipartFile[] files) { //receber o modelo inteiro
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		String imagem;
		
		Funcoes.sendEmailReset("hencarnacao@sapo.pt"); // email do objeto
		System.out.println(String.valueOf(files.length)+"   "+files[0].getSize());
		if(files[0].getSize()>0) {
		StringBuilder fileNames = new StringBuilder();
		for(MultipartFile file : files) {
			Path fileNameAndPath = Paths.get(uploadDirectory,file.getOriginalFilename());
			fileNames.append(file.getOriginalFilename()+" ");
			try {
				Files.write(fileNameAndPath,file.getBytes());
			}catch (IOException e) {
				e.printStackTrace();
			}			
		}
		
		imagem="/uploads/"+fileNames; //para o adicionar normal
		ll.setFoto(imagem);

		service.save(ll);
		
		if(ll.getTipo().equals("1")) {
			//System.out.println("OLA SOU UM DONO");
		return "redirect:/introduzir_restaurante?fragment=introduzir_rest";
		}else {
			return "redirect:/painel?fragment=painel_admin";
		}
		
	}else { //ENTRA AQUI SE NAO METER FOTO
		imagem="/uploads/default.png";
		ll.setFoto(imagem);
		
		service.save(ll);
		
			if(ll.getTipo().equals("1")) {
			
			return "redirect:/introduzir_restaurante?fragment=introduzir_rest";
			}else {
			return "redirect:/painel?fragment=painel_admin";
		}
	}
		/*if(ll.getTipo().equals("1")) {
			return "redirect:/painel?fragment=painel_admin";
		}*/
		
	}
	
	
	@GetMapping("/listagem_utilizadores")
	public String lista_rest(Model m, String fragment,HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		ArrayList<Login> artipo = new ArrayList<>();
		//System.out.println(l.getFoto());
		for(Login ll: service.findAll()) {
			if(ll.getTipo().compareTo("3")!=0) {
				artipo.add(ll);
			}
		}
		
		m.addAttribute("utilizadores",artipo);
		
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
		m.addAttribute("fragment",fragment);
		
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
				//System.out.println("aquiii");
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
	
	@GetMapping("/minha_conta")
	public String conta(Login l,Model m,String fragment,String id) {
		System.out.println("id"+id);
		for(Login ll: service.findAll()) {
			if(ll.getId().equals(id)) {
				//System.out.println("hweeeeeeeeeeeeeeeee");
				m.addAttribute("conta",ll); 
				m.addAttribute("fragment",fragment);
			}
			
		}
		
		
		
		return "main.html";
	}



}
