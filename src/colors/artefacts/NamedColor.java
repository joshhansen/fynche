package colors.artefacts;

import java.awt.Color;

public class NamedColor implements Artefact {
	private static final long serialVersionUID = 1L;
	private final Color color;
	private final String name;
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
