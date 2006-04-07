package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.SampleSOP;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

/**
 * 
 * @author pansu
 * 
 */

/* CVS $Id: ManageAliquotService.java,v 1.7 2006-04-07 15:27:13 pansu Exp $ */

public class ManageAliquotService {
	private static Logger logger = Logger.getLogger(ManageAliquotService.class);

	/**
	 * 
	 * @return all methods for creating aliquots
	 */
	public List<LabelValueBean> getAliquotCreateMethods(String urlPrefix) {
		List<LabelValueBean> createMethods = new ArrayList<LabelValueBean>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select sop.name, file.path from SampleSOP sop join sop.sampleSOPFileCollection file where sop.description='aliquot creation'";
			List results = ida.query(hqlString, SampleSOP.class.getName());
			for (Object obj : results) {
				String sopName = (String) ((Object[]) obj)[0];
				String sopURI = (String) ((Object[]) obj)[1];
				String sopURL = (sopURI == null) ? "" : urlPrefix + sopURI;
				createMethods.add(new LabelValueBean(sopName, sopURL));
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all sample sources", e);
			throw new RuntimeException("Error in retrieving all sample sources");
		}
		return createMethods;
	}

	public int getDefaultAliquotMatrixColumnNumber() {
		return 10;
	}

	/**
	 * 
	 * @param sampleId
	 * @param parentAliquotId
	 * @return the existing prefix for assigning a new aliquot ID.
	 */
	public String getAliquotPrefix(String sampleId, String parentAliquotId) {

		if (parentAliquotId.length() == 0) {
			return sampleId + "-";
		} else {
			return parentAliquotId + "-";
		}
	}

	/**
	 * 
	 * @param sampleId
	 * @param parentAliquotId
	 * @return the first number for assigning a new aliquot IDs.
	 */
	public int getFirstAliquotNum(String sampleId, String parentAliquotId) {
		int aliquotNum = 0;
		if (parentAliquotId.length() == 0) {
			aliquotNum = getLastSampleNum() + 1;
		} else {
			aliquotNum = getLastAliquotNum() + 1;
		}
		return aliquotNum;
	}

	private int getLastSampleNum() {
		// TODO query db for the actual data
		return 0;
	}

	private int getLastAliquotNum() {
		// TODO query db for the actual data
		return 0;
	}

	/**
	 * Save the aliquots into the database
	 * 
	 * @param sampleId
	 * @param parentAliquotId
	 * @param aliquotMatrix
	 * @param comments
	 * @throws Exception
	 */
	public void saveAliquots(String sampleId, String parentAliquotId,
			List aliquotMatrix, String comments) throws Exception {
		// TODO fill in details for saving aliquots
	}
}