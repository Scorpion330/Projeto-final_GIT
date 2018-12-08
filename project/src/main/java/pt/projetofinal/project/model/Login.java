package pt.projetofinal.project.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
@Document(collection="login")
public class Login {
	
	@Id
	String id;
	
	String username,
	password,nome,contacto, email,
	id_restaurante,tipo,foto;

	ArrayList<String> arrestaurante = new ArrayList<>(); //guardar os restaurentes que uma pessoa já deu rating(para nao voltar a votar no mesmo)

	public Login() {
		super();
	}

	public Login(String id, String username, String password, String nome, String contacto, String email,
			String id_restaurante, String tipo, String foto, ArrayList<String> arrestaurante) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.nome = nome;
		this.contacto = contacto;
		this.email = email;
		this.id_restaurante = id_restaurante;
		this.tipo = tipo;
		this.foto = foto;
		this.arrestaurante = arrestaurante;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getContacto() {
		return contacto;
	}

	public void setContacto(String contacto) {
		this.contacto = contacto;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getId_restaurante() {
		return id_restaurante;
	}

	public void setId_restaurante(String id_restaurante) {
		this.id_restaurante = id_restaurante;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}

	public ArrayList<String> getArrestaurante() {
		return arrestaurante;
	}

	public void setArrestaurante(ArrayList<String> arrestaurante) {
		this.arrestaurante = arrestaurante;
	}
	
}
