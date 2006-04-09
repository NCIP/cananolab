package gov.nih.nci.calab.service.administration;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.SampleSOP;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerBean;
import gov.nih.nci.calab.service.util.CalabConstants;
import gov.nih.nci.calab.service.util.StringUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.struts.util.LabelValueBean;

/**
*
* @author pansu
*
*/

/* CVS $Id: ManageAliquotService.java,v 1.9 2006-04-09 21:57:22 zengje Exp $ 
*/

public class ManageAliquotService {
	private static Logger logger = 
Logger.getLogger(ManageAliquotService.class);

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
					int aliquotSeqNum=Integer.parseInt(aliquotNameParts[aliquotNameParts.length-1]);
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
			List<AliquotBean[]> aliquotMatrix, String comments) throws Exception {
		// TODO fill in details for saving aliquots
		// Check to if the aliquot is from Sample or Aliqot
        IDataAccess ida = (new DataAccessProxy()).getInstance(IDataAccess.HIBERNATE);
		try {
			ida.open();

			Aliquot parentAliquot = null;
			Sample sample = null;
			if ((parentAliquotId != null) && (parentAliquotId.length() > 0)){
				// Get aliqot ID and load the object
				List aliquots = ida.search("from Aliquot aliquot where aliquot.name='" + parentAliquotId + "'");
				parentAliquot = (Aliquot)aliquots.get(0);
			} else {
				List samples = ida.search("from Sample sample where sample.name='" + sampleId + "'");
				sample = (Sample)samples.get(0);
			}

			for (AliquotBean[] aliquotBeans:aliquotMatrix) {
				for(int i=0;i<aliquotBeans.length;i++) {
					AliquotBean aliquotBean = aliquotBeans[i];
					Aliquot doAliquot = new Aliquot();
					// use Hibernate Hilo algorithm to generate the id

					// Attributes
					// TODO: AliquotBean need to have a comment
					ContainerBean containerBean = aliquotBean.getContainer();
					doAliquot.setComments(comments);
					doAliquot.setConcentration(StringUtils.convertToFloat(containerBean.getConcentration()));
					doAliquot.setConcentrationUnit(containerBean.getConcentrationUnit());
					doAliquot.setContainerType(containerBean.getContainerType());
					doAliquot.setCreatedBy(aliquotBean.getCreator());
					doAliquot.setCreatedDate(StringUtils.convertToDate(aliquotBean.getCreationDate(),CalabConstants.DATE_FORMAT));
					doAliquot.setDiluentsSolvent(containerBean.getSolvent());
					//TODO:  construct the name (AliquotID is the whole name?)
					doAliquot.setName(aliquotBean.getAliquotId());
					doAliquot.setQuantity(StringUtils.convertToFloat(containerBean.getQuantity()));
					doAliquot.setQuantityUnit(containerBean.getQuantityUnit());
					doAliquot.setStorageCondition(containerBean.getStorageCondition());
					doAliquot.setSafetyPrecaution(containerBean.getSafetyPrecaution());
					doAliquot.setVolume(StringUtils.convertToFloat(containerBean.getVolume()));
					doAliquot.setVolumeUnit(containerBean.getVolumeUnit());

					// Associations
					// 1. ParentAliquos or Sample
					if ((parentAliquotId != null) && (parentAliquotId.length() > 0)){
						doAliquot.getParentSampleContainerCollection().add(parentAliquot);
					} else {
						doAliquot.setSample(sample);
					}

					ida.createObject(doAliquot);

					// TODO: 2. Protocol -- CreatedMethod is Protocol or string
//					doAliquot.setCreatedMethod()

					// 3. StorageElement
					HashSet<StorageElement> storages = new HashSet<StorageElement>();

					String boxValue = containerBean.getStorageLocation().getBox();
					if (( boxValue != null)&&(boxValue.length()>0)) {
						List existedSE = ida.search("from StorageElement se where se.type = '" + CalabConstants.STORAGE_BOX +
														 "' and se.location = '" + boxValue + "'");
						StorageElement box = null;
						if (existedSE.size() > 0){
							box = (StorageElement)existedSE.get(0);
						} else {
							box = new StorageElement();
							box.setLocation(boxValue);
							box.setType(CalabConstants.STORAGE_BOX);
							ida.store(box);
						}
						// Create releationship between this source and this sample
						storages.add(box);
					}

					String shelfValue = containerBean.getStorageLocation().getShelf();
					if ((shelfValue != null)&&(shelfValue.length()>0)) {
						List existedSE = ida.search("from StorageElement se where se.type = '" + CalabConstants.STORAGE_SHELF +
														 "' and se.location = '" + shelfValue + "'");
						StorageElement shelf = null;
						if (existedSE.size() > 0){
							shelf = (StorageElement)existedSE.get(0);
						} else {
							shelf = new StorageElement();
							shelf.setLocation(shelfValue);
							shelf.setType(CalabConstants.STORAGE_SHELF);
							ida.store(shelf);
						}
						// Create releationship between this source and this sample
						storages.add(shelf);
					}

					String freezerValue = containerBean.getStorageLocation().getFreezer();
					if ((freezerValue != null)&& (freezerValue.length()>0)) {
						List existedSE = ida.search("from StorageElement se where se.type = '" + CalabConstants.STORAGE_FREEZER +
														 "' and se.location = '" + freezerValue + "'");
						StorageElement freezer = null;
						if (existedSE.size() > 0){
							freezer = (StorageElement)existedSE.get(0);
						} else {
							freezer = new StorageElement();
							freezer.setLocation(freezerValue);
							freezer.setType(CalabConstants.STORAGE_FREEZER);
							ida.store(freezer);
						}
						// Create releationship between this source and this sample
						storages.add(freezer);
					}

					String roomValue = containerBean.getStorageLocation().getRoom();
					if ((roomValue != null)&&(roomValue.length()>0)) {
						List existedSE = ida.search("from StorageElement se where se.type = '" + CalabConstants.STORAGE_ROOM +
														 "' and se.location = '" + roomValue + "'");
						StorageElement room = null;
						if (existedSE.size() > 0){
							room = (StorageElement)existedSE.get(0);
						} else {
							room = new StorageElement();
							room.setLocation(roomValue);
							room.setType(CalabConstants.STORAGE_ROOM);
							ida.store(room);
						}
						// Create releationship between this source and this sample
						storages.add(room);
					}
					doAliquot.setStorageElementCollection(storages);
					ida.store(doAliquot);
				}
			}
			ida.close();
		} catch (Exception e ){
			e.printStackTrace();
			ida.rollback();
			ida.close();
			throw new RuntimeException(e.getMessage());
		}
	}
}
