package colors.interfaces;


public interface PublicationDecider {
	public boolean shouldPublish(final Agent potentialPublisher, final Artefact artefact, final Agent toAgent);
}