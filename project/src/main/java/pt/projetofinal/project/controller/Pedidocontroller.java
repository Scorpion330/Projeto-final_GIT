package pt.projetofinal.project.controller;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import pt.projetofinal.project.model.Login;
import pt.projetofinal.project.model.Menu;
import pt.projetofinal.project.model.Pedido;
import pt.projetofinal.project.model.Restaurante;
import pt.projetofinal.project.service.Loginrepository;
import pt.projetofinal.project.service.Menurepository;
import pt.projetofinal.project.service.Pedidorepository;
import pt.projetofinal.project.service.Restauranterepository;

@Controller
public class Pedidocontroller {
	
	@Autowired
	Pedidorepository svpedido;
	
	@Autowired
	Loginrepository svusuário;
	
	@Autowired
	Restauranterepository svres;
	
	@Autowired
	Menurepository svmenu;
	
	/*@GetMapping(value="/listpedidos")
	public String listorder(Model m) {
		ArrayList<Pedido> arorder = new ArrayList<>();
		ArrayList<Login> arlog = new ArrayList<>();
		
		for(Pedido p: svpedido.findAll()) {
			
			if(p.getEstado().compareTo("Enviado")==0) {
				
				for(Login q: svusuário.findAll()) {
					
					if(q.getId().compareTo(p.getId_cliente())==0) {
						System.out.println("Hacker voice I'm in");
						arorder.add(p);
						arlog.add(q);
						
					}
					
				}
				
			}
			
			
		}
		
		m.addAttribute("userorder",arlog);
		m.addAttribute("pedidos",arorder);
		return "listpedidopf.html";
	}*/
	
	/*@GetMapping(value="/listped")
	public String listpedidoo(Model m, String id) {
		ArrayList<Login> arlogin = new ArrayList<>();
		
		for(Login l: svusuário.findAll()) {
			
			if(l.getId().compareTo(id)==0) {
				
				arlogin.add(l);
				
				
			}
			
		}
		m.addAttribute("teste",arlogin);
		return "listpedidopf.html";
	}*/
	
	@GetMapping(value="/listpedidos")
    public String listorder(Model m, String ordertype, String fragment, HttpSession session) {
        ArrayList<Pedido> arorder = new ArrayList<>();
        ArrayList<Login> arlog = new ArrayList<>();
        
        String date="";
        
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        date = dateFormat.format(cal.getTime());
        
        System.out.println("Current date: "+date);
        
        Login u = (Login)session.getAttribute("user");
        
        //System.out.println("que u é este "+u.getTipo());
        
        if(u==null || u.getTipo().compareTo("2")!=0) {return "redirect:/login";}
        
        //System.out.println("type: "+ordertype);
        for(Pedido p: svpedido.findAll()) {
            
        	System.out.println("id restaurante pedido "+p.getId_restaurante()+" u id restaurante "+u.getId_restaurante());
        	
        	
            if(p.getId_restaurante().compareTo(u.getId_restaurante())==0) {
            	String orderdate = (p.getData().substring(0,10));
                System.out.println("Order date "+orderdate);
            	
                for(Login q: svusuário.findAll()) {
                	
                	if(p.getEstado().compareTo("Enviado")==0) {
                		System.out.println("Estado passado");
                		if(ordertype.compareTo("1")==0) {
                            System.out.println("Ultrapasso o ordertype");
                            if(q.getId().compareTo(p.getId_user())==0 && p.getTipo().compareTo("reserva")!=0) {
                                System.out.println("Hacker voice I'm in");
                                
                                arorder.add(p);
                                arlog.add(q);
                                
                            }
                            
                        }
                        
                        else if(ordertype.compareTo("2")==0) {
                            
                            if(q.getId().compareTo(p.getId_user())==0 && p.getTipo().compareTo("reserva")==0) {
                                System.out.println("Hacker voice I'm in");
                                
                                arorder.add(p);
                                arlog.add(q);
                                
                            }
                            
                        }
                		
                	}
                
                	else if (ordertype.compareTo("3")==0) {

                		if(q.getId().compareTo(p.getId_user())==0 && p.getEstado().compareTo("Aceite")==0 && p.getTipo().compareTo("reserva")==0 && date.compareTo(orderdate)==0) {
                    		
                			arorder.add(p);
                            arlog.add(q);
                    		
                    	}
                		
					}
                    
                }
                
            }
            
            
        }
        
        m.addAttribute("ordertype",ordertype);
        m.addAttribute("fragment",fragment);
        m.addAttribute("userorder",arlog);
        m.addAttribute("pedidos",arorder);
        return "mainempprofile.html";
    }
	
