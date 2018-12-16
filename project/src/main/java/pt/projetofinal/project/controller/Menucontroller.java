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
	
	@GetMapping(value="/addmenu")
	public String addmenu(Model m, String fragment, HttpSession session) {
		
		ArrayList<Restaurante> res = new ArrayList<>();
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("1")!=0) {return "redirect:/login";}
		
		System.out.println("Estou no addmenu");
		
		for(Restaurante re: svrestaurante.findAll()) {
			
			if(re.getId_dono().compareTo(u.getId())==0) {
				
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
		
		System.out.println("id enviado "+mm.getId_restaurante());
		
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
		
		String pic="/uploads/"+fileNames;
		mm.setPicture(pic);
		//mm.setId_restaurante(u.getId_restaurante());
		svmenu.save(mm);
		return "redirect:/profileowner?fragment=profileowner";
	}
	
	@GetMapping(value="/listarmenu")
	public String listmenu(Model m, String fragment, HttpSession session) {
		
		Login u = (Login)session.getAttribute("user");
		
		System.out.println("estou aqui no listar");
		System.out.println("usuario "+u.getTipo());
		
		if(u==null || u.getTipo().compareTo("1")!=0 && u.getTipo().compareTo("2")!=0) {return "redirect:/login";}
		
		ArrayList<Menu> armenu = new ArrayList<>();
		
		for(Restaurante re: svrestaurante.findAll()) {
			System.out.println("a");
			if(u.getId().compareTo(re.getId_dono())==0) {
				System.out.println("b");
				for(Menu me: svmenu.findAll()) {
					System.out.println("c");
					System.out.println("me id restaurante: "+me.getId()+" re id: "+re.getId());
					if(me.getId_restaurante().compareTo(re.getId())==0) {
						System.out.println("d");
						armenu.add(me);
						
					}
					
				}
				
				
			}
			
			
			
		}
		
		
		
		m.addAttribute("fragment", fragment);
		m.addAttribute("menu",armenu);
		System.out.println("I am indeed here");
		return "mainownerprofile.html";
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
	public String filtmenu(Model m, String cat) {
		
		
		return "redirect:/menu_cat?fragment=listar_menu&categoria="+cat;
	}
	
	
	@GetMapping(value="/menu_cat")
	public String menucat(Model m, String categoria, String fragment, HttpSession session) {
		
		System.out.println("Me categoria "+categoria);
		
		ArrayList<Menu> armenu = new ArrayList<>();
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("1")!=0) {return "redirect:/login";}
				
			if (categoria.compareTo("t")==0) {
				m.addAttribute("menu",svmenu.findAll());
			}
			
			for(Restaurante tt: svrestaurante.findAll()) {
				
				for(Menu me: svmenu.findAll()) {
					System.out.println("menu "+me.getNome());
					System.out.println("cat: "+categoria+" me categoria "+me.getCategoria());
					
					if(me.getCategoria().compareToIgnoreCase(categoria)==0 && tt.getId_dono().compareTo(u.getId())==0 && tt.getId().compareTo(me.getId_restaurante())==0) {
						
						
							System.out.println("Ok but I'm here m8 "+tt.getNome());
							

							
							armenu.add(me);
							m.addAttribute("menu",armenu);
							
							}	
						}
						
					
				}
		
		
		
		m.addAttribute("fragment",fragment);
		//m.addAttribute("menu",armenu);
		return "mainownerprofile.html";
	}
	
	@GetMapping(value="/naprocurar")
	public String procevery(Model m, String search, String fragment, HttpSession session) {
		
		Login u = (Login)session.getAttribute("user");
		ArrayList<Menu> armenu = new ArrayList<>();
		ArrayList<Login> arlogin = new ArrayList<>();
		
		for(Restaurante re: svrestaurante.findAll()) {
			
			if(u.getId().compareTo(re.getId_dono())==0) {
				
				for(Menu me: svmenu.findAll()) {
					
					for(Login lo: svlogin.findAll()) {
						
						if(me.getId_restaurante().compareTo(re.getId())==0 && lo.getTipo().equals("2") && lo.getId_restaurante().compareTo(re.getId())==0) {
							
							if(lo.getNome().startsWith(search) && me.getNome().contains(search)) {
								
								armenu.add(me);
								arlogin.add(lo);
								
							}
							
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
		//System.out.println("search: "+search);
		
		Login u = (Login)session.getAttribute("user");
		
		for(Restaurante re: svrestaurante.findAll()) {
			
			if(u.getId().compareTo(re.getId_dono())==0) {
				
				for(Menu me: svmenu.findAll()) {
					
					for(Login lo: svlogin.findAll()) {
						//System.out.println("quatro");
						//System.out.println("onde esta o null"+me.getId_restaurante()+" id re: "+re.getId()+" lo type "+lo.getTipo()+" lo id "+lo.getId()+" re id dono "+me.getId_restaurante());
						if(me.getId_restaurante().compareTo(re.getId())==0 && lo.getTipo().equals("2") && lo.getId_restaurante().compareTo(re.getId())==0) {
							//System.out.println("cinco");
							System.out.println("search "+search);
							System.out.println("re nome "+lo.getNome() +" me nome "+me.getNome());
							if(lo.getNome().startsWith(search) && me.getNome().contains(search)) { 
								System.out.println("BRO TEMOS 2 LOL");
								return "redirect:/naprocurar?fragment=naprocurar&search="+search; 
								//break;
								//return "somewhere";
								
							}
							
						}
						//System.out.println("mas vens para aqui ou nao");
						if(lo.getTipo().equals("2") && lo.getId_restaurante().compareTo(re.getId())==0 && lo.getNome().startsWith(search)) {
							
							abcz="a";
							System.out.println("ayy procurando: "+abcz);
							
						}if (me.getNome().contains(search)) {
							abcz="b";
							System.out.println("oyy procurando: "+abcz);
						}
						
						
					}
					
				}
				
			}
			
		}
		
		if(abcz.equals("a")) {
			return "redirect:/proc_empname?fragment=listar_emp&search="+search; 
		}else if(abcz.equals("b")) {
			return "redirect:/proc_menun?fragment=listar_menu&search="+search; 
		}
		
		return "redirect:/menu_cat?fragment=listar_menu&categoria=bebida";
	}
	
	@GetMapping(value="/proc_empname")
	public String proc_empna(Model m, String fragment, String search, HttpSession session) {
		
		ArrayList<Login> arlogin = new ArrayList<>();
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("1")!=0) {return "redirect:/login";}
		
		for(Restaurante re: svrestaurante.findAll()) {
			
			if(u.getId().compareTo(re.getId_dono())==0) {
				
				for(Login lo: svlogin.findAll()) {
					
					if(lo.getId_restaurante().compareTo(re.getId())==0 && lo.getTipo().equals("2") && lo.getNome().startsWith(search)) {
						
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
					
					if(me.getId_restaurante().compareTo(re.getId())==0 && me.getNome().contains(search)) {
						
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

