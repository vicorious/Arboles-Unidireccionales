package com.ejercicio.segundocorte.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @author Alejandro
 *
 */
public class Nodo 
{
	private String nombre;
	
	private List<Nodo> nodo_der;
	
	private int distancia;

	/**
	 * 
	 * @param nombre
	 * @param nodo_der
	 * @param distancia
	 */
	public Nodo(String nombre, List<Nodo> nodo_der, int distancia) 
	{
		super();
		this.nombre = nombre;
		this.nodo_der = nodo_der;
		this.distancia = distancia;
		if(this.nodo_der == null)
		{
			this.nodo_der =  new ArrayList<Nodo>();
		}
		
	}//Constructor

	public String getNombre() 
	{
		return nombre;
	}

	public void setNombre(String nombre) 
	{
		this.nombre = nombre;
	}

	public List<Nodo> getNodo_der() 
	{
		return nodo_der;
	}

	public void setNodo_der(List<Nodo> nodo_der) 
	{
		this.nodo_der = nodo_der;
	}

	public int getDistancia() 
	{
		return distancia;
	}

	public void setDistancia(int distancia) 
	{
		this.distancia = distancia;
	}
	
			
}//NoBorrar
