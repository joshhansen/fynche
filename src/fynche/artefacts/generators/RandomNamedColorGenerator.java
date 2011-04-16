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
package fynche.artefacts.generators;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import fynche.artefacts.NamedColor;
import fynche.interfaces.Agent;
import fynche.interfaces.ArtefactGenerator;
import fynche.interfaces.Factory;
import fynche.util.Rand;
import fynche.util.Util.SmartStaticFactory;

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
		final int r = Rand.nextInt(256);
		final int g = Rand.nextInt(256);
		final int b = Rand.nextInt(256);
		final Color c = new Color(r, g, b);
		final String word = dict.get(Rand.nextInt(dict.size())).replace("'s", "");
		return new NamedColor(c, word);
	}
}
