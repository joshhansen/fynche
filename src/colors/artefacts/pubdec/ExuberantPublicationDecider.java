package colors.artefacts.pubdec;

import colors.Factory;
import colors.interfaces.Artefact;
import colors.interfaces.PublicationDecider;

public class ExuberantPublicationDecider implements PublicationDecider {
	public static Factory<PublicationDecider> factory() {
		return new Factory<PublicationDecider>(){
			@Override
			public PublicationDecider instantiate() {
				return new ExuberantPublicationDecider();
			}
		};
	}
	@Override
	public boolean shouldPublish(Artefact artefact) {
		return true;
	}
}
