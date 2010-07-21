package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.DataAvailabilityBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.exception.DataAvailabilityException;

import java.util.List;

public interface DataAvailabilityService {

	/**
	 * find data availability for a sample
	 * @param sampleId
	 * @return
	 * @throws DataAvailabilityException
	 */
	public List<DataAvailabilityBean> findDataAvailabilityBySampleId(String sampleId) 
		throws DataAvailabilityException;
	
	/**
	 * generate data availability for the sample and persist to
	 * database table
	 * @param sampleBean
	 * @param user
	 * @return
	 * @throws DataAvailabilityException
	 */
	public List<DataAvailabilityBean> generateDataAvailability(SampleBean sampleBean, UserBean user) 
		throws DataAvailabilityException;
	
	/**
	 * find update to data availability for the sample and persist to database table
	 * @param sampleBean
	 * @param user
	 * @return
	 * @throws DataAvailabilityException
	 */
	public List<DataAvailabilityBean> saveDataAvailability(SampleBean sampleBean, UserBean user) 
		throws DataAvailabilityException;
	
	/**
	 * delete data availability for the sample from database table
	 * @param sampleId
	 * @throws DataAvailabilityException
	 */
	public void deleteDataAvailability(String sampleId) throws DataAvailabilityException;
}
