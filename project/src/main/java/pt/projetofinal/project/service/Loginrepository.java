package pt.projetofinal.project.service;

import org.springframework.data.mongodb.repository.MongoRepository;

import pt.projetofinal.project.model.Login;

public interface Loginrepository extends MongoRepository<Login,String>{

}
