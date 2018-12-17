package pt.projetofinal.project.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Pedido")
public class Pedido {
	
	@Id
	public String id;
	
	public String id_user,
					id_restaurante,
					data;
	
	ArrayList<Menu> armenu = new ArrayList<>();
	public int lugares=0;
	public double preco;
	public String mensagem, tipo, morada, estado, reserva;
	
	
	//estado -> enviado, o empregado tem que receber o pedido e aceitar, e após estar pronto enviar outro
	// tipo -> reserva, levar a casa ou comer lá
	
	public Pedido() {
		super();
		// TODO Auto-generated constructor stub
	}


	public Pedido(String id, String id_user, String id_restaurante, String data, ArrayList<Menu> armenu, int lugares,
			double preco, String mensagem, String tipo, String morada, String estado, String reserva) {
		super();
		this.id = id;
		this.id_user = id_user;
		this.id_restaurante = id_restaurante;
		this.data = data;
		this.armenu = armenu;
		this.lugares = lugares;
		this.preco = preco;
		this.mensagem = mensagem;
		this.tipo = tipo;
		this.morada = morada;
		this.estado = estado;
		this.reserva = reserva;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getId_user() {
		return id_user;
	}


	public void setId_user(String id_user) {
		this.id_user = id_user;
	}


	public String getId_restaurante() {
		return id_restaurante;
	}


	public void setId_restaurante(String id_restaurante) {
		this.id_restaurante = id_restaurante;
	}


	public String getData() {
		return data;
	}


	public void setData(String data) {
		this.data = data;
	}


	public ArrayList<Menu> getArmenu() {
		return armenu;
	}


	public void setArmenu(ArrayList<Menu> armenu) {
		this.armenu = armenu;
	}


	public int getLugares() {
		return lugares;
	}


	public void setLugares(int lugares) {
		this.lugares = lugares;
	}


	public double getPreco() {
		return preco;
	}


	public void setPreco(double preco) {
		this.preco = preco;
	}


	public String getMensagem() {
		return mensagem;
	}


	public void setMensagem(String mensagem) {
		this.mensagem = mensagem;
	}


	public String getTipo() {
		return tipo;
	}


	public void setTipo(String tipo) {
		this.tipo = tipo;
	}


	public String getMorada() {
		return morada;
	}


	public void setMorada(String morada) {
		this.morada = morada;
	}


	public String getEstado() {
		return estado;
	}


	public void setEstado(String estado) {
		this.estado = estado;
	}


	public String getReserva() {
		return reserva;
	}


	public void setReserva(String reserva) {
		this.reserva = reserva;
	}
	
	
}
