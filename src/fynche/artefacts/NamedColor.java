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
package fynche.artefacts;

import java.awt.Color;

import fynche.interfaces.Artefact;

public class NamedColor implements Artefact {
	private static final long serialVersionUID = 1L;
	private final Color color;
	private final String name;
	public NamedColor(final int r, final int g, final int b, final String name) {
		this(new Color(r, g, b), name);
	}
	
	public NamedColor(Color color, String name) {
		this.color = color;
		this.name = name;
	}
	
	public Color getColor() {
		return color;
	}
	
	public String getName() {
		return name;
	}
	
	public String toString() {
		return "NamedColor[(" + color.getRed() + "," + color.getGreen() + "," + color.getBlue() + ")," + name + "]";
	}
}
