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
		Map<String, String> distancias = new HashMap<String, String>();
		
		String[] ultima = args[args.length - 1].split("-");						
		origen = ultima[0];
		fin    = ultima[1];
		
		int i = caminoMasCorto(red, origen, fin, padres, distancias, 0, fin, origen);
		
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
	private static int caminoMasCorto(List<Nodo> _red, String _inicio, String _fin, Map<String, String> _padres, Map<String, String> _padres_distancias,  int _numero_padres, String _fin_temp, String _inicio_temp_)
	{
		String fin_temp 					  = _fin;
		String _inicio_temp					  = _inicio;	
		
		Map<String, String> padres_distancias = _padres_distancias;
		
		int numero_padres = _numero_padres;
		
		Optional<Nodo> nodo_init = _red.stream().filter(p -> p.getNombre().equalsIgnoreCase(_inicio_temp)).findFirst();
		
		for(Nodo nodo: _red)
		{		
			Optional<Nodo> nodo_fin = nodo.getNodo_der().stream().filter(p -> p.getNombre().equalsIgnoreCase(fin_temp)).findFirst();
			
			if(nodo_fin.isPresent())
			{
				if(nodo.getNombre().equalsIgnoreCase(_inicio))
					numero_padres++;
																
				if(_padres.get(nodo.getNombre()) != null)
				{
					//Ancestro cercano
					_padres.put(nodo.getNombre().concat("-").concat(_padres.get(nodo.getNombre())), nodo_fin.get().getNombre());
				}
				_padres.put(nodo.getNombre(), nodo_fin.get().getNombre());
				padres_distancias.put(nodo.getNombre().concat("-").concat(nodo_fin.get().getNombre()), String.valueOf(nodo_fin.get().getDistancia()));
				
			}
			
		}
		
		for(Map.Entry<String, String> entry: _padres.entrySet())
		{
			if(entry.getValue().equalsIgnoreCase(_fin))
			{
				if(!entry.getKey().equalsIgnoreCase(_inicio))
				{
					_fin = entry.getKey();
				}															
			}
			
		}
		
		//Unimos los padres en caminos
		//System.out.println("******************************");
		//_distancias.forEach((llave, valor) -> System.out.println("Pareja: "+llave + " Distancia: "+valor));
		//_padres.forEach((llave, valor) -> System.out.println("Padre: "+llave + " Hijo: "+valor));
		//padres_distancias.forEach((llave, valor) -> System.out.println("Padre: "+llave + " Distancia: "+valor));
		
		String distancia = new String();
		boolean directo = false;
		//Termina de recorrer los padres
		if(numero_padres >= nodo_init.get().getNodo_der().size())
		{
			//Sacamos las rutas directas sin ancestros
			for(Map.Entry<String, String> entry: _padres_distancias.entrySet())
			{
				if(entry.getKey().split("-")[0].equalsIgnoreCase(_inicio_temp_) && entry.getKey().split("-")[1].equalsIgnoreCase(_fin_temp))
				{
					System.out.println("Primera ruta: "+_inicio_temp_ + "-" + _fin_temp + " Distancia: "+entry.getValue());	
					directo = true;
				}
			}
			
			if(directo)
				_padres_distancias.remove(_inicio_temp_ + "-" + _fin_temp);
			
			for(Map.Entry<String, String> entry: _padres_distancias.entrySet())
			{
				String[] pareja = entry.getKey().split("-");				
				String hijo 	= pareja[1];
				
				if(hijo.equalsIgnoreCase(_fin_temp))
				{
					String camino = new String();
					
					camino = unirCaminos(camino, _padres_distancias, entry.getKey(), _inicio_temp_);
							
					if(!camino.isEmpty())
					{						
						System.out.println("Camino: "+camino);
					}	
					
				}												
				
			}			
			
			System.out.println("************* FIN *****************");
			padres_distancias.forEach((llave, valor) -> System.out.println("Padre: "+llave + " Distancia: "+valor));
			System.out.println(distancia);
			System.out.println("Numero maximo de conexiones a padres iniciales "+_inicio);
		}
		else
		{
			caminoMasCorto(_red, _inicio, _fin, _padres, padres_distancias, numero_padres, _fin_temp, _inicio_temp_);
		}
		
		return 0;
		
	}//caminoMasCorto
	
	/**
	 * 
	 * @param _inicio
	 * @param _inicio_temp
	 * @param _fin
	 * @param _camino
	 * @param _padres
	 * @return
	 */
	public static String unirCaminos(String _camino, Map<String, String> _padres, String _key, String _inicio)
	{
		String[] pareja = _key.split("-");
		String padre = pareja[0];		
		
		String distancia_1 = _padres.get(_key);
		
		for(Map.Entry<String, String> entry: _padres.entrySet())
		{
			String[] pareja_i = _key.split("-");
			String padre_i 	  = pareja_i[0];
			String hijo_i  	  = pareja_i[1];
			
			if(padre.equalsIgnoreCase(hijo_i))
			{
				if(padre_i.equalsIgnoreCase(_inicio))
				{					
					_camino = _camino.concat(_key).concat("-").concat(entry.getKey()).concat(":").concat(distancia_1);
					
					System.out.println("Camino_i: "+_camino);
					
					_camino = "";
					
				}else
				{					
					_camino = _camino.concat(_key).concat("-").concat(entry.getKey());
				}
								
				distancia_1 = String.valueOf(Integer.parseInt(distancia_1) + Integer.parseInt(entry.getValue()));
			}
		}
								
		return _camino;
	}//unirCaminos

}//NoBorrar
