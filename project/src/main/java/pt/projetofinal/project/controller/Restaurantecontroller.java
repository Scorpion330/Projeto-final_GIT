package pt.projetofinal.project.controller;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import pt.projetofinal.project.model.Login;
import pt.projetofinal.project.model.Restaurante;
import pt.projetofinal.project.service.Loginrepository;
import pt.projetofinal.project.service.Restauranterepository;

@Controller
public class Restaurantecontroller {
	
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";
	
	@Autowired
	Restauranterepository service;
	@Autowired
	Loginrepository service_login;

	//Manda para o painel do admin
	@GetMapping("/painel")
	public String inicio(Model m, String fragment,HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		m.addAttribute("fragment",fragment);
		
		return "main.html";
	}
	
	@GetMapping("/introduzir_restaurante")
	public String intr_rest(Model m,String fragment,HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		m.addAttribute("fragment",fragment);
	
		return "main.html";
	}
	
	/*@GetMapping("/listar_restaurante")
	public String lista() {
	
		return "listar_rest.html";
	}*/
	
	@PostMapping("/resgistar_restaurante") 
	public String addrestaurante(Model m, Restaurante r/*, String id,String nome,String longitude, String latitude, String descricao*/,HttpSession request,String categoria,String fragment,String dias,@RequestParam(value="files",defaultValue="null") MultipartFile[] files) {
		String imagem,
		categor;
		
		Login l = (Login)request.getAttribute("user"); //o objeto vai ser igual aos atributos do user que vem do httpsession request (chamar)
		
		if(l==null) {return "redirect:/login";}
		
		ArrayList<String> dia_sem = new ArrayList<>();
		
		m.addAttribute("fragment",fragment);
		
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
		//m.addAttribute("msg","Sucesso "+fileNames.toString());
		//return "uploadstatusview";

			
		for(Restaurante rr:service.findAll()) {
			if(rr.getId().equals(r.getId())) {
				
				
				System.out.println(categoria);
				if(categoria.isEmpty()) {
					//System.out.println("estou aqui");
					categor=rr.getCategoria();
					r.setCategoria(categor);
				}
				else {
					System.out.println("categoria "+categoria);
					System.out.println("categor:" +rr.getCategoria());
					r.setCategoria(categoria);
				}
	
				
				imagem=rr.getPicture();
				r.setPicture(imagem);
				service.save(r);
			
			}
		}
			
			imagem="/uploads/"+fileNames; //para o adicionar normal
			r.setPicture(imagem);
			
			
			
			
			//r.setArDias_Semana(Monday);
			dia_sem.add(dias);
			r.setArDias_Semana(dia_sem);
			
			service.save(r);
	
		}else {
			for(Restaurante rr:service.findAll()) {
				if(rr.getId().equals(r.getId())) {
					
					
					System.out.println(categoria);
					if(categoria.isEmpty()) {
						System.out.println("estou aqui");
						categor=rr.getCategoria();
						r.setCategoria(categor);
					}
					else {
						System.out.println("categoria "+categoria);
						System.out.println("categor:" +rr.getCategoria());
						r.setCategoria(categoria);
					}
				
					
					
					imagem=rr.getPicture();
					r.setPicture(imagem);
					service.save(r);
				
				}
			}
			
		}
		return "redirect:/painel?fragment=painel_admin"; //falta redirect para o painel do admin
		
	}

	@GetMapping("/listagem")
	public String lista_rest(Model m,Restaurante r, String fragment,HttpSession request) {
		
		
		Login l = (Login)request.getAttribute("user"); //o objeto vai ser igual aos atributos do user que vem do httpsession request (chamar)
		
		if(l==null) {return "redirect:/login";} //se a sessao nunca for iniciada
		
		double val = 0.0;
		
		//Restaurante estrela = new Restaurante();
		
		ArrayList<Restaurante> arrestaurante = new ArrayList<>();
		for(Restaurante rr: service.findAll()) {
			double valorarr = 0.0;
			
			

			valorarr= Double.parseDouble(rr.getRating());
			System.out.println("double"+valorarr);
			
			val = Math.round(valorarr/0.5)*0.5;
			System.out.println("ver o que ta adar "+val);
			String star = Double.toString(val);
			rr.setRating(star);
			System.out.println("startttt"+star);
			service.save(rr);//salvar cada objecto com a nova star.

		}
		
		
	
		m.addAttribute("restaurantes",service.findAll());
		
		m.addAttribute("fragment",fragment);

		
		return "main.html";
	}
	
	@GetMapping("/delete_rest")
	public String apagar(Restaurante r, String id,HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		for(Restaurante rr: service.findAll()) {
			if(rr.getId().equals(id)) {
				service.delete(rr);
			}
		}
		
		return "redirect:/listagem?fragment=listar_rest";
	}
	
	@PostMapping("/editar")
	public String editar(Model m,Restaurante r, String id, String nome,String fragment,HttpSession request) {
		
	/*	Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}*/
	
		for(Restaurante rr: service.findAll()) {
			if(rr.getId().equals(id)) {
				m.addAttribute("restaurante",rr); //receber no th os dados deste objeto que tiver o mesmo id
				m.addAttribute("fragment",fragment);
			}
		}
		
		
		return "main.html";
	}
	
	
	@PostMapping("/procurar")
	public String proc(Model m,String search) {
		boolean valida= false;
		System.out.println("ola1");
		for(Login l : service_login.findAll()) {
			if(l.getNome().toLowerCase().equals(search.toLowerCase())) {
				System.out.println("ola2");
				valida = true;
				if(l.getPassword().isEmpty()) {
					System.out.println("rest");
					return "redirect:/proc_rest?fragment=listar_rest&search="+search; //restaurantes
				}else {
					System.out.println("user");
					return "redirect:/filtra?fragment=listagem_users&search="+search; //utilizadores
				}
			}
		}
		if (valida == false) {
			System.out.println("ola3");
			for (Restaurante rr : service.findAll()) {
				if(rr.getNome().equals(search)) {
					return "redirect:/proc_rest?fragment=listar_rest&search="+search;
				}
			}
		}
		
		return "main.html";
	}
	
	
	@GetMapping("/procurar_categoria")
	public String proc_cate(Model m,String categoria) {
		
		
		//m.addAttribute("fragment",fragment);
		
		//M.ADDATTRIBUTE NAO FUNCIONA COM O REDIRECT (PERDE OS VALORES PELO CAMINHO)
		
		return "redirect:/filtra_categor?fragment=listar_rest&categoria="+categoria;
	}
	
	//procurar por nome de restaurante
	@GetMapping("/proc_rest")
	public String proc_rest(Model m,String search,String fragment,HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		m.addAttribute("fragment",fragment);
		
		ArrayList<Restaurante> arrestaurantes= new ArrayList<>();
		
		for(Restaurante rr: service.findAll()) {
			if(rr.getNome().startsWith(search)) {
				arrestaurantes.add(rr);
					//System.out.println("entrei");	
				m.addAttribute("restaurantes",arrestaurantes);		
			}
		}
		//return "redirect:/listagem_utilizadores?fragment=listagem_users&search="+search;
	
		return "main.html";
	}
	
	@GetMapping("/filtra_categor")
	public String filta(Model m,String categoria,String fragment,HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		String todos = "Todos Restaurantes";
		
		//procurar por categoria(vêm do redirect do /procurar_categoria)
		ArrayList<Restaurante> arrest = new ArrayList<>();
		
		for(Restaurante rr: service.findAll()) {
			if(rr.getCategoria().compareTo(categoria)!=0) {
				m.addAttribute("restaurantes",arrest);
			}else if(rr.getCategoria().equals(categoria)){
				arrest.add(rr);
				m.addAttribute("restaurantes",arrest);
			
			}
			
			if(categoria.equals(todos)){
				m.addAttribute("restaurantes",service.findAll());
			}
		}
		
		m.addAttribute("fragment",fragment);
		
		return "main.html";
	}
	

}
