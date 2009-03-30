package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.common.DataSet;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;

import java.util.List;

public interface CharacterizationResultService {

	public void saveData(List<Datum> data)
			throws CharacterizationResultException;

	public void saveDataSet(DataSet dataSet)
			throws CharacterizationResultException;

	public List<Datum> getDataForDataSet(String dataSetId)
			throws CharacterizationResultException;

	public DataSet findDataSetById(String dataSetId)
			throws CharacterizationResultException;

	public void deleteDataSet(DataSet dataSet)
			throws CharacterizationResultException;
}
