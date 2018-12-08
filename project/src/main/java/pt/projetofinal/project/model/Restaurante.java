package pt.projetofinal.project.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Restaurante")
public class Restaurante {
	
	@Id
	String id;
	
	String nome,longitude,
	latitude,descricao,
	categoria,picture,
	rating,nome_chef,
	horario_abrir,
	horario_fechar;
	int rating1 = 0;
	int rating2 = 0;
	int rating3 = 0;
	int rating4 = 0;
	int rating5 = 0;
	ArrayList<String> arServico =new ArrayList<>();
	ArrayList<String> arDias_Semana = new ArrayList<>(); 
	
	public Restaurante() {
		super();
	}
	

	public Restaurante(String id, String nome, String longitude, String latitude, String descricao, String categoria,
			String picture, String rating, String nome_chef, String horario_abrir, String horario_fechar, int rating1,
			int rating2, int rating3, int rating4, int rating5, ArrayList<String> arServico,
			ArrayList<String> arDias_Semana) {
		super();
		this.id = id;
		this.nome = nome;
		this.longitude = longitude;
		this.latitude = latitude;
		this.descricao = descricao;
		this.categoria = categoria;
		this.picture = picture;
		this.rating = rating;
		this.nome_chef = nome_chef;
		this.horario_abrir = horario_abrir;
		this.horario_fechar = horario_fechar;
		this.rating1 = rating1;
		this.rating2 = rating2;
		this.rating3 = rating3;
		this.rating4 = rating4;
		this.rating5 = rating5;
		this.arServico = arServico;
		this.arDias_Semana = arDias_Semana;
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

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
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

	public String getRating() {
		return rating;
	}

	public void setRating(String rating) {
		this.rating = rating;
	}

	public String getNome_chef() {
		return nome_chef;
	}

	public void setNome_chef(String nome_chef) {
		this.nome_chef = nome_chef;
	}

	public String getHorario_abrir() {
		return horario_abrir;
	}

	public void setHorario_abrir(String horario_abrir) {
		this.horario_abrir = horario_abrir;
	}

	public String getHorario_fechar() {
		return horario_fechar;
	}

	public void setHorario_fechar(String horario_fechar) {
		this.horario_fechar = horario_fechar;
	}

	public int getRating1() {
		return rating1;
	}

	public void setRating1(int rating1) {
		this.rating1 = rating1;
	}

	public int getRating2() {
		return rating2;
	}

	public void setRating2(int rating2) {
		this.rating2 = rating2;
	}

	public int getRating3() {
		return rating3;
	}

	public void setRating3(int rating3) {
		this.rating3 = rating3;
	}

	public int getRating4() {
		return rating4;
	}

	public void setRating4(int rating4) {
		this.rating4 = rating4;
	}

	public int getRating5() {
		return rating5;
	}

	public void setRating5(int rating5) {
		this.rating5 = rating5;
	}

	public ArrayList<String> getArServico() {
		return arServico;
	}

	public void setArServico(ArrayList<String> arServico) {
		this.arServico = arServico;
	}

	public ArrayList<String> getArDias_Semana() {
		return arDias_Semana;
	}

	public void setArDias_Semana(ArrayList<String> arDias_Semana) {
		this.arDias_Semana = arDias_Semana;
	}

	
	
	

}
