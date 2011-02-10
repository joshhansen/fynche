package colors.artefacts.generators;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import colors.artefacts.NamedColor;
import colors.interfaces.Agent;
import colors.interfaces.ArtefactGenerator;

public class RandomNamedColorGenerator implements ArtefactGenerator {	
	private static List<String> readAsList(final String filename) {
		List<String> list = new ArrayList<String>();
		try {
			BufferedReader r = new BufferedReader(new FileReader(filename));
			String tmp = null;
			while( (tmp=r.readLine()) != null) {
				list.add(tmp);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
		return list;
	}
	
	private final List<String> dict = Collections.unmodifiableList(readAsList("/usr/share/dict/words"));

	private final Random rand = new Random();
	@Override
	public NamedColor generate(Agent agent) {
		final int r = rand.nextInt(256);
		final int g = rand.nextInt(256);
		final int b = rand.nextInt(256);
		final Color c = new Color(r, g, b);
		final String word = dict.get(rand.nextInt(dict.size())).replace("'s", "");
		return new NamedColor(c, word);
	}
}
