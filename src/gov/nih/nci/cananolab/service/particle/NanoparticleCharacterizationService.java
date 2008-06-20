package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.service.security.AuthorizationService;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.SortedSet;

/**
 * Interface defining service methods involving characterizations
 * 
 * @author pansu, tanq
 * 
 */
public interface NanoparticleCharacterizationService {

	public void saveCharacterization(NanoparticleSample particleSample,
			Characterization achar) throws Exception;

	public Characterization findCharacterizationById(String charId)
			throws ParticleCharacterizationException;

	public Characterization findCharacterizationById(String charId,
			String className) throws ParticleCharacterizationException;

	public SortedSet<String> findAllCharacterizationSources()
			throws ParticleCharacterizationException;

	public List<Instrument> findAllInstruments()
			throws ParticleCharacterizationException;

	public Instrument findInstrumentBy(String instrumentType,
			String manufacturer) throws ParticleCharacterizationException;

	public CharacterizationSummaryBean getParticleCharacterizationSummaryByClass(
			String particleName, String className, UserBean user)
			throws ParticleCharacterizationException;

	// set lab file visibility of a characterization
	public void retrieveVisiblity(CharacterizationBean charBean, UserBean user)
			throws ParticleCharacterizationException;

	public void exportDetail(CharacterizationBean achar, OutputStream out, String filePath)
		throws ParticleCharacterizationException;

	// private short setDetailSheet(CharacterizationBean achar, HSSFWorkbook wb,
	// HSSFSheet sheet, HSSFPatriarch patriarch, short rowCount);

	public void exportSummary(CharacterizationSummaryBean summaryBean,
			OutputStream out) throws IOException;

	// private short setSummarySheet(CharacterizationSummaryBean summaryBean,
	// HSSFWorkbook wb, HSSFSheet sheet, short rowCount);

	public void deleteCharacterization(Characterization chara)
			throws ParticleCharacterizationException;

	public void exportFullSummary(CharacterizationSummaryBean summaryBean,
			OutputStream out, String remoteDownloadUrl) throws IOException;

	public List<Characterization> findCharsByParticleSampleId(String particleId)
			throws ParticleCharacterizationException;

	public void removeCharacterizationPublicVisibility(
			AuthorizationService authService, Characterization aChar)
			throws Exception;

	public void assignCharacterizationPublicVisibility(
			AuthorizationService authService, Characterization aChar) 
			throws Exception;

}