package pt.projetofinal.project.service;

import org.springframework.data.mongodb.repository.MongoRepository;

import pt.projetofinal.project.model.Menu;

public interface Menurepository extends MongoRepository<Menu, String>{

}