	@PostMapping(value="/orderacept")
	public String orderacept(Pedido p, Model m, String id, String ordertype, String fragment, HttpSession session) {
		System.out.println("String id: "+id);
		ArrayList<Pedido> arorder = new ArrayList<>();
		ArrayList<Menu> armenu = new ArrayList<>();
		
		String date="";
		
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        date = dateFormat.format(cal.getTime());
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("2")!=0) {return "redirect:/login";}
		
		System.out.println("idddd porra "+id);
		System.out.println("ordertype "+ordertype);
		
		for(Pedido r: svpedido.findAll()) {
			String orderdate = (r.getData().substring(0,10));
			
			
			if (u.getId_restaurante().compareTo(r.getId_restaurante())==0) {
				
				if (r.getId_user().compareTo(id)==0) {
					
					if(ordertype.equals("1")) {
						
						if (r.getEstado().compareTo("Enviado")==0 && r.getTipo().compareTo("reserva")!=0) {
							arorder.add(r);
							
							for(int i=0; i<r.getArmenu().size();i++) {
								System.out.println("armenu "+r.getArmenu().get(i).getNome());
								armenu.add(r.getArmenu().get(i));
								
							}
						}
						
						
					}
					
					if(ordertype.equals("2")) {
						
						if(r.getTipo().compareTo("reserva")==0 && r.getEstado().compareTo("Enviado")==0) {
							System.out.println("broo reserva");
							arorder.add(r);
							
							for(int i=0; i<r.getArmenu().size();i++) {
								System.out.println("armenu "+r.getArmenu().get(i).getNome());
								armenu.add(r.getArmenu().get(i));
								
							}
							
						}
						
					}
					
					if(ordertype.equals("3")) {
						
						if (r.getTipo().compareTo("reserva")==0 && r.getEstado().compareTo("Aceite")==0 && orderdate.compareTo(date)==0) {
							
							arorder.add(r);
							System.out.println("estou aqui na reserva lol");
							
							for(int i=0; i<r.getArmenu().size();i++) {
								System.out.println("armenu "+r.getArmenu().get(i).getNome());
								armenu.add(r.getArmenu().get(i));
								
							}
							
						}
						
						
					}
					
				}
				
			}
			
		}
		m.addAttribute("fragment",fragment);
		m.addAttribute("pedidos", arorder);
		m.addAttribute("menupedido",armenu);
		return "mainempprofile.html";
	}
	
	@PostMapping(value="/ordermessage")
    public String ordermessage(Model m, String id,String buttonorder, String descricao, String fragment) {
        System.out.println("id: "+id);
        String ordertype="";
        
        for(Pedido r: svpedido.findAll()) {
            
            if(r.getId().compareTo(id)==0) {
                
                if(buttonorder.compareTo("0")==0) {
                    
                	
                    r.setMensagem("O seu pedido foi aceite. "+descricao);
                    r.setEstado("Aceite");
                    svpedido.save(r);
                    m.addAttribute("fragment",fragment);
                    System.out.println("Estou aqui 1");
                    
                    if(r.getTipo().compareTo("reserva")!=0) {
                    	
                    	ordertype="1";
                    	
                    }
                    
                    else if(r.getTipo().compareTo("reserva")==0 && r.getEstado().compareTo("Aceite")==0) {
                    	ordertype="3";
                    }
                    
                    else {
                    	ordertype="2";
                    }
                    
                }
                
                else if (buttonorder.compareTo("1")==0) {
                    
                    r.setMensagem("O seu pedido não foi aceite. "+descricao);
                    r.setEstado("Recusado");
                    svpedido.save(r);
                    m.addAttribute("fragment",fragment);
                    System.out.println("Estou aqui 2");
                }
                
            }
            
        }
        
        return "redirect:/listpedidos?fragment=listpedidopf&ordertype="+ordertype;
    }
	
	@PostMapping(value="/pesqe")
	public String pesqe(Model m, String search, HttpSession session) {
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("2")!=0) {return "redirect:/login";}
		
		String abcz="z";
		
		for(Restaurante re: svres.findAll()) {
			
			if(re.getId().compareTo(u.getId_restaurante())==0) {
				
				for(Menu me: svmenu.findAll()) {
				
					for(Pedido p: svpedido.findAll()) {
						
						if(me.getId_restaurante().compareTo(re.getId())==0 && p.getId_restaurante().compareTo(re.getId())==0) {
							
							for(Login lo: svusuário.findAll()) {
								
								if(lo.getTipo().equals("3") && lo.getId().compareTo(p.getId_user())==0) {
									
									if ((me.getNome().contains(search) || me.getIngrediente().contains(search)) && lo.getNome().startsWith(search)) {
										
										return "redirect:/proc_menupedido?fragment=proc_menupedido&search="+search; 
									}
							
									else if(me.getNome().contains(search) || me.getIngrediente().contains(search)) {
										
										abcz="a";
									}
									
									else if (lo.getNome().startsWith(search)) {
										
										abcz="b";
									}	
								}	
							}
						}
					}
				}
				
			}
			
			
		}

		if(abcz.compareTo("a")==0) {
			
			return "redirect:/proc_menuse?fragment=listarmenu_empregado&search="+search; 
		}
		
		else if(abcz.compareTo("b")==0) {
			return "redirect:/proc_pedido?fragment=listpedidopf&search="+search; 
		}
		
		return "mainempprofile.html";
	}
	
	
	@GetMapping(value="/proc_menupedido")
	public String procmenu(Model m, String search, String fragment, HttpSession session) {
		
		Login u = (Login)session.getAttribute("user");
		
		ArrayList<Login> arlogin = new ArrayList<>();
		
		ArrayList<Menu> armenu = new ArrayList<>();
		
		if(u==null || u.getTipo().compareTo("2")!=0) {return "redirect:/login";}
		
		for(Restaurante re: svres.findAll()) {
			
			if(re.getId().compareTo(u.getId_restaurante())==0) {
				
				for(Menu me: svmenu.findAll()) {
						
						if(me.getId_restaurante().compareTo(re.getId())==0) {
							
									if (me.getNome().contains(search) ) {
										
										
										armenu.add(me);
										//enviar para aquele com ambos
									}
									
								}
								
							}		
			}
			
		}
		
		
		for(Restaurante re: svres.findAll()) {
			
			if(re.getId().compareTo(u.getId_restaurante())==0) {
				
				for(Pedido p: svpedido.findAll()) {
					
					if (p.getId_restaurante().compareTo(re.getId())==0) {
						
						for(Login lo: svusuário.findAll()){
							
							if(lo.getTipo().equals("3") && lo.getId().compareTo(p.getId_user())==0) {
							
								if(lo.getNome().startsWith(search)) {
								
									arlogin.add(lo);
								}
							}	
							
						}	
						
					}
					
				}
				
			}
			
		}
		
		
		m.addAttribute("menu",armenu);
		m.addAttribute("userorder", arlogin);
		m.addAttribute("fragment",fragment);
		return "mainempprofile.html";
	}
	
	@GetMapping(value="/proc_pedido")
	public String procpedido(Model m, String fragment, String search, HttpSession session) {
		
		ArrayList<Login> arlo = new ArrayList<>();
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("2")!=0) {return "redirect:/login";}
		
		for(Pedido p: svpedido.findAll()) {
			
			if(p.getId_restaurante().compareTo(u.getId_restaurante())==0) {
				
				for(Login lo: svusuário.findAll()) {
					
					if(lo.getId().compareTo(p.getId_user())==0 && lo.getNome().startsWith(search) && lo.getNome().endsWith(search)) {

						arlo.add(lo);
					}
					
				}
				
			}
			
			
		}
		
		m.addAttribute("userorder", arlo);
		m.addAttribute("fragment",fragment);
		return "mainempprofile.html";
	}
	
	@GetMapping(value="/proc_menuse")
	public String procmenuse(Model m, String fragment, String search, HttpSession session) {
		
		System.out.println("proc_menuse");
		
		ArrayList<Menu> armenu = new ArrayList<>();
		
		Login u = (Login)session.getAttribute("user");
		
		if(u==null || u.getTipo().compareTo("2")!=0) {return "redirect:/login";}
		
		for(Menu me: svmenu.findAll()) {
			
			if(me.getId_restaurante().compareTo(u.getId_restaurante())==0) {	
				
				if(me.getNome().contains(search) || me.getNome().equalsIgnoreCase(search) || me.getIngrediente().contains(search)) {
					
					
					armenu.add(me);
					
				}	
			}
		}
		m.addAttribute("menu",armenu);
		m.addAttribute("fragment",fragment);
		return "mainempprofile.html";
	}
	

}

