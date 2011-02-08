package colors.artefacts.pubdec;

import colors.Factory;
import colors.interfaces.Artefact;
import colors.interfaces.PublicationDecider;

public class NullPublicationDecider implements PublicationDecider {
	public static Factory<PublicationDecider> factory() {
		return new Factory<PublicationDecider>(){
			@Override
			public PublicationDecider instantiate() {
				return new NullPublicationDecider();
			}};
	}
	@Override
	public boolean shouldPublish(Artefact artefact) {
		return false;
	}
}
