package colors.interfaces;


public interface PublicationDecider {
	public boolean shouldPublish(final Artefact artefact, final Agent toAgent);
}