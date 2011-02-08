package colors.artefacts.generators;

import colors.Factory;
import colors.interfaces.Artefact;
import colors.interfaces.ArtefactGenerator;

public class NullArtefactGenerator implements ArtefactGenerator {
	public static Factory<ArtefactGenerator> factory() {
		return new Factory<ArtefactGenerator>(){
			@Override
			public ArtefactGenerator instantiate() {
				return new NullArtefactGenerator();
			}
		};
	}
	@Override
	public Artefact generate() {
		throw new UnsupportedOperationException("This generator doesn't actually generate.");
	}
}
