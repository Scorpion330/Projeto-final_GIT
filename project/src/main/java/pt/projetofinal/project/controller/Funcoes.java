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
	email.setSubject("Confirmação da alteração da passsword");
	email.setMsg("mensagens");
	email.addTo(usermail);
	email.send();
					
}catch(EmailException e) {
	e.printStackTrace();
}
	System.out.println("email enviado para: "+ usermail +"");

	return "oioi";
			
	}

}
