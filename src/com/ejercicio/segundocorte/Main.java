package com.ejercicio.segundocorte;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ejercicio.segundocorte.dto.Nodo;

/**
 * 
 * @author User
 *
 */
public class Main 
{
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args)
	{
		if(args == null)
		{
			throw new IllegalArgumentException("Los argumentos no pueden ser nulos. Intente de nuevo");
		}
		
		if(args.length < 4)
		{
			throw new IllegalArgumentException("Deben existir minimo 4 Argumentos (Numero nodos, Numero conexiones, Conexiones entre nodos (minimo 1) y lugar donde padre y madre abandonaron los niños acompañado de lugar donde esta ubicada la casa");
		}
		
		int numero_nodos, numero_conexiones = 0;
		
		try
		{
			numero_nodos 	  = Integer.valueOf(args[0]);			
			numero_conexiones = Integer.valueOf(args[1]);			
			
		}catch(NumberFormatException ex)
		{
			throw new IllegalArgumentException("Error al obtener el numero de nodos y numeros de conexiones. DETALLE: "+ex.getMessage());
		}
		
		List<Nodo> red = construirRed(args, numero_nodos, numero_conexiones);
		
		imprimirRed(red);
		
		String fin, origen = "";
		Map<String, String> padres = new HashMap<String, String>();
		String[] ultima = args[args.length - 1].split("-");
						
		origen = ultima[0];
		fin    = ultima[1];
		
		int i = caminoMasCorto(red, origen, fin, padres, 0);
		
		System.out.println("Ruta mas corta: ");
		System.out.println(i);
				
	}//main
	
	/**
	 * 
	 * @param args
	 * @return
	 */
	private static List<Nodo> construirRed(String[] _args, int _numero_nodos, int _numero_conexiones)
	{
		List<Nodo> red = new ArrayList<Nodo>();
		
		int j = 2;//Posicion donde vienen los Nodos
				
		for(int i = 0; i < _numero_conexiones; i++)
		{			
			Pattern patron = Pattern.compile("(.+)\\-(.+)\\-(.+)", Pattern.MULTILINE);		
			Matcher match = patron.matcher(_args[j].trim());	
			match.find();						
			
			if(!match.matches())
			{
				throw new IllegalArgumentException("No existen match para la cadena: "+_args[j]);
			}
			
			String nombre_nodo 	= match.group(1);
			String nodo_derecha = match.group(2);
			String distancia    = match.group(3);
			
			//Buscamos que el nodo ya este en la lista
			Optional<Nodo> nodo_presente = red.stream().filter(p -> p.getNombre().equalsIgnoreCase(nombre_nodo)).findFirst();
			
			if(nodo_presente.isPresent())
			{
				//Verificamos que la conexion exista
				Optional<Nodo> nodo_der = nodo_presente.get().getNodo_der().stream().filter(h -> h.getNombre().equalsIgnoreCase(nodo_derecha)).findFirst();
				
				if(nodo_der.isPresent())
				{
					
					
				}else
				{
					Nodo nodo_dere = new Nodo(nodo_derecha, null, Integer.valueOf(distancia));
					Nodo nodo_existente = nodo_presente.get();
					int red_index = red.indexOf(nodo_existente);
					nodo_existente.getNodo_der().add(nodo_dere);
					red.set(red_index, nodo_existente);					
				}
				
			}else
			{
				Nodo nodo_root = new Nodo(nombre_nodo, null, 0);
				Nodo nodo_dere = new Nodo(nodo_derecha, null, Integer.valueOf(distancia));
				nodo_root.getNodo_der().add(nodo_dere);
				red.add(nodo_root);
			}	
			
			j++;
						
		}
		
		return red;
		
	}//construirRed
	
	/**
	 * 
	 * @param red
	 */
	private static void imprimirRed(List<Nodo> red)
	{
		for(Nodo nodo: red)
		{
			System.out.println("");
			System.out.println("Padre: "+nodo.getNombre());
			System.out.print(" -> ");
			nodo.getNodo_der().stream().map(Nodo::getNombre).forEach(p -> System.out.print(p+","));			
		}
		System.out.println("");
		
	}//imprimirRed
	
	/**
	 * 
	 * @param red
	 * @param origen
	 * @param fin
	 */
	private static int caminoMasCorto(List<Nodo> _red, String _inicio, String _fin, Map<String, String> _padres, int _numero_padres)
	{
		String fin_temp = _fin;
		Map<String, String> padres 			 =  new HashMap<String, String>();
		Map<String, Integer> padres_distancia =  new HashMap<String, Integer>();
		
		int numero_padres = _numero_padres;
		
		Optional<Nodo> nodo_init = _red.stream().filter(p -> p.getNombre().equalsIgnoreCase(_inicio)).findFirst();
		
		for(Nodo nodo: _red)
		{		
			Optional<Nodo> nodo_fin = nodo.getNodo_der().stream().filter(p -> p.getNombre().equalsIgnoreCase(fin_temp)).findFirst();
			
			if(nodo_fin.isPresent())
			{
				if(nodo.getNombre().equalsIgnoreCase(_inicio))
					numero_padres++;
				
				padres.put(nodo.getNombre(), nodo_fin.get().getNombre());
				padres_distancia.put(nodo.getNombre(), nodo_fin.get().getDistancia());
			}
		}
		
		for(Map.Entry<String, String> entry: padres.entrySet())
		{
			if(entry.getValue().equalsIgnoreCase(_fin) && !entry.getKey().equalsIgnoreCase(_inicio))
			{
				_fin = entry.getKey();
				break;
			}
		}
		
		padres.forEach((llave, valor) -> System.out.println("Padre: "+llave + " Hijo: "+valor));
		padres_distancia.forEach((llave, valor) -> System.out.println("Padre: "+llave + " Distancia: "+valor));
		
		if(numero_padres >= nodo_init.get().getNodo_der().size())
			System.out.println("Numero maximo de conexiones a padres iniciales "+_inicio);
		else
			caminoMasCorto(_red, _inicio, _fin, _padres, numero_padres);		
		
		return 0;
		
	}//caminoMasCorto

}//NoBorrar
