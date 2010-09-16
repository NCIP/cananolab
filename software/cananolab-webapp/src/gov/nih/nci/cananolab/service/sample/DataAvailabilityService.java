package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.dto.particle.DataAvailabilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DataAvailabilityException;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.security.SecurityService;

import java.util.List;
import java.util.Set;

public interface DataAvailabilityService {

	/**
	 * find data availability for a sample
	 *
	 * @param sampleId
	 * @param securityService
	 * @return
	 * @throws DataAvailabilityException
	 * @throws NoAccessException
	 * @throws SecurityException
	 */
	public Set<DataAvailabilityBean> findDataAvailabilityBySampleId(
			String sampleId, SecurityService securityService)
			throws DataAvailabilityException, NoAccessException,
			SecurityException;

	/**
	 * generate data availability for the sample and persist to database table
	 *
	 * @param sampleBean
	 * @param user
	 * @return
	 * @throws DataAvailabilityException
	 */
	/*
	 * public List<DataAvailabilityBean> generateDataAvailability(SampleBean
	 * sampleBean, UserBean user) throws DataAvailabilityException;
	 */
	/**
	 * generate data availability for the sample and persist to database table
	 *
	 * @param sampleBean
	 * @param securityService
	 * @return
	 * @throws DataAvailabilityException
	 * @throws NoAccessException
	 * @throws SecurityException
	 */
	public Set<DataAvailabilityBean> generateDataAvailability(
			SampleBean sampleBean, SecurityService securityService)
			throws DataAvailabilityException, NoAccessException,
			SecurityException;

	/**
	 * find update to data availability for the sample and persist to database
	 * table
	 *
	 * @param sampleBean
	 * @param securityService
	 * @return
	 * @throws DataAvailabilityException
	 * @throws NoAccessException
	 * @throws SecurityException
	 */
	public Set<DataAvailabilityBean> saveDataAvailability(
			SampleBean sampleBean, SecurityService securityService)
			throws DataAvailabilityException, NoAccessException,
			SecurityException;

	/**
	 * delete data availability for the sample from database table
	 *
	 * @param sampleId
	 * @param securityService
	 * @throws DataAvailabilityException
	 * @throws NoAccessException
	 * @throws SecurityException
	 */
	public void deleteDataAvailability(String sampleId,
			SecurityService securityService) throws DataAvailabilityException,
			NoAccessException, SecurityException;

	public void generateBatch(SecurityService securityService,
			List<String> sampleIds) throws Exception;

	public void deleteAll(SecurityService securityService) throws Exception;

	public List<String> findSampleIds(SecurityService securityService)
			throws Exception;
}
