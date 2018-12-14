package pt.projetofinal.project.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="menu")
public class Menu {
	
	@Id
	String id;
	
	String nome,
			ingridiente,
			categoria,
			picture,
			id_restaurante;
	
	int quantidade;
	
	double custo;

	public Menu() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Menu(String id, String nome, String ingridiente, String categoria, String picture, String id_restaurante,
			int quantidade, double custo) {
		super();
		this.id = id;
		this.nome = nome;
		this.ingridiente = ingridiente;
		this.categoria = categoria;
		this.picture = picture;
		this.id_restaurante = id_restaurante;
		this.quantidade = quantidade;
		this.custo = custo;
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

	public String getIngridiente() {
		return ingridiente;
	}

	public void setIngridiente(String ingridiente) {
		this.ingridiente = ingridiente;
	}

	public String getCategoria() {
		return categoria;
	}

	public void setCategoria(String categoria) {
		this.categoria = categoria;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public String getId_restaurante() {
		return id_restaurante;
	}

	public void setId_restaurante(String id_restaurante) {
		this.id_restaurante = id_restaurante;
	}

	public int getQuantidade() {
		return quantidade;
	}

	public void setQuantidade(int quantidade) {
		this.quantidade = quantidade;
	}

	public double getCusto() {
		return custo;
	}

	public void setCusto(double custo) {
		this.custo = custo;
	}

}
