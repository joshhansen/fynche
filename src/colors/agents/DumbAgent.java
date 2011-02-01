package colors.agents;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import colors.MultiAgentSystem;
import colors.artefacts.Artefact;
import colors.artefacts.NamedColor;

public class DumbAgent extends AbstractAgent {
	private static final long serialVersionUID = 1L;
	
	public DumbAgent(MultiAgentSystem sys) {
		super(sys);
	}
	
	
	@Override
	public void roundStart() {}

	@Override
	public void rate() {}
	
	@Override
	public void roundFinish() {}

	@Override
	protected int numArtefactsToGenerate() {
		return 1;
	}

	@Override
	protected boolean shouldPublish(Artefact a) {
		return true;
	}
}
