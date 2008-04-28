package gov.nih.nci.cananolab.dto.particle.characterization;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents summary view characterization properties to be shown in
 * characterization summary view pages.
 * 
 * @author pansu
 * 
 */
public class CharacterizationSummaryBean {
	private String particleName;

	private List<CharacterizationSummaryRowBean> summaryRows = new ArrayList<CharacterizationSummaryRowBean>();

	private SortedSet<String> columnLabels = new TreeSet<String>();

	private List<CharacterizationBean> charBeans = new ArrayList<CharacterizationBean>();

	private String characterizationClassName;

	public CharacterizationSummaryBean() {
	}

	public String getCharacterizationClassName() {
		return characterizationClassName;
	}

	public void setCharacterizationClassName(String characterizationClassName) {
		this.characterizationClassName = characterizationClassName;
	}

	public String getParticleName() {
		return particleName;
	}

	public void setParticleName(String particleName) {
		this.particleName = particleName;
	}

	public List<CharacterizationSummaryRowBean> getSummaryRows() {
		return summaryRows;
	}

	public SortedSet<String> getColumnLabels() {
		return columnLabels;
	}

	public List<CharacterizationBean> getCharBeans() {
		return charBeans;
	}
}
