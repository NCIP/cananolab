package gov.nih.nci.cananolab.service.common;

import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.Technique;
import gov.nih.nci.cananolab.dto.common.InstrumentBean;
import gov.nih.nci.cananolab.dto.common.TechniqueBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.InstrumentTechniqueException;

import java.util.List;

/**
 *
 * @author pansu
 *
 */
public interface InstrumentTechniqueService {
	public void saveInstrument(Instrument instrument)
			throws InstrumentTechniqueException, DuplicateEntriesException;

	public List<InstrumentBean> findAllInstruments()
			throws InstrumentTechniqueException;

	public void saveTechnique(Technique Technique)
			throws InstrumentTechniqueException, DuplicateEntriesException;

	public List<TechniqueBean> findAllTechniques()
			throws InstrumentTechniqueException;
}
