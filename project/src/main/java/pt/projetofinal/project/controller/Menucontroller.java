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

import pt.projetofinal.project.files.FileHandler;
import pt.projetofinal.project.files.FileHandler.UploadFileResponse;
import pt.projetofinal.project.model.Login;
import pt.projetofinal.project.model.Menu;
import pt.projetofinal.project.model.Restaurante;
import pt.projetofinal.project.service.Loginrepository;
import pt.projetofinal.project.service.Menurepository;
import pt.projetofinal.project.service.Restauranterepository;

@Controller
public class Menucontroller {
	
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";
	
	@Autowired
	Menurepository svmenu;
	
	@Autowired
	Restauranterepository svrestaurante;
	
	@Autowired
	Loginrepository svlogin;
	
	@Autowired
	FileHandler filehandler;
	
	@GetMapping(value="/addmenu")
	public String addmenu(Model m, String fragment, HttpSession session) {
		
		ArrayList<Restaurante> res = new ArrayList<>();
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("1")!=0) {return "redirect:/login";}
		
		System.out.println("Estou no addmenu");
		
		for(Restaurante re: svrestaurante.findAll()) {
			System.out.println("estou no for");
			System.out.println("id dono"+re.getId_dono()+"o id "+u.getId());
			
			if(re.getId_dono().compareTo(u.getId())==0) {
				System.out.println("Estou a adicionar");
				res.add(re);
				
			}
			
		}
		
