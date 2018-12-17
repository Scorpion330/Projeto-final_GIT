package pt.projetofinal.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Contactos")
public class Contacto {
	
	@Id
	String id;
	
	String nome,email,
	mensagem,data,tipo,id_dono;

	public Contacto() {
		super();
	}

	public Contacto(String id, String nome, String email, String mensagem, String data, String tipo, String id_dono) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.mensagem = mensagem;
		this.data = data;
		this.tipo = tipo;
		this.id_dono = id_dono;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getMensagem() {
		return mensagem;
	}

	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getId_dono() {
		return id_dono;
	}

	public void setId_dono(String id_dono) {
		this.id_dono = id_dono;
	}
	
}
