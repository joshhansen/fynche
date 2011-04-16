/*
 * Fynche - a Framework for Multiagent Computational Creativity
 * Copyright 2011 Josh Hansen
 * 
 * This file is part of the Fynche <https://github.com/joshhansen/fynche>.
 * 
 * Fynche is free software: you can redistribute it and/or modify it
 * under the terms of the GNU Affero General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or (at your
 * option) any later version.
 * 
 * Fynche is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License
 * for more details.
 * 
 * You should have received a copy of the GNU Affero General Public License
 * along with Fynche.  If not, see <http://www.gnu.org/licenses/>.
 * 
 * If you have inquiries regarding any further use of Fynche, please
 * contact Josh Hansen <http://joshhansen.net/>
 */
package fynche;

import java.util.Collection;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;


public class BetterProperties {
	private final Properties props = new Properties();
	public static class NoSuchPropException extends IllegalArgumentException {
		public NoSuchPropException(String message) {
			super(message);
		}
	}
	
	public void setProp(final String propName, final Object value) {
		props.put(propName, value);
	}
	
	public Object getProp(final String propName) {
		return props.get(propName);
	}
	
	public String getPropS(final String propName) {
		final Object val = props.get(propName);
		if(val == null) throw new NoSuchPropException("Property '" + propName + "' not found.");
		if(!(val instanceof String)) throw new NoSuchPropException("Property '" + propName + "' is not a string.");
		return (String) val;
	}
	
	public int getPropI(final String propName) {
		final Object val = props.get(propName);
		if(val == null) throw new NoSuchPropException("Property '" + propName + "' not found.");
		if(!(val instanceof Integer)) throw new NoSuchPropException("Property '" + propName + "' is not an integer.");
		return ((Integer)val).intValue();
	}
	
	public boolean getPropB(final String propName) {
		final Object val = props.get(propName);
		if(val == null) throw new NoSuchPropException("Property '" + propName + "' not found.");
		if(!(val instanceof Boolean)) throw new NoSuchPropException("Property '" + propName + "' is not a boolean.");
		return ((Boolean) val).booleanValue();
	}
	
	public double getPropD(final String propName) {
		final Object val = props.get(propName);
		if(val == null) throw new NoSuchPropException("Property '" + propName + "' not found.");
		if(!(val instanceof Double)) throw new NoSuchPropException("Property '" + propName + "' is not a double.");
		return ((Double)val).doubleValue();
	}

	public Set<Entry<Object, Object>> entrySet() {
		return props.entrySet();
	}

	public Set<String> keySet() {
		return props.stringPropertyNames();
	}

	public Collection<Object> values() {
		return props.values();
	}
	
	
}
