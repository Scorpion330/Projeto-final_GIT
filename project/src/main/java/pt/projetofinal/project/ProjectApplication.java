package pt.projetofinal.project;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

import pt.projetofinal.project.controller.Restaurantecontroller;

@SpringBootApplication
@ComponentScan({"pt.projetofinal.project","pt.projetofinal.project.controller"})
public class ProjectApplication {

	public static void main(String[] args) {
		new File(Restaurantecontroller.uploadDirectory).mkdir();
		SpringApplication.run(ProjectApplication.class, args);
		System.out.println("Servidor on !");
	}
}
