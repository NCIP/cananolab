package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.particle.characterization.Datum;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;

import java.util.List;

public interface NanoparticleCharacterizationResultService {

	public void saveData(List<Datum> data)
			throws CharacterizationResultException;

	public List<Datum> getDataForDataSet(String dataSetId)
			throws CharacterizationResultException;
}
