package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;

/**
 * This class represents summary view characterization properties to be shown in
 * characterization summary view pages.
 * 
 * @author pansu
 * 
 */
public class CharacterizationSummaryBean {
	private String particleName;

	private String characterizationType; // physical or in vitro

	private List<CharacterizationSummaryRowBean> summaryRows;

	private SortedSet<String> columnLabels;

	private List<CharacterizationBean> charBeans;

	private String characterizationClassName; // e.g. MolecularWeight

	private String exportFileName;

	private String fullExportFileName;

	public CharacterizationSummaryBean() {
	}

	public String getCharacterizationType() {
		return characterizationType;
	}

	public void setCharacterizationType(String characterizationType) {
		this.characterizationType = characterizationType;
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

	public String getCharacterizationClassName() {
		return characterizationClassName;
	}

	public void setCharacterizationClassName(String characterizationName) {
		this.characterizationClassName = characterizationName;
	}

	public String getExportFileName() {
		exportFileName = composeExportFileName("summaryView");
		return exportFileName;
	}

	public String getFullExportFileName() {
		fullExportFileName = composeExportFileName("fullSummaryView");
		return fullExportFileName;
	}

	private String composeExportFileName(String function) {
		List<String> nameParts = new ArrayList<String>();
		nameParts.add(particleName);
		nameParts.add(StringUtils
				.getOneWordUpperCaseFirstLetter(characterizationClassName));
		nameParts.add(function);
		nameParts.add(StringUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS"));
		exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}
}
