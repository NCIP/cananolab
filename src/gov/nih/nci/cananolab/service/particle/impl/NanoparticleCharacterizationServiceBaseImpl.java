package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryRowBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.particle.helper.NanoparticleCharacterizationServiceHelper;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;

import org.apache.log4j.Logger;

/**
 * Base implementation of NanoparticleCharacterizationService, shared by local
 * impl and remote impl.
 * 
 * @author pansu
 * 
 */
public abstract class NanoparticleCharacterizationServiceBaseImpl {
	Logger logger = Logger
	.getLogger(NanoparticleCharacterizationServiceBaseImpl.class);
	private NanoparticleCharacterizationServiceHelper helper = new NanoparticleCharacterizationServiceHelper();

	public void exportDetail(CharacterizationBean achar, OutputStream out)
			throws ParticleCharacterizationException {
		helper.exportDetail(achar, out);
	}

	public void exportFullSummary(CharacterizationSummaryBean summaryBean,
			OutputStream out) throws IOException {
		helper.exportFullSummary(summaryBean, out);
	}

	public void exportSummary(CharacterizationSummaryBean summaryBean,
			OutputStream out) throws IOException {
		helper.exportSummary(summaryBean, out);
	}

	protected abstract SortedSet<Characterization> findParticleCharacterizationsByClass(
			String particleName, String className) throws Exception;

	public CharacterizationSummaryBean getParticleCharacterizationSummaryByClass(
			String particleName, String className, UserBean user)
			throws ParticleCharacterizationException {
		CharacterizationSummaryBean charSummary = new CharacterizationSummaryBean(
				className);
		try {
			SortedSet<Characterization> charas = findParticleCharacterizationsByClass(
					particleName, className);
			if (charas.isEmpty()) {
				return null;
			}
			FileService fileService = new FileService();
			for (Characterization chara : charas) {
				CharacterizationBean charBean = new CharacterizationBean(chara);
				charSummary.getCharBeans().add(charBean);
				if (charBean.getDerivedBioAssayDataList() != null
						&& !charBean.getDerivedBioAssayDataList().isEmpty()) {
					for (DerivedBioAssayDataBean derivedBioAssayDataBean : charBean
							.getDerivedBioAssayDataList()) {
						fileService.retrieveVisibility(derivedBioAssayDataBean
								.getLabFileBean(), user);
						Map<String, String> datumMap = new HashMap<String, String>();
						for (DerivedDatum data : derivedBioAssayDataBean
								.getDatumList()) {
							String datumLabel = data.getName();
							if (data.getValueUnit() != null
									&& data.getValueUnit().length() > 0) {
								datumLabel += "(" + data.getValueUnit() + ")";
							}
							datumMap
									.put(datumLabel, data.getValue().toString());
						}
						CharacterizationSummaryRowBean charSummaryRow = new CharacterizationSummaryRowBean();
						charSummaryRow.setCharBean(charBean);
						charSummaryRow.setDatumMap(datumMap);
						charSummaryRow
								.setDerivedBioAssayDataBean(derivedBioAssayDataBean);
						charSummary.getSummaryRows().add(charSummaryRow);
						if (datumMap != null && !datumMap.isEmpty()) {
							charSummary.getColumnLabels().addAll(
									datumMap.keySet());
						}
					}
				} else {
					CharacterizationSummaryRowBean charSummaryRow = new CharacterizationSummaryRowBean();
					charSummaryRow.setCharBean(charBean);
					charSummary.getSummaryRows().add(charSummaryRow);
				}
			}
			return charSummary;
		} catch (Exception e) {
			String err = "Error getting " + particleName
					+ " characterization summary of type " + className;
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}
}
