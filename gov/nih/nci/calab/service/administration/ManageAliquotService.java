package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.SampleContainer;
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

/* CVS $Id: ManageAliquotService.java,v 1.8 2006-04-07 18:27:19 pansu Exp $ */

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
			aliquotNum = getLastSampleAliquotNum(sampleId) + 1;
		} else {
			aliquotNum = getLastAliquotChildAliquotNum(parentAliquotId) + 1;
		}
		return aliquotNum;
	}

	private int getLastSampleAliquotNum(String sampleId) {
		int aliquotNum=0;
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select container from Sample sample join sample.sampleContainerCollection container where sample.name='"+sampleId+"'";
			List results = ida.query(hqlString, SampleSOP.class.getName());
			for (Object obj : results) {
				SampleContainer container=(SampleContainer)obj;
				if (container instanceof Aliquot) {
					String aliquotName=((Aliquot)container).getName();
					String[] aliquotNameParts=aliquotName.split("-");
					int aliquotSeqNum=Integer.parseInt(aliquotNameParts[aliquotNameParts.length]);
					if (aliquotSeqNum>aliquotNum) {
						aliquotNum=aliquotSeqNum;						
					}
				}
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving the last sample aliquot number", e);
			throw new RuntimeException("Error in retrieving the last sample aliquot number");
		}
		return aliquotNum;
	}

	private int getLastAliquotChildAliquotNum(String parentAliquotId) {
		int aliquotNum=0;
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select child from Aliquot parent join parent.childSampleContainerCollection child where parent.name='"+parentAliquotId+"'";
			List results = ida.query(hqlString, SampleSOP.class.getName());
			for (Object obj : results) {
				SampleContainer container=(SampleContainer)obj;
				if (container instanceof Aliquot) {
					String aliquotName=((Aliquot)container).getName();
					String[] aliquotNameParts=aliquotName.split("-");
					int aliquotSeqNum=Integer.parseInt(aliquotNameParts[aliquotNameParts.length]);
					if (aliquotSeqNum>aliquotNum) {
						aliquotNum=aliquotSeqNum;						
					}
				}
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving the last aliquot child aliquot number", e);
			throw new RuntimeException("Error in retrieving the last aliquot child aliquot number");
		}
		return aliquotNum;
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