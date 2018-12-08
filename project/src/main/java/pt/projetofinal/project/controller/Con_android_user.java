package pt.projetofinal.project.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import pt.projetofinal.project.model.Login;
import pt.projetofinal.project.model.Restaurante;
import pt.projetofinal.project.service.Loginrepository;
import pt.projetofinal.project.service.Restauranterepository;

@RestController
public class Con_android_user {

	@Autowired
	Loginrepository inter_pessoa;
	
	@Autowired
	Restauranterepository inter_restaurante;
	
	
	@RequestMapping(value="/android/registo", method=RequestMethod.POST)
	 public String registo(@RequestBody Login log) {
		String username=log.getUsername();
		String password=log.getPassword();
		String email=log.getEmail();
		log.setTipo("3");
		log.setArrestaurante(null);
		log.setId_restaurante("32");
		
	
		int estado=0;
		
		for(Login l: inter_pessoa.findAll()) {	
			if(l.getUsername().compareToIgnoreCase(username)==0) {
				estado++;	
				return l.getUsername().toString();
			}
			if( l.getEmail().compareTo(email)==0) {
				estado++;	
				return l.getEmail().toString();
			}
		}
		if(estado==0) {
			inter_pessoa.save(log);		
		}
		
		return "Inserido com sucesso ";
	}
	
	@RequestMapping(value="/android/review_user", method=RequestMethod.POST)
    public String revie_user(@RequestParam("id") String id,@RequestParam("rating") String rating, @RequestParam("id_restaurante") String id_restaurante) {
       
       Login pessoatmp = new Login();
       Restaurante restaurantetmp = new Restaurante();
       int control = 0;
       
       for(Login p:inter_pessoa.findAll()) {
           if(p.getId().compareTo(id)==0) {
               pessoatmp = p;
           }
       }

       for(Restaurante r:inter_restaurante.findAll()) {
               
           if(r.getId().compareTo(id_restaurante)==0) {
               restaurantetmp = r;
           }
           
       }
       
       if(pessoatmp.getArrestaurante() == null) {
           control = 0;
       }
       else {
           
           for(String s:pessoatmp.getArrestaurante()) {
               if(s.compareTo(id_restaurante)==0) {
                   control = 1;
                   return "Ja EXISTEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE";
               }
               
           }
           
       }
       
       if(control == 0) {
           
           switch(Integer.parseInt(rating)) {
           
           case 1:
               restaurantetmp.setRating1(restaurantetmp.getRating1()+1);
           break;
               
           case 2:
               restaurantetmp.setRating2(restaurantetmp.getRating2()+1);
           break;
           
           case 3:
               restaurantetmp.setRating3(restaurantetmp.getRating3()+1);
           break;
           
           case 4:
               restaurantetmp.setRating4(restaurantetmp.getRating4()+1);
           break;
           
           case 5:
               restaurantetmp.setRating5(restaurantetmp.getRating5()+1);
           break;
           
           default:
               
           return "Não pode ser maior que o orgao genital do vicente (5)";
           
           
           }

   pessoatmp.getArrestaurante().add(id_restaurante);
           
           double tmp1 = ((restaurantetmp.getRating1()*1)
                   +(restaurantetmp.getRating2()*2)
                   +(restaurantetmp.getRating3()*3)
                   +(restaurantetmp.getRating4()*4)
                   +(restaurantetmp.getRating5()*5));
           
           double tmp2 = (restaurantetmp.getRating1()
                   +restaurantetmp.getRating2()
                   +restaurantetmp.getRating3()
                   +restaurantetmp.getRating4()
                   +restaurantetmp.getRating5());
           
           System.out.println(tmp1 + " / " + tmp2 + " = " + tmp1/tmp2);
           
           double ratingtmp = tmp1 / tmp2;
           
           //System.out.print(arredondar(ratingtmp,2));
           
           //arredondar(ratingtmp,2);
           
           
           
           restaurantetmp.setRating(Double.toString(arredondar(ratingtmp,2)));
           
           inter_restaurante.save(restaurantetmp);
           inter_pessoa.save(pessoatmp);
           
           
           return "Sucesso";
           
       }
       
       return "Erro";
       
   }

public double arredondar(double valor, int espacos) {
       if (espacos < 0) throw new IllegalArgumentException();
       
       long fator = (long) Math.pow(10, espacos);
       valor = valor * fator;
       long tmp = Math.round(valor);
       return (double) tmp / fator;
   }
   
	
	
}
