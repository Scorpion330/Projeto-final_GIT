package pt.projetofinal.project.controller;


import org.apache.commons.mail.*;

public class Funcoes {
	
public static String sendEmailReset(String usermail) {
		
	
	Email email = new SimpleEmail();
	email.setHostName("smtp.gmail.com");
	email.setSmtpPort(465);
	email.setAuthentication("findelechef@gmail.com", "tpsi1117b");
	email.setSSL(true);
		
try {
						
	email.setFrom("findelechef@gmail.com");
	email.setSubject("Bem-Vindo ao Find Le Chef !");
	email.setMsg("Obrigado por ter aderido à nossa aplicação, esperemos que goste e continue a utilizar a nossa aplicação no futuro."
			+ "Melhores Cumprimentos,"
			+ "Equipa Find Le Chef.");
	email.addTo(usermail);
	email.send();
					
}catch(EmailException e) {
	e.printStackTrace();
}
	System.out.println("email enviado para: "+ usermail +"");

	return "oioi";
			
	}

}
