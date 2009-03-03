package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.DataSet;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;

import java.util.List;

public interface NanoparticleCharacterizationResultService {

	public void saveData(List<Datum> data)
			throws CharacterizationResultException;

	public List<Datum> getDataForDataSet(String dataSetId)
			throws CharacterizationResultException;

	public DataSet findDataSetById(String dataSetId)
			throws CharacterizationResultException;
}
