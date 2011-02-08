package colors.artefacts;

import java.util.Collections;
import java.util.Set;

import colors.Factory;
import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactInitializer;

public class NullArtefactInitializer implements ArtefactInitializer {
	public static Factory<ArtefactInitializer> factory() {
		return new Factory<ArtefactInitializer>(){
			@Override
			public ArtefactInitializer instantiate() {
				return new NullArtefactInitializer();
			}
		};
	}
	@Override
	public Set<Artefact> initialArtefacts(Agent agent) {
		return Collections.emptySet();
	}
}
