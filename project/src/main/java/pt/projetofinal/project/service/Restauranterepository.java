package pt.projetofinal.project.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import pt.projetofinal.project.model.Restaurante;

public interface Restauranterepository extends MongoRepository<Restaurante, String> {
	
	   // List<Restaurante> findbyCategoria(String categoria);
//		List<coisas> fi
}
	
	
		