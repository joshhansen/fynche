package colors.artefacts.initers;

import java.util.Collections;
import java.util.Set;

import colors.interfaces.Agent;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactGenerator;
import colors.interfaces.ArtefactInitializer;
import colors.interfaces.Factory;
import colors.util.Util.SmartStaticFactory;

public class NullArtefactCombo implements ArtefactInitializer, ArtefactGenerator {
	public static final Factory<NullArtefactCombo> factory = new SmartStaticFactory<NullArtefactCombo>(){
		@Override
		protected NullArtefactCombo instantiate_() {
			return new NullArtefactCombo();
		}
	};
	
	@Override
	public Set<Artefact> initialArtefacts(Agent agent) {
		return Collections.emptySet();
	}
	
	@Override
	public Artefact generate(Agent agent) {
		throw new UnsupportedOperationException("This generator doesn't actually generate.");
	}
}
