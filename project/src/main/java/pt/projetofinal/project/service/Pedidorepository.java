package pt.projetofinal.project.service;

import org.springframework.data.mongodb.repository.MongoRepository;

import pt.projetofinal.project.model.Pedido;

public interface Pedidorepository extends MongoRepository<Pedido, String>{
	
}
