package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryViewBean;
import gov.nih.nci.cananolab.exception.CharacterizationException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;

import java.io.OutputStream;
import java.util.List;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

/**
 * Interface defining service methods involving characterizations
 *
 * @author pansu, tanq
 *
 */
public interface CharacterizationService {

	public void saveCharacterization(Sample particleSample,
			Characterization achar) throws Exception;

	public Characterization findCharacterizationById(String charId)
			throws CharacterizationException;

	public Characterization findCharacterizationById(String charId,
			String className) throws CharacterizationException;

	public SortedSet<String> findAllCharacterizationSources()
			throws CharacterizationException;

	public CharacterizationSummaryBean getSampleCharacterizationSummaryByClass(
			String sampleName, String className, UserBean user)
			throws CharacterizationException;

	// set lab file visibility of a characterization
	public void retrieveVisiblity(CharacterizationBean charBean, UserBean user)
			throws CharacterizationException;

	public void deleteCharacterization(Characterization chara)
			throws CharacterizationException;

	public List<CharacterizationBean> findCharsBySampleId(String sampleId)
			throws CharacterizationException;

	public void removePublicVisibility(
			AuthorizationService authService, Characterization aChar)
			throws Exception;

	public void assignPublicVisibility(
			AuthorizationService authService, Characterization aChar)
			throws Exception;

	/**
	 * Export sample characterization summary report as Excel spread sheet.
	 *
	 * @param summaryBean CharacterizationSummaryViewBean
	 * @param out OutputStream
	 * @throws CharacterizationException if error occurred.
	 */
	public void exportSummary(CharacterizationSummaryViewBean summaryBean,
			HttpServletRequest request, OutputStream out)
			throws CharacterizationException;
}