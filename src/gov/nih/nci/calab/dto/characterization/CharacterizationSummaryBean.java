package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.dto.particle.ParticleBean;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
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
	private String actionName; // physical or inVitro

	private ParticleBean particle = new ParticleBean();

	private String characterizationType; // physical or in vitro

	private List<CharacterizationSummaryRowBean> summaryRows;

	private SortedSet<String> columnLabels;

	private List<CharacterizationBean> charBeans;

	private String characterizationName; // e.g. Molecular Weight

	private String exportFileName;

	private String fullExportFileName;

	private String dispatchActionName; // e.g. molecularWeight

	public String getActionName() {
		if (getCharacterizationType() != null)
			this.actionName = StringUtils.getOneWordLowerCaseFirstLetter(this
					.getCharacterizationType())
					+ "Characterization";
		return this.actionName;
	}

	public CharacterizationSummaryBean() {
		particle = new ParticleBean();
	}

	public String getCharacterizationType() {
		return characterizationType;
	}

	public void setCharacterizationType(String characterizationType) {
		this.characterizationType = characterizationType;
	}

	public ParticleBean getParticle() {
		return particle;
	}

	public void setParticle(ParticleBean particle) {
		this.particle = particle;
	}

	public void setActionName(String actionName) {
		this.actionName = actionName;
	}

	public List<CharacterizationSummaryRowBean> getSummaryRows() {
		return summaryRows;
	}

	public void setSummaryRows(List<CharacterizationSummaryRowBean> summaryRows) {
		this.summaryRows = summaryRows;
		this.charBeans = new ArrayList<CharacterizationBean>();
		this.columnLabels = new TreeSet<String>();
		for (CharacterizationSummaryRowBean summaryBean : summaryRows) {
			Map<String, String> datumMap = summaryBean.getDatumMap();
			if (datumMap != null && !datumMap.isEmpty()) {
				columnLabels.addAll(datumMap.keySet());
			}
			summaryBean.getCharBean().setParticle(particle);
			if (!charBeans.contains(summaryBean.getCharBean())) {
				charBeans.add(summaryBean.getCharBean());
			}
		}
	}

	public SortedSet<String> getColumnLabels() {
		return columnLabels;
	}

	public List<CharacterizationBean> getCharBeans() {
		return charBeans;
	}

	public String getCharacterizationName() {
		return characterizationName;
	}

	public void setCharacterizationName(String characterizationName) {
		this.characterizationName = characterizationName;
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
		nameParts.add(particle.getSampleName());
		nameParts.add(StringUtils
				.getOneWordUpperCaseFirstLetter(characterizationName));
		nameParts.add(function);
		nameParts.add(StringUtils.convertDateToString(new Date(),
				"yyyyMMdd_HH-mm-ss-SSS"));
		exportFileName = StringUtils.join(nameParts, "_");
		return exportFileName;
	}

	public String getDispatchActionName() {
		dispatchActionName = StringUtils
				.getOneWordLowerCaseFirstLetter(characterizationName);
		return dispatchActionName;
	}
}
