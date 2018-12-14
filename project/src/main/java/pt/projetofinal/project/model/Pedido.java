package pt.projetofinal.project.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection="Pedido")
public class Pedido {
	
	@Id
	public String id;
	
	public String id_cliente,
					id_restaurante_pedido,
					data;
	
	ArrayList<Menu> armenu = new ArrayList<>();
	public int nr_pessoas=0;
	public double custo_total;
	public String mensagem, tipo, morada, estado;
	
	//estado -> enviado, o empregado tem que receber o pedido e aceitar, e após estar pronto enviar outro
	// tipo -> reserva, levar a casa ou comer lá
	
	public Pedido() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Pedido(String id, String id_cliente, String id_restaurante_pedido, String data, ArrayList<Menu> armenu,
			int nr_pessoas, double custo_total, String mensagem, String tipo, String morada, String estado) {
		super();
		this.id = id;
		this.id_cliente = id_cliente;
		this.id_restaurante_pedido = id_restaurante_pedido;
		this.data = data;
		this.armenu = armenu;
		this.nr_pessoas = nr_pessoas;
		this.custo_total = custo_total;
		this.mensagem = mensagem;
		this.tipo = tipo;
		this.morada = morada;
		this.estado = estado;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId_cliente() {
		return id_cliente;
	}

	public void setId_cliente(String id_cliente) {
		this.id_cliente = id_cliente;
	}

	public String getId_restaurante_pedido() {
		return id_restaurante_pedido;
	}

	public void setId_restaurante_pedido(String id_restaurante_pedido) {
		this.id_restaurante_pedido = id_restaurante_pedido;
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

	public int getNr_pessoas() {
		return nr_pessoas;
	}

	public void setNr_pessoas(int nr_pessoas) {
		this.nr_pessoas = nr_pessoas;
	}

	public double getCusto_total() {
		return custo_total;
	}

	public void setCusto_total(double custo_total) {
		this.custo_total = custo_total;
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
	
}
