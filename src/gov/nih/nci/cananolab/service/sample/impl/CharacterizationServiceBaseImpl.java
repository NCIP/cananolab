package gov.nih.nci.cananolab.service.sample.impl;

import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.sample.helper.CharacterizationServiceHelper;

import java.util.List;

import org.apache.log4j.Logger;

/**
 * Base implementation of CharacterizationService, shared by local impl and
 * remote impl.
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

}
