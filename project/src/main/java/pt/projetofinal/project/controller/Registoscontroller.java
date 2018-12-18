package pt.projetofinal.project.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import pt.projetofinal.project.files.FileHandler;
import pt.projetofinal.project.files.FileHandler.UploadFileResponse;
import pt.projetofinal.project.model.Login;
import pt.projetofinal.project.model.Restaurante;
import pt.projetofinal.project.service.Loginrepository;
import pt.projetofinal.project.service.Restauranterepository;

@Controller
public class Registoscontroller {
	
	public static String uploadDirectory = System.getProperty("user.dir")+"/src/main/resources/static/uploads";
	
	@Autowired
	Loginrepository service;
	@Autowired
	Restaurantecontroller rc;
	
	@Autowired
	Restauranterepository svres;
	
	@Autowired
	FileHandler filehandler;


@GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request) {
        // Load file as Resource
        Resource resource = filehandler.getFileByName(fileName);
        
        

        // Try to determine file's content type
        String contentType = null;
        try {
            contentType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
        } catch (IOException ex) {
           // logger.info("Could not determine file type.");
        }

        // Fallback to the default content type if type could not be determined
        if(contentType == null) {
            contentType = "application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.ACCEPT, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
	
	
	@GetMapping("/introduzir")
	public String registar(Model m,String fragment,HttpSession request) {
		
		Login l = (Login)request.getAttribute("user"); 
		
		if(l==null) {return "redirect:/login";}
		
		m.addAttribute("fragment",fragment);
		return "main.html";
	}
	
	@PostMapping("/registar")
	public String add(Login ll,String email,String username,String id,HttpSession request,@RequestParam(value="files",defaultValue="null") MultipartFile[] files) { //receber o modelo inteiro
		
		Login l = (Login)request.getAttribute("user"); 
		UploadFileResponse response = null;
		
		
		if(l==null) {return "redirect:/login";}
		
		
		
		String imagem;
		
		//Funcoes.sendEmailReset("hencarnacao@sapo.pt"); // email do objeto
		System.out.println(String.valueOf(files.length)+"   "+files[0].getSize());
		if(files[0].getSize()>0) {
			
		
			response = filehandler.saveFile(files[0]);
			
			//System.out.println("nome da foto "+response.getFileName());
			ll.setFoto(response.getFileDownloadUri());
			
		
		for(Login ver: service.findAll()) {
			
			if(ver.getUsername().compareToIgnoreCase(username)==0) {
				System.out.println("Username já existe");
				return "redirect:/painel?fragment=painel_admin";
			}else if(ver.getEmail().equals(email)) {
				System.out.println("Email já existe");
				return "redirect:/painel?fragment=painel_admin";
			}
		
		}
		ll.setId_restaurante("0");
		
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
		
		
		
		//Resource resource = filehandler.getFileByName(response.getFileName());
		
		//imagem=response.getFileName();
		
		for(Login ver: service.findAll()) {
			if(ver.getUsername().compareToIgnoreCase(username)==0) {
				System.out.println("Username já existe");
				return "redirect:/painel?fragment=painel_admin";
			}else if(ver.getEmail().equals(email)) {
				System.out.println("Email já existe");
				return "redirect:/painel?fragment=painel_admin";
			}
		}
		System.out.println("Estou a adicionar na mesma : )");
		
		ll.setId_restaurante("0");
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
	
	@PostMapping("/edit_conta")
	public String editing(Model m, Login l,String id,String fragment,String username,String email,@RequestParam(value="files",defaultValue="null") MultipartFile[] files) {
		
		String img;
		
		UploadFileResponse response = null;
		
		for(Login lo: service.findAll()) {
			if(lo.getId().equals(id)) {
				System.out.println("1 if ID");
				if(lo.getUsername().equals(username)) {
					System.out.println("ola nome igual");
				
					if(lo.getEmail().equals(email)) {
						
						if(files[0].getSize()>0) {
							response = filehandler.saveFile(files[0]);
							
							l.setFoto(response.getFileDownloadUri());
							
							/*img="/uploads/"+fileNames; 
							l.setFoto(img);*/
							
							img=response.getFileDownloadUri();
							l.setFoto(img);
							
							
							service.save(l);
							System.out.println("img nova "+img);
							return "redirect:/painel?fragment=painel_admin";
						}
						
						img=lo.getFoto();
						l.setFoto(img);
						
						service.save(l);
						System.out.println("ola email igual");
						return "redirect:/painel?fragment=painel_admin";
						
					}
				}else if(lo.getEmail().equals(email)) {
					System.out.println("o email é igual ?");
					if(lo.getUsername().equals(username)) {
						System.out.println("ola nome igual2");
						
						if(files[0].getSize()>0) {
							
							response = filehandler.saveFile(files[0]);
							
							l.setFoto(response.getFileDownloadUri());
							
							/*img="/uploads/"+fileNames; 
							l.setFoto(img);*/
							
							img=response.getFileDownloadUri();
							l.setFoto(img);
							
							service.save(l);
							System.out.println("img nova "+img);
							return "redirect:/painel?fragment=painel_admin";
						}
						
						img=lo.getFoto();
						l.setFoto(img);
						
						service.save(l);
						return "redirect:/painel?fragment=painel_admin";
					}
							
				}
				
				System.out.println("2 if");
				System.out.println("lo user: "+lo.getUsername() + " username envi "+username);
				System.out.println("lo user: "+lo.getEmail() + " username envi "+email);
				 if(lo.getUsername().compareToIgnoreCase(username)!=0 || lo.getEmail().compareTo(email)!=0){
					System.out.println("o nome ou o email é diferente ?");
				
							System.out.println("username ll "+lo.getUsername());
							if(lo.getUsername().compareToIgnoreCase(username)==0) {
								System.out.println("deu merda o nome");
								if(lo.getEmail().equals(email)){
									System.out.println("primeiro else");
									
									if(files[0].getSize()>0) {
										
										response = filehandler.saveFile(files[0]);
										
										l.setFoto(response.getFileDownloadUri());
										
										img=response.getFileDownloadUri();
										l.setFoto(img);
										
										service.save(l);
										System.out.println("img nova "+img);
										return "redirect:/painel?fragment=painel_admin";
									}
									
									System.out.println("foi parar aqui ?");
									img=lo.getFoto();
									l.setFoto(img);
									service.save(l);
									return "redirect:/painel?fragment=painel_admin";
								}
								else if(!lo.getEmail().equals(email)) {
									System.out.println("heyyyyyyyyyyyyyyyyy");
									int cont=0;
									for(Login po: service.findAll()) {
									
										if(po.getEmail().equals(email)) {
											cont=1;
											
										}
									}
									if(cont==0) {
										
										if(files[0].getSize()>0) {
											
											response = filehandler.saveFile(files[0]);
											
											l.setFoto(response.getFileDownloadUri());
											
									
											
											service.save(l);
											//System.out.println("img nova "+img);
											return "redirect:/painel?fragment=painel_admin";
										}else {
	
											
											img=lo.getFoto();
											l.setFoto(img);
											service.save(l);
											//System.out.println("img nova "+img);
											return "redirect:/painel?fragment=painel_admin";
										}
										
								
										
									}else if(cont==1){
										
										
										return "main.html";
									}
								}
									
																
								
								else if (!lo.getUsername().equals(username) && l.getId().compareTo(lo.getId())==0) {
									System.out.println("USERNAME DIF E ID = ID");
									if(files[0].getSize()>0) {
										
										response = filehandler.saveFile(files[0]);
										
										l.setFoto(response.getFileDownloadUri());
										
										
										img=response.getFileDownloadUri();
										l.setFoto(img);
										
										service.save(l);
										System.out.println("img nova "+img);
										return "redirect:/painel?fragment=painel_admin";
									}
									
									img=lo.getFoto();
									l.setFoto(img);
									service.save(l);
									return "redirect:/painel?fragment=painel_admin";
								}
							} 
							//System.out.println("ll mail "+lo.getEmail());
							if(lo.getEmail().compareToIgnoreCase(email)==0) {
								System.out.println("deu merda o email");
								if (lo.getUsername().equals(username)){
									System.out.println("segundo else");
									
									
									if(files[0].getSize()>0) {
										
										response = filehandler.saveFile(files[0]);
										
										l.setFoto(response.getFileDownloadUri());
										
										img=response.getFileDownloadUri();
										l.setFoto(img);
										
										service.save(l);
										System.out.println("img nova "+img);
										return "redirect:/painel?fragment=painel_admin";
									}
									
									img=lo.getFoto();
									l.setFoto(img);
									service.save(l);
									return "redirect:/painel?fragment=painel_admin";
								}else if(!lo.getUsername().equals(username)) {
									System.out.println("hey 2");
									int cont=0;
									for(Login po: service.findAll()) {
									
										if(po.getUsername().equals(username)) {
											cont=1;
											
										}
									}
									if(cont==0) {
										
										if(files[0].getSize()>0) {
											
											response = filehandler.saveFile(files[0]);
											
											l.setFoto(response.getFileDownloadUri());
											
									
											
											service.save(l);
											//System.out.println("img nova "+img);
											return "redirect:/painel?fragment=painel_admin";
										}else {
	
											
											img=lo.getFoto();
											l.setFoto(img);
											service.save(l);
											//System.out.println("img nova "+img);
											return "redirect:/painel?fragment=painel_admin";
										}
										
								
										
									}else if(cont==1){
										
										
										return "main.html";
									}
								}
								
								
								
								
								
								
								
								
								
								else if (!lo.getEmail().equals(email) && l.getId().compareTo(lo.getId())==0) {
									System.out.println("COMPARAR O ID");
									if(files[0].getSize()>0) {
										
										response = filehandler.saveFile(files[0]);
										
										l.setFoto(response.getFileDownloadUri());
										
										img=response.getFileDownloadUri();
										l.setFoto(img);
										
										service.save(l);
										System.out.println("img nova "+img);
										return "redirect:/painel?fragment=painel_admin";
									}
									
									img=lo.getFoto();
									l.setFoto(img);
									service.save(l);
									return "redirect:/painel?fragment=painel_admin";
								}
							}
							
							if(lo.getUsername().compareToIgnoreCase(username)!=0 && lo.getEmail().compareTo(email)!=0) {
								
								int ola=0;
								System.out.println("lo username dif "+lo.getUsername() + " l user " + l.getUsername());
								
								for(Login novo: service.findAll()) {
									
									//System.out.println("novo for");
									//System.out.println("novo user "+novo.getUsername());
									
									
									if(novo.getUsername().compareToIgnoreCase(username)==0) {
										System.out.println("novo user bad");
										ola=1;
										//return "main.html";
									}
									else if(novo.getEmail().compareToIgnoreCase(email)==0) {
										ola=1;
										System.out.println("novo email bad");
										//return "main.html";
									}
									
								
									
								}
								
								if(ola==1) {
									System.out.println("deu merda");
									ola=0;
									return "main.html";
								}
								else {
									
									
									if(files[0].getSize()>0) {
										
										response = filehandler.saveFile(files[0]);
										
										l.setFoto(response.getFileDownloadUri());
										
										img=response.getFileDownloadUri();
										l.setFoto(img);
										
										service.save(l);
										System.out.println("img nova "+img);
										return "redirect:/painel?fragment=painel_admin";
									}else {
										
										img=lo.getFoto();
										l.setFoto(img);

										service.save(l);
										return "redirect:/painel?fragment=painel_admin";
										
									}
									
								}
							
							}	
					}
				}
		}
		
		return "main.html";
	}

	//Editar dos users
	@PostMapping("/editar_uti")
	public String editar_uti(Model m,Login ll,String fragment,String id,String tipo,String username,String email,HttpSession request) {
		
		Login l=(Login)request.getAttribute("user");
		 
		if(l==null) {return "redirect:/login";}
		
		String tip;
	/*	int conte=0;
		int contu=0;
	
		for(Login lo: service.findAll()) {
			if(lo.getId().equals(id)) {
				//System.out.println("nome desse gajo "+lo.getUsername());
				
				if(lo.getEmail().compareTo(email)==0) { //SE O EMAIL AINDA SE MANTIVER (FOR IGUAL)
					
					System.out.println("lo username "+lo.getEmail());
					System.out.println("usernmae que meti "+email);
					
					if(lo.getUsername().compareTo(username)==0) {
						System.out.println("OI");
						tip=lo.getTipo();
						lo.setTipo(tip);
						service.save(ll);
					}else if(lo.getUsername().compareTo(username)!=0) {
						for(Login lu: service.findAll()) {
							if(lu.getUsername().compareTo(username)==0) {	
								contu=1;	
							}
						}
					}

				}
				
				
				
				
				else if(lo.getUsername().compareTo(username)==0) {//o email vai ser diferente logo entra aqui
					System.out.println("o email vai ser diferente logo entra aqui");
					if(lo.getEmail().compareTo(email)==0) {//alteras te o email ? se nao alterou pode guardar
						System.out.println("OI 2");
						tip=lo.getTipo();
						lo.setTipo(tip);
						service.save(ll);
					}else if(lo.getEmail().compareTo(email)!=0) {//se alterar
						System.out.println("alterei o email");
						for(Login li: service.findAll()) {
							if(li.getEmail().compareTo(email)==0) {
								System.out.println("quantas vezes");
								conte=1;
							}
						}
					}
					
				}
				
				
				if(contu==1) {
					System.out.println("username ja existe");
					return "redirect:/painel?fragment=painel_admin";
				}else if(conte==1) {
					System.out.println("email já existe");
					return "redirect:/painel?fragment=painel_admin";
				}else{
					System.out.println("entrei ?");
					tip=lo.getTipo();
					lo.setTipo(tip);
					service.save(ll);
				}
				
			}
			
		}*/
		
		
				for(Login lu: service.findAll()) {
					if(lu.getEmail().compareTo(email)==0 && lu.getUsername().compareTo(username)==0) {
						if(lu.getId().compareTo(id)!=0) {
							System.out.println("ID "+id);
							System.out.println("outro ID "+lu.getId());
							return "redirect:/painel?fragment=painel_admin";
						}
					}else if(lu.getEmail().compareTo(email)==0) {
						if(lu.getId().compareTo(id)!=0) {
							System.out.println("ID "+id);
							System.out.println("outro ID "+lu.getId());
							return "redirect:/painel?fragment=painel_admin";
						}		
					}else if(lu.getUsername().compareTo(username)==0) {
							if(lu.getId().compareTo(id)!=0) {
								System.out.println("ID "+id);
								System.out.println("outro ID "+lu.getId());
								return "redirect:/painel?fragment=painel_admin";
							}						
					}else if(lu.getId().compareTo(id)==0) {
							tip=lu.getTipo();
							lu.setTipo(tip);
					}
						
				}
				
				/*else {
					tip=lu.getTipo();
					lu.setTipo(tip);
					service.save(ll);
				}	*/
			
				service.save(ll);
		

	
		m.addAttribute("fragment",fragment);
		
		
		
		return "main.html";
	}
	
	@GetMapping(value="/addemp")
	public String addemp(Model m, String fragment, HttpSession session) {
		
		ArrayList<Restaurante> res = new ArrayList<>();
		
		Login u = (Login)session.getAttribute("user");
		
		System.out.println("qual e o u porra "+ u.getNome());
		
		if(u==null || u.getTipo().compareTo("1")!=0) {return "redirect:/login";}
		
		for(Restaurante re: svres.findAll()) {
			
			if(re.getId_dono().compareTo(u.getId())==0) {
				
				res.add(re);
				
			}
			
		}
		
		m.addAttribute("listrestaurantes",res);
		
		m.addAttribute("fragment",fragment);
		
		return "mainownerprofile.html";
	}
	
	@PostMapping(value="/addemployee")
	public String adde(Login em, String username2, HttpSession session) {
		
		Login u = (Login)session.getAttribute("user");
		
		System.out.println("entrei lol");
		
		em.setTipo("2");
		
		em.setFoto("a");
		
		//em.setArrestaurante(null);
		service.save(em); //new Login(em.getId(),em.getUsername(),em.getPassword(),em.getNome(),em.getContacto(),em.getEmail(),username2,"2",null)
				
		System.out.println("lolz");
		
		//return "profileowner.html";
		return "redirect:/profileowner?fragment=profileowner";
	}
	
	@GetMapping(value="/listaremp")
	public String listemp(Model m, String fragment, HttpSession session) {
		ArrayList<Login> emp = new ArrayList<Login>();
		
		Login u = (Login)session.getAttribute("user"); 
		
		System.out.println("hecked up");
		
		
		for(Restaurante re: svres.findAll()) {
			
			for(Login e: service.findAll()) {
				
				if (re.getId_dono().compareTo(u.getId())==0) {
					
					if(re.getId().compareTo(e.getId_restaurante())==0 && e.getTipo().compareTo("2")==0) {
						
						emp.add(e);
					}
					
				}
				
			}
			
		}
		
		//Collections.sort(emp, c);
		
		m.addAttribute("fragment",fragment);
		m.addAttribute("pessoa",emp);	
		return "mainownerprofile.html";
	}
	
	@PostMapping("/editar_empregados")
    public String editaremp(Model m,Login l, String id, String nome, String fragment, HttpSession session) {
		
		Login u = (Login)session.getAttribute("user");
		
		ArrayList<Restaurante> arres = new ArrayList<>();

        for(Login ll: service.findAll()) {
            if(ll.getId().equals(id)) {
            	System.out.println("Hecker voice I'm in");
                m.addAttribute("utilizador",ll); //receber no th os dados deste objeto que tiver o mesmo id
                
            }
        }
        
        for(Restaurante re: svres.findAll()) {
        	
        	if(re.getId_dono().compareTo(u.getId())==0) {
        		
        		arres.add(re);
        	}
        }
        
        m.addAttribute("listrestaurantes",arres);
        m.addAttribute("fragment",fragment);
        return "mainownerprofile.html";
    }
	
	@PostMapping(value="/editutil")
    public String editu(Login l, String idrestaurante, String id_restaurantes) {
    	
		System.out.println("wth is going with this id "+l.getId_restaurante());
		System.out.println("id vindo i think "+id_restaurantes);
		l.setId_restaurante(id_restaurantes);
		l.setFoto(null);
    	service.save(l);
    	return "redirect:/listaremp?fragment=listar_emp";
    }
	
	@GetMapping(value="/delemp")
    public String delpeople(String id, String idrestaurante) {
    	
    	for(Login h: service.findAll()) {
    		
    		if(h.getId().compareTo(id)==0) {
    			
    			service.delete(h);
    			
    		}
    		
    	}
    	
    	//return "redirect:/listaremp?idrestaurante="+idrestaurante;
    	return "redirect:/listaremp?fragment=listar_emp&idrestaurante="+idrestaurante;
    }
	

}
