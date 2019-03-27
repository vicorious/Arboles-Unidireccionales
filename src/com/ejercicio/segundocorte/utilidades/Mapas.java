package com.ejercicio.segundocorte.utilidades;

import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;

/**
 * 
 * @author User
 *
 */
public abstract class Mapas 
{
	/**
	 * 
	 * @param map
	 * @param value
	 * @return
	 */
	public static <T, E> Set<T> getKeysByValue(Map<T, E> map, E value) 
	{
	    Set<T> keys = new HashSet<T>();
	    for (Entry<T, E> entry : map.entrySet()) 
	    {
	        if (Objects.equals(value, entry.getValue())) 
	        {
	            keys.add(entry.getKey());
	        }
	    }
	    
	    return keys;
	    
	}//getKeysByValue

}//Mapas