		m.addAttribute("listrestaurantes",res);
		m.addAttribute("fragment",fragment);
		System.out.println("Passo primeiro por aqui");
		return "mainownerprofile.html";
	}
	
	@PostMapping(value="/acceptmenu")
	public String addm(Menu mm, @RequestParam("files") MultipartFile[] files, String categoria, HttpSession session) {
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("1")!=0) {return "redirect:/login";}
		
		UploadFileResponse response = null;
		
		System.out.println("id enviado "+mm.getId_restaurante());
		
		response = filehandler.saveFile(files[0]);
		
		mm.setPicture(response.getFileDownloadUri());
		
		String pic=response.getFileDownloadUri();
		
		mm.setPicture(pic);
		//mm.setId_restaurante(u.getId_restaurante());
		svmenu.save(mm);
		return "redirect:/profileowner?fragment=profileowner";
	}
	
	@GetMapping(value="/listarmenu")
	public String listmenu(Model m, String fragment, HttpSession session) {
		
		System.out.println("Oi boy estou no listar");
		
		Login u = (Login)session.getAttribute("user");
		
		System.out.println("estou aqui no listar");
		System.out.println("usuario "+u.getTipo());
		
		if(u==null || u.getTipo().compareTo("1")!=0 && u.getTipo().compareTo("2")!=0) {return "redirect:/login";}
		
		ArrayList<Menu> armenu = new ArrayList<>();
		
		for(Restaurante re: svrestaurante.findAll()) {
			System.out.println("a");
			if(u.getId().compareTo(re.getId_dono())==0 || u.getId_restaurante().compareTo(re.getId())==0) {
				System.out.println("b");
				for(Menu me: svmenu.findAll()) {
					System.out.println("c");
					System.out.println("me id restaurante: "+me.getId()+" re id: "+re.getId());
					if(me.getId_restaurante().compareTo(re.getId())==0 && u.getId().compareTo(re.getId_dono())==0) {
						System.out.println("d");
						armenu.add(me);
						
					}
					System.out.println(" id restaurante "+u.getId_restaurante()+" me id "+me.getId_restaurante());
					 if(u.getId_restaurante().compareTo(me.getId_restaurante())==0) {
						
						armenu.add(me);
					}	
				}
			}
		}
		
		
		m.addAttribute("fragment", fragment);
		m.addAttribute("menu",armenu);
		
		System.out.println("I am indeed here");
		
		if (u.getTipo().equals("1")) {
			return "mainownerprofile.html";
		}
		else if(u.getTipo().equals("2")) {
			return "mainempprofile.html";
		}
		
		return "redirect:/login";
		
	}
	
	@GetMapping(value="/deletemenu")
	public String delmenu(String id) {
		
		for(Menu m: svmenu.findAll()) {
			
			if(m.getId().compareTo(id)==0) {
				
				svmenu.delete(m);		
			}
		}
		return "redirect:/listarmenu?fragment=listar_menu";
	}
	
	@PostMapping(value="/editmenu")
	public String editmenu(Model m, String id,String fragment, HttpSession session) {
		
		Login u = (Login)session.getAttribute("user");
		
		ArrayList<Restaurante> res = new ArrayList<>();
		
		//if(u==null || u.getTipo().compareTo("1")!=0) {return "redirect:/login";}
		
		System.out.println("1");
		for(Menu t: svmenu.findAll()) {
			System.out.println("2");
			if(t.getId().compareTo(id)==0) {
				System.out.println("3");
				m.addAttribute("pratos",t);
			}
			
		}
		
		for(Restaurante re: svrestaurante.findAll()) {
			System.out.println("the begining with yo");
			if(re.getId_dono().compareTo(u.getId())==0) {
				System.out.println("bye bye aqyi");
				res.add(re);
				
			}
			
		} 
		
		m.addAttribute("listrestaurantes",res);
		m.addAttribute("fragment",fragment);
		return "mainownerprofile.html";
	}
	
	@PostMapping(value="/editarmenu")
	public String editarmenu(Menu mm, @RequestParam("files") MultipartFile[] files,String id, String categoria) {
		System.out.println("pooof");
		
		
		String categor, picture;
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
			
			
			for(Menu t: svmenu.findAll()) {
							
						if(t.getId().compareTo(id)==0) {
							
							if(categoria.isEmpty()) {
								System.out.println("Here 1");
								categor=t.getCategoria();
								mm.setCategoria(categor);
							}
								
							else {
								System.out.println("Here 2");
								mm.setCategoria(categoria);
							}
								
							picture=t.getPicture();
							mm.setPicture(picture);
							svmenu.save(mm);
							
						}
							
					}
		}
		
		else {
			
			for(Menu t: svmenu.findAll()) {
				
				if(t.getId().compareTo(id)==0) {
					
					if(categoria.isEmpty()) {
						System.out.println("Here 3");
						categor=t.getCategoria();
						mm.setCategoria(categor);
					}
					
					else {
						System.out.println("Here 4");
						mm.setCategoria(categoria);
					}
					
					
					
					picture=t.getPicture();
					mm.setPicture(picture);
					svmenu.save(mm);
					
				}
				
			}
			
		}
		return "redirect:/listarmenu?fragment=listar_menu";
		
	}
	
	@GetMapping(value="filt_menu")
	public String filtmenu(Model m, String cat, HttpSession session) {
		
		Login u = (Login)session.getAttribute("user");
		
		if(u.getTipo().equals("1")) {
			return "redirect:/menu_cat?fragment=listar_menu&categoria="+cat;
		}
		else if(u.getTipo().equals("2")) {
			return "redirect:/menu_cat?fragment=listarmenu_empregado&categoria="+cat;
		}
		
		return "redirect:/login";
	}
	
	
	@GetMapping(value="/menu_cat")
	public String menucat(Model m, String categoria, String fragment, HttpSession session) {
		
		System.out.println("Me categoria "+categoria);
		
		ArrayList<Menu> armenu = new ArrayList<>();
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("1")!=0 && u.getTipo().compareTo("2")!=0) {return "redirect:/login";}
		
		for(Menu mm: svmenu.findAll()) {
			
			if (categoria.compareTo("Todos")==0 && u.getTipo().compareTo("1")==0) {
				
				for(Restaurante re: svrestaurante.findAll()) {
					
					if(re.getId().compareTo(mm.getId_restaurante())==0) {
						
						if (re.getId_dono().compareTo(u.getId())==0) {
							System.out.println("nome "+mm.getNome()+" re id: "+re.getId()+" mm id "+mm.getId_restaurante()+" u id "+u.getId()+" id dele proprio "+mm.getId_restaurante());
							armenu.add(mm);
							
						}
						
					}
					
				}
				
				
				//m.addAttribute("menu",svmenu.findAll());
			}else if (categoria.compareTo("Todos")==0 && u.getTipo().compareTo("2")==0) {
						
					if(u.getId_restaurante().compareTo(mm.getId_restaurante())==0) {
						armenu.add(mm);
						
						
					}
					
				}
			
		}	
				
			
			for(Restaurante tt: svrestaurante.findAll()) {
				
				for(Menu me: svmenu.findAll()) {
					System.out.println("menu "+me.getNome());
					System.out.println("cat: "+categoria+" me categoria "+me.getCategoria());
					
					if(me.getCategoria().compareToIgnoreCase(categoria)==0 && tt.getId().compareTo(me.getId_restaurante())==0) {
						System.out.println("oi");
						if(u.getTipo().equals("1")) {
							
							if (tt.getId_dono().compareTo(u.getId())==0) {
								
								armenu.add(me);
								//m.addAttribute("menu",armenu);
							}
						}
						
						else if(u.getTipo().equals("2")) {
							
							if (me.getId_restaurante().compareTo(u.getId_restaurante())==0) {
								armenu.add(me);
								
							}
							
						}			
						
						//	System.out.println("Ok but I'm here m8 "+tt.getNome());				
							
							}	
						}	
				}
			
			m.addAttribute("menu",armenu);
			m.addAttribute("fragment",fragment);
			
			if(u.getTipo().equals("1")) {
				return "mainownerprofile.html";
			}
			else if(u.getTipo().equals("2")) {
				return "mainempprofile.html";
			}
		
			return "redirect:/login";
		
	}
	
	@GetMapping(value="/naprocurar")
	public String procevery(Model m, String search, String fragment, HttpSession session) {
		
		Login u = (Login)session.getAttribute("user");
		ArrayList<Menu> armenu = new ArrayList<>();
		ArrayList<Login> arlogin = new ArrayList<>();
		
		
		for(Restaurante re: svrestaurante.findAll()) {
			
			if(u.getId().compareTo(re.getId_dono())==0) {
				
				for(Menu me: svmenu.findAll()) {
					
					if (me.getNome().toLowerCase().contains(search.toLowerCase()) && me.getId_restaurante().compareTo(re.getId())==0) {
							
							armenu.add(me);
						}
				}
				
				for(Login lo: svlogin.findAll()) {
					
					if(lo.getId_restaurante().compareTo(re.getId())==0 ) {
						
						if (lo.getTipo().equals("2") && lo.getId_restaurante().compareTo(re.getId())==0 && lo.getNome().toLowerCase().startsWith(search.toLowerCase())) {
							arlogin.add(lo);
						}
						
					}
					
				}
				
			}
			
		}
		
		
		m.addAttribute("fragment",fragment);
		m.addAttribute("menu",armenu);
		m.addAttribute("pessoa",arlogin);
		return "mainownerprofile.html";
		
	}
	
	@PostMapping(value="/nsearch")
	public String nsearchh(Model m, String search, String fragment, HttpSession session) {
		
		String abcz="z";
		
		ArrayList<Login> allogin = new ArrayList<>();
		
		ArrayList<Menu> almenu = new ArrayList<>();
		
		Login u = (Login)session.getAttribute("user");
		
		for(Restaurante re: svrestaurante.findAll()) {
			
			if(u.getId().compareTo(re.getId_dono())==0) {
				
				for(Menu me: svmenu.findAll()) {
					
					for(Login lo: svlogin.findAll()) {
						
						if(lo.getTipo().equals("2") && lo.getId_restaurante().compareTo(re.getId())==0 && lo.getNome().toLowerCase().startsWith(search.toLowerCase())) {
							
							abcz="a";
							allogin.add(lo);
							
						}if (me.getNome().toLowerCase().contains(search.toLowerCase())) {
							abcz="b";
							almenu.add(me);
						}
						
						
					}
					
				}
				
			}
			
		}
		
		for(int i=0;i<allogin.size();i++) {
			
			for(int j=0;j<almenu.size();j++) {
			
				if(almenu.get(j).getNome().toLowerCase().contains(search.toLowerCase()) && (allogin.get(i).getNome().toLowerCase().startsWith(search.toLowerCase()) ||  allogin.get(i).getNome().toLowerCase().endsWith(search.toLowerCase()))){
					 return "redirect:/naprocurar?fragment=naprocurar&search="+search; 
				}
				
			}
		}
		
		
		if(abcz.equals("a")) {
			return "redirect:/proc_empname?fragment=listar_emp&search="+search; 
		}else if(abcz.equals("b")) {
			return "redirect:/proc_menun?fragment=listar_menu&search="+search; 
		}
		
		//TENS QUE TIRAR ISTO DAQUI
		return "redirect:/login";
	}
	
	@GetMapping(value="/proc_empname")
	public String proc_empna(Model m, String fragment, String search, HttpSession session) {
		
		ArrayList<Login> arlogin = new ArrayList<>();
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("1")!=0) {return "redirect:/login";}
		
		for(Restaurante re: svrestaurante.findAll()) {
			
			if(u.getId().compareTo(re.getId_dono())==0) {
				
				for(Login lo: svlogin.findAll()) {
					
					if(lo.getId_restaurante().compareTo(re.getId())==0 && lo.getTipo().equals("2") && lo.getNome().toLowerCase().startsWith(search.toLowerCase())) {
						
						arlogin.add(lo);
						
					}
					
				}
				
			}
		}
		
		
		m.addAttribute("fragment", fragment);
		m.addAttribute("pessoa",arlogin);
		return "mainownerprofile.html";
	}
	
	
	@GetMapping(value="/proc_menun")
	public String proc_menun(Model m, String fragment, String search, HttpSession session) {
		
		ArrayList<Menu> armenu = new ArrayList<>();
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("1")!=0 && u.getTipo().compareTo("2")!=0) {return "redirect:/login";}
		
		for(Restaurante re: svrestaurante.findAll()) {
			
			if(u.getId().compareTo(re.getId_dono())==0) {
				
				for(Menu me: svmenu.findAll()) {
					
					if(me.getId_restaurante().compareTo(re.getId())==0 && me.getNome().toLowerCase().contains(search.toLowerCase())) {
						
						armenu.add(me);
						
					}
					
				}
				
			}
			
		}
		
		m.addAttribute("menu",armenu);
		m.addAttribute("fragment",fragment);
		return "mainownerprofile.html";
	}
	
	
}

