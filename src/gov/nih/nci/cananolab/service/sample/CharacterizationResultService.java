package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.exception.CharacterizationResultException;

import java.util.List;

public interface CharacterizationResultService {

	public void saveData(List<Datum> data)
			throws CharacterizationResultException;

	public void saveFinding(Finding finding)
			throws CharacterizationResultException;

	public List<Datum> getDataForFinding(String findingId)
			throws CharacterizationResultException;

	public Finding findFindingById(String findingId)
			throws CharacterizationResultException;

	public void deleteFinding(Finding finding)
			throws CharacterizationResultException;
}
