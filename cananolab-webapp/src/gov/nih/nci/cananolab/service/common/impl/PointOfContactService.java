package gov.nih.nci.cananolab.service.common.impl;

import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.PointOfContactException;
import gov.nih.nci.cananolab.service.BaseService;

import java.util.List;

public interface PointOfContactService extends BaseService {
	public PointOfContactBean findPointOfContactById(String pocId)
			throws PointOfContactException;

	public List<PointOfContactBean> findPointOfContactsBySampleId(
			String sampleId) throws PointOfContactException;

	public List<PointOfContactBean> findPointOfContactsByStudyId(
			String studyId) throws PointOfContactException;

	public void savePointOfContact(PointOfContactBean pointOfContactBean)
			throws PointOfContactException, NoAccessException;

}
