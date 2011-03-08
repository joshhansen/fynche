package colors.artefacts.generators;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import colors.MultiAgentSystem;
import colors.artefacts.NamedColor;
import colors.interfaces.Agent;
import colors.interfaces.ArtefactGenerator;
import colors.interfaces.Factory;
import colors.util.Util.SmartStaticFactory;

public class RandomNamedColorGenerator implements ArtefactGenerator {
	public static Factory<RandomNamedColorGenerator> factory = new SmartStaticFactory<RandomNamedColorGenerator>(){
		@Override
		protected RandomNamedColorGenerator instantiate_() {
			return new RandomNamedColorGenerator();
		}
	};
	
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
	
	private static final List<String> dict = Collections.unmodifiableList(readAsList("/usr/share/dict/words"));

	@Override
	public NamedColor generate(Agent agent) {
		final int r = MultiAgentSystem.rand.nextInt(256);
		final int g = MultiAgentSystem.rand.nextInt(256);
		final int b = MultiAgentSystem.rand.nextInt(256);
		final Color c = new Color(r, g, b);
		final String word = dict.get(MultiAgentSystem.rand.nextInt(dict.size())).replace("'s", "");
		return new NamedColor(c, word);
	}
}
