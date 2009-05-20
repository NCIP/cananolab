package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Base implementation of CharacterizationService, shared by local
 * impl and remote impl.
 *
 * @author pansu
 *
 */
public abstract class CharacterizationServiceBaseImpl {
	private static Logger logger = Logger
			.getLogger(CharacterizationServiceBaseImpl.class);

	protected CharacterizationServiceHelper helper = new CharacterizationServiceHelper();
	protected FileService fileService;

	protected abstract List<Characterization> findSampleCharacterizationsByClass(
			String sampleName, String className) throws Exception;

	public CharacterizationSummaryBean getSampleCharacterizationSummaryByClass(
			String sampleName, String className, UserBean user)
			throws CharacterizationException {
		CharacterizationSummaryBean charSummary = new CharacterizationSummaryBean(
				className);
		try {
			List<Characterization> charas = findSampleCharacterizationsByClass(
					sampleName, className);
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
			String err = "Error getting " + sampleName
					+ " characterization summary of type " + className;
			logger.error(err, e);
			throw new CharacterizationException(err, e);
		}
	}
}
