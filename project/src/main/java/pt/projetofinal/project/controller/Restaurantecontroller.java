package pt.projetofinal.project.controller;

import java.io.IOException;
import java.lang.reflect.Array;
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

import pt.projetofinal.project.files.FileHandler;
import pt.projetofinal.project.files.FileHandler.UploadFileResponse;
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
	

	@Autowired
	FileHandler filehandler;


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
	public String addrestaurante(Model m, Restaurante r/*, String id,String nome,String longitude, String latitude, String descricao*/,HttpSession request,String categoria,String fragment,String email_dono,String dia1,String dia2,String dia3,String dia4,String dia5,String dia6,String dia7,String srest,String stakeaway,String sencomenda,String sreserva,String hora_entrada,String min_entrada, String hora_saida,String min_saida,@RequestParam(value="files",defaultValue="null") MultipartFile[] files) {
		String imagem,
		categor,rat,hora_inicio,hora_fim;
		
		Login l = (Login)request.getAttribute("user"); //o objeto vai ser igual aos atributos do user que vem do httpsession request (chamar)
		
		if(l==null) {return "redirect:/login";}
		
		UploadFileResponse response = null;
		
		ArrayList<String> temp = new ArrayList<>();
		
		ArrayList<String> arserv = new ArrayList<>();
		
		ArrayList<String> dia_sem = new ArrayList<>();
		
		m.addAttribute("fragment",fragment);
		
		System.out.println(String.valueOf(files.length)+"   "+files[0].getSize());
		if(files[0].getSize()>0) {
			
			response = filehandler.saveFile(files[0]);
			
			r.setPicture(response.getFileDownloadUri());
			
			
		for(Restaurante rr:service.findAll()) {//para o editar
			if(rr.getId().equals(r.getId())) {
				
				System.out.println("id ?"+r.getId());
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
				
				r.setId_dono(rr.getId_dono());
								
				imagem=rr.getPicture();
				r.setPicture(imagem);
				
				temp.add(dia1);
				temp.add(dia2);
				temp.add(dia3);
				temp.add(dia4);
				temp.add(dia5);
				temp.add(dia6);
				temp.add(dia7);
				//r.getArDias_Semana().add(dia1);
				r.setArDias_Semana(temp);
				
				hora_inicio=hora_entrada+":"+min_entrada;
				hora_fim=hora_saida+":"+min_saida;
				
				r.setHorario_abrir(hora_inicio);
				r.setHorario_fechar(hora_fim);
		
				/*for(String dias : rr.getArDias_Semana()) {
					temp.add(dias);
					System.out.println("dias"+dias);
					System.out.println("array"+temp);
					
				}
				System.out.println("array"+temp);
				r.setArDias_Semana(temp);
				System.out.println("ooo "+ r.getArDias_Semana());*/
				
				service.save(r);
				//System.out.println("dep save "+ r.getArDias_Semana());
			}
		}
			
			/*imagem="/uploads/"+fileNames; //para o adicionar normal
			r.setPicture(imagem);*/
		
			imagem=response.getFileDownloadUri();
			r.setPicture(imagem);
			System.out.println("ifsdsdsdggsd "+imagem);
			
			
			for(Login ll: service_login.findAll()) {
				System.out.println("supp");
				if(ll.getEmail().equals(email_dono)) {
					System.out.println("Nome do Dono "+ll.getNome());
					r.setId_dono(ll.getId());
				}
				
			}
			
			dia_sem.add(dia1);
			dia_sem.add(dia2);
			dia_sem.add(dia3);
			dia_sem.add(dia4);
			dia_sem.add(dia5);
			dia_sem.add(dia6);
			dia_sem.add(dia7);
			//r.getArDias_Semana().add(dia1);
			r.setArDias_Semana(dia_sem);
			
			arserv.add(srest);
			arserv.add(stakeaway);
			arserv.add(sencomenda);
			arserv.add(sreserva);
			
			System.out.println("servicos antes "+arserv);
			r.setArServico(arserv);
			System.out.println("servicos "+arserv);
			
			hora_inicio=hora_entrada+":"+min_entrada;
			hora_fim=hora_saida+":"+min_saida;
			
			r.setHorario_abrir(hora_inicio);
			r.setHorario_fechar(hora_fim);
			
			
			r.setRating("0.0");
			
			Funcoes.sendEmailReset("pedroalex.vicente@hotmail.com"); // email do objeto
			System.out.println("email com img");
			service.save(r);
	
		}else {
			for(Restaurante rr:service.findAll()) {
				if(rr.getId().equals(r.getId())) {
					
					
					System.out.println(categoria);
					if(categoria.isEmpty()) {
						System.out.println("estou aqui");
						categor=rr.getCategoria();
						r.setCategoria(categor);
						System.out.println(categor);
					}
					else {
						System.out.println("categoria "+categoria);
						System.out.println("categor:" +rr.getCategoria());
						r.setCategoria(categoria);
					}
				
					r.setId_dono(rr.getId_dono());
	
					/*imagem=rr.getPicture();
					r.setPicture(imagem);*/
					imagem = rr.getPicture();
					r.setPicture(imagem);
					
					//PODE SER NECESSARIO
					/*String fn = ll.getFoto().substring(ll.getFoto().indexOf("File/")+5, ll.getFoto().length());
					System.out.println(fn);*/
					
					rat=rr.getRating();
					r.setRating(rat);
					//r.setRating("0");
					
				/*	for(Login ll: service_login.findAll()) {
						System.out.println("supp");
						if(ll.getEmail().equals(email_dono)) {
							System.out.println("Nome do Dono "+ll.getNome());
							r.setId_dono(ll.getId());
						}
						
					}*/
					
					temp.add(dia1);
					temp.add(dia2);
					temp.add(dia3);
					temp.add(dia4);
					temp.add(dia5);
					temp.add(dia6);
					temp.add(dia7);
					//r.getArDias_Semana().add(dia1);
					r.setArDias_Semana(temp);
					//Funcoes.sendEmailReset("pedroalex.vicente52@gmail.com"); // email do objeto
					System.out.println("email sem img");
					
					arserv.add(srest);
					arserv.add(stakeaway);
					arserv.add(sencomenda);
					arserv.add(sreserva);
					
					r.setArServico(arserv);
					
					hora_inicio=hora_entrada+":"+min_entrada;
					hora_fim=hora_saida+":"+min_saida;
					
					r.setHorario_abrir(hora_inicio);
					r.setHorario_fechar(hora_fim);
					System.out.println(r.getHorario_abrir());
					System.out.println(r.getHorario_fechar());
					
					
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
				m.addAttribute("arr",rr.getArDias_Semana());
				m.addAttribute("arrserv",rr.getArServico());
				System.out.println("VER "+rr.getArDias_Semana());
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
				if(rr.getNome().compareToIgnoreCase(search)==0) {
					System.out.println("nomeee "+rr.getNome());
					System.out.println("search "+search);
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
			if(rr.getNome().toLowerCase().startsWith(search.toLowerCase())) {
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
	
	@GetMapping("/local_rest")
	public String map(Model m, String fragment,HttpSession request) {
		
		Login l=(Login)request.getAttribute("user");
		
		if(l==null) {return "redirect:/login"; }
		
		m.addAttribute("fragment",fragment);
		
		return "main.html";
	}
	
	@GetMapping("/ver_id")
	public String ver(Model m,String fragment, String email_dono, HttpSession request) {
		


		
		
		
		return "main.html";
	}
	

}
