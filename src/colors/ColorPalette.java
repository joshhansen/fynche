package colors;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ColorPalette extends Artefact {
	private static final long serialVersionUID = 1L;
	private final Set<NamedColor> colors = new HashSet<NamedColor>();
	private final String name;
	public ColorPalette(String name, NamedColor... colors) {
		this.name = name;
		for(NamedColor color : colors) {
			this.colors.add(color);
		}
	}
	
	public Set<NamedColor> getColors() {
		return Collections.unmodifiableSet(colors);
	}
	
	public String getName() {
		return name;
	}
}
