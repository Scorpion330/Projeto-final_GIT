package pt.projetofinal.project.service;

import org.springframework.data.mongodb.repository.MongoRepository;

import pt.projetofinal.project.model.Contacto;

public interface Contactorepository extends MongoRepository<Contacto, String> {

}
