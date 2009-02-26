package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.particle.helper.NanoparticleCharacterizationServiceHelper;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Base implementation of NanoparticleCharacterizationService, shared by local
 * impl and remote impl.
 *
 * @author pansu
 *
 */
public abstract class NanoparticleCharacterizationServiceBaseImpl {
	private static Logger logger = Logger
			.getLogger(NanoparticleCharacterizationServiceBaseImpl.class);

	private NanoparticleCharacterizationServiceHelper helper = new NanoparticleCharacterizationServiceHelper();
	protected FileService fileService;

	protected abstract List<Characterization> findParticleCharacterizationsByClass(
			String particleName, String className) throws Exception;

	public CharacterizationSummaryBean getParticleCharacterizationSummaryByClass(
			String particleName, String className, UserBean user)
			throws ParticleCharacterizationException {
		CharacterizationSummaryBean charSummary = new CharacterizationSummaryBean(
				className);
		try {
			List<Characterization> charas = findParticleCharacterizationsByClass(
					particleName, className);
			if (charas.isEmpty()) {
				return null;
			}
			for (Characterization chara : charas) {
				CharacterizationBean charBean = new CharacterizationBean(chara);
				charSummary.getCharBeans().add(charBean);
				//TODO:
//				if (charBean.getDerivedBioAssayDataList() != null
//						&& !charBean.getDerivedBioAssayDataList().isEmpty()) {
//					for (DerivedBioAssayDataBean derivedBioAssayDataBean : charBean
//							.getDerivedBioAssayDataList()) {
//						if (fileService instanceof FileServiceLocalImpl) {
//							fileService.retrieveVisibility(
//									derivedBioAssayDataBean.getFileBean(),
//									user);
//						}
//						Map<String, String> datumMap = new HashMap<String, String>();
//						for (DerivedDatumBean data : derivedBioAssayDataBean
//								.getDatumList()) {
//							String datumLabel = data.getDomainDerivedDatum()
//									.getName();
//							if (data.getDomainDerivedDatum().getValueUnit() != null
//									&& data.getDomainDerivedDatum().getValueUnit().length() > 0) {
//								datumLabel += "(" + data.getDomainDerivedDatum().getValueUnit() + ")";
//							}
//							datumMap
//									.put(datumLabel, data.getValueStr());
//						}
//						CharacterizationSummaryRowBean charSummaryRow = new CharacterizationSummaryRowBean();
//						charSummaryRow.setCharBean(charBean);
//						charSummaryRow.setDatumMap(datumMap);
//						charSummaryRow
//								.setDerivedBioAssayDataBean(derivedBioAssayDataBean);
//						charSummary.getSummaryRows().add(charSummaryRow);
//						if (datumMap != null && !datumMap.isEmpty()) {
//							charSummary.getColumnLabels().addAll(
//									datumMap.keySet());
//						}
//					}
//				} else {
//					CharacterizationSummaryRowBean charSummaryRow = new CharacterizationSummaryRowBean();
//					charSummaryRow.setCharBean(charBean);
//					charSummary.getSummaryRows().add(charSummaryRow);
//				}
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
