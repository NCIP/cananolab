package gov.nih.nci.calab.service.common;

import gov.nih.nci.calab.db.DataAccessProxy;
import gov.nih.nci.calab.db.IDataAccess;
import gov.nih.nci.calab.domain.Aliquot;
import gov.nih.nci.calab.domain.AssayType;
import gov.nih.nci.calab.domain.MeasureUnit;
import gov.nih.nci.calab.domain.Sample;
import gov.nih.nci.calab.domain.SampleContainer;
import gov.nih.nci.calab.domain.SampleType;
import gov.nih.nci.calab.domain.StorageElement;
import gov.nih.nci.calab.dto.administration.AliquotBean;
import gov.nih.nci.calab.dto.administration.ContainerInfoBean;
import gov.nih.nci.calab.dto.administration.SampleBean;
import gov.nih.nci.calab.service.util.StringUtils;
import gov.nih.nci.calab.dto.workflow.AssayBean;


import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

import org.apache.log4j.Logger;

/**
 * The service to return prepopulated data that are shared across different
 * views.
 * 
 * @author zengje
 * 
 */
/* CVS $Id: LookupService.java,v 1.17 2006-04-14 21:28:28 zengje Exp $ */

public class LookupService {
	private static Logger logger = Logger.getLogger(LookupService.class);

	/**
	 * Retriving all aliquot in the system, for views view aliquot, create run,
	 * search sample.
	 * 
	 * @return a list of AliquotBeans containing aliquot ID and aliquot name
	 */
	public List<AliquotBean> getAliquots() {
		List<AliquotBean> aliquots = new ArrayList<AliquotBean>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select aliquot.id, aliquot.name from Aliquot aliquot order by aliquot.name";
			List results = ida.query(hqlString, Aliquot.class.getName());
			for (Object obj : results) {
				Object[] aliquotInfo = (Object[]) obj;
				aliquots.add(new AliquotBean(StringUtils
						.convertToString(aliquotInfo[0]), StringUtils
						.convertToString(aliquotInfo[1])));
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all aliquot Ids and names", e);
			throw new RuntimeException(
					"Error in retrieving all aliquot Ids and names");
		}
		return aliquots;
	}

	/**
	 * Retrieving all unmasked aliquots for views use aliquot and create
	 * aliquot.
	 * 
	 * @return a list of AliquotBeans containing unmasked aliquot IDs and names.
	 * 
	 */

	public List<AliquotBean> getUnmaskedAliquots() {
		List<AliquotBean> aliquots = new ArrayList<AliquotBean>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select aliquot.id, aliquot.name from Aliquot aliquot where aliquot.dataStatus is null order by aliquot.name";
			List results = ida.query(hqlString, Aliquot.class.getName());
			for (Object obj : results) {
				Object[] aliquotInfo = (Object[]) obj;
				aliquots.add(new AliquotBean(StringUtils
						.convertToString(aliquotInfo[0]), StringUtils
						.convertToString(aliquotInfo[1])));
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all aliquot Ids and names", e);
			throw new RuntimeException(
					"Error in retrieving all aliquot Ids and names");
		}
		return aliquots;
	}

	/**
	 * Retrieving all sample types.
	 * 
	 * @return a list of all sample types
	 */
	public List<String> getAllSampleTypes() {
		// Detail here
		// Retrieve data from Sample_Type table
		List<String> sampleTypes = new ArrayList<String>();

		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select sampleType.name from SampleType sampleType order by sampleType.name";
			List results = ida.query(hqlString, SampleType.class.getName());
			for (Object obj : results) {
				sampleTypes.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all sample types", e);
			throw new RuntimeException("Error in retrieving all sample types");
		}

		return sampleTypes;
	}

	/**
	 * 
	 * @return the default sample container information in a form of
	 *         ContainerInfoBean
	 */
	public ContainerInfoBean getSampleContainerInfo() {
		// tmp code to be replaced
		List<String> containerTypes = getAllContainerTypes();
		List<MeasureUnit> units = getAllMeasureUnits();
		List<StorageElement> storageElements = getAllRoomAndFreezers();
		List<String> quantityUnits = new ArrayList<String>();
		List<String> concentrationUnits = new ArrayList<String>();
		List<String> volumeUnits = new ArrayList<String>();
		List<String> rooms = new ArrayList<String>();
		List<String> freezers = new ArrayList<String>();

		for (MeasureUnit unit : units) {
			if (unit.getType().equalsIgnoreCase("Quantity")) {
				quantityUnits.add(unit.getName());
			} else if (unit.getType().equalsIgnoreCase("Volume")) {
				volumeUnits.add(unit.getName());
			} else if (unit.getType().equalsIgnoreCase("Concentration")) {
				concentrationUnits.add(unit.getName());
			}
		}

		for (StorageElement storageElement : storageElements) {
			if (storageElement.getType().equalsIgnoreCase("Room")) {
				rooms.add((storageElement.getLocation()));
			} else if (storageElement.getType().equalsIgnoreCase("Freezer")) {
				freezers.add((storageElement.getLocation()));
			}
		}

		// set labs and racks to null for now
		ContainerInfoBean containerInfo = new ContainerInfoBean(containerTypes,
				quantityUnits, concentrationUnits, volumeUnits, null, rooms,
				freezers);

		return containerInfo;
	}

	private List<String> getAllContainerTypes() {
		List<String> containerTypes = new ArrayList<String>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select distinct container.containerType from SampleContainer container order by container.containerType";
			List results = ida
					.query(hqlString, SampleContainer.class.getName());
			for (Object obj : results) {
				containerTypes.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all container types", e);
			throw new RuntimeException(
					"Error in retrieving all container types.");
		}
		return containerTypes;
	}

	private List<MeasureUnit> getAllMeasureUnits() {
		List<MeasureUnit> units = new ArrayList<MeasureUnit>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "from MeasureUnit";
			List results = ida.query(hqlString, MeasureUnit.class.getName());
			for (Object obj : results) {
				units.add((MeasureUnit) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all measure units", e);
			throw new RuntimeException("Error in retrieving all measure units.");
		}
		return units;
	}

	private List<StorageElement> getAllRoomAndFreezers() {
		List<StorageElement> storageElements = new ArrayList<StorageElement>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "from StorageElement where type in ('Room', 'Freezer')";
			List results = ida.query(hqlString, StorageElement.class.getName());
			for (Object obj : results) {
				storageElements.add((StorageElement) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all rooms and freezers", e);
			throw new RuntimeException(
					"Error in retrieving all rooms and freezers.");
		}
		return storageElements;
	}

	/**
	 * 
	 * @return the default sample container information in a form of
	 *         ContainerInfoBean
	 */
	public ContainerInfoBean getAliquotContainerInfo() {
		return getSampleContainerInfo();
	}

	/**
	 * Get all samples in the database
	 * 
	 * @return a list of SampleBean containing sample Ids and names
	 */
	public List<SampleBean> getAllSamples() {
		List<SampleBean> samples = new ArrayList<SampleBean>();

		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select sample.id, sample.name from Sample sample order by sample.name";
			List results = ida.query(hqlString, Sample.class.getName());
			for (Object obj : results) {
				Object[] sampleInfo = (Object[]) obj;
				samples.add(new SampleBean(StringUtils
						.convertToString(sampleInfo[0]), StringUtils
						.convertToString(sampleInfo[1])));
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all sample IDs and names", e);
			throw new RuntimeException(
					"Error in retrieving all sample IDs and names");
		}

		return samples;
	}

	/**
	 * Retrieve all Assay Types from the system
	 * 
	 * @return A list of all assay type
	 */
	public List getAllAssayTypes() {
		List<String> assayTypes = new ArrayList<String>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.TOOLKITAPI);
			ida.open();
			String hqlString = "select assayType.name from AssayType assayType order by assayType.executeOrder";
			List results = ida.query(hqlString, AssayType.class.getName());
			for (Object obj : results) {
				assayTypes.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all assay types", e);
			throw new RuntimeException("Error in retrieving all assay types");
		}
		return assayTypes;
	}
	

	/**
	 * Retrieve all assays
	 *
	 * @return a list of all assays in certain type
	 */
	public List<String> getAllAssays() {
		// Detail here
		List<String> assays = new ArrayList<String>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.HIBERNATE);
			ida.open();
			String hqlString = "select assay.name from Assay assay";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				assays.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all assays  ", e);
			throw new RuntimeException("Error in retrieving all assay " );
		}
		return assays;
	}
	/**
	 * Retrieve all assays
	 *
	 * @return a list of all assays in certain type
	 */
	public List<String> getAllAvailableAliquots() {
		// Detail here
		List<String> assays = new ArrayList<String>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.HIBERNATE);
			ida.open();
			String hqlString = "select aliquot.name from Aliquot aliquot order by aliquot.name";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				assays.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all assays  ", e);
			throw new RuntimeException("Error in retrieving all assay " );
		}
		return assays;
	}
	/**
	 * Retrieve all assays
	 *
	 * @return a list of all assays in certain type
	 */
	public List<String> getAllAssignedAliquots() {
		// Detail here
		List<String> aliquots = new ArrayList<String>();
		return aliquots;
	}
	/**
	 * Retrieve all assays
	 *
	 * @return a list of all assays in certain type
	 */
	public List<String> getAllInFiles() {
		// Detail here
		List<String> allInFiles = new ArrayList<String>();
		return allInFiles;
	}
	/**
	 * Retrieve all assays
	 *
	 * @return a list of all assays in certain type
	 */
	public List<String> getAllOutFiles() {
		// Detail here
		List<String> allOutFiles = new ArrayList<String>();
		return allOutFiles;
	}

	
	/**
	 * Retrieve assays by assayType
	 *
	 * @return a list of all assays in certain type
	 */

	public List<AssayBean> getAllAssayBeans()
	{
		List<AssayBean> assayBeans = new ArrayList<AssayBean>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.HIBERNATE);
			ida.open();
			String hqlString = "select assay.id, assay.name, assay.assayType from Assay assay";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				Object[] objArray = (Object[])obj;
				AssayBean assay = new AssayBean(((Long)objArray[0]).toString(), (String)objArray[1], (String)objArray[2]);
				assayBeans.add(assay);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving all assay beans. ", e);
			throw new RuntimeException("Error in retrieving all assays beans. ");
		}
		return assayBeans;
	}
	public List<String> getAssayByType(String assayTypeName) {
		// Detail here
		List<String> assays = new ArrayList<String>();
		try {
			IDataAccess ida = (new DataAccessProxy())
					.getInstance(IDataAccess.HIBERNATE);
			ida.open();
			String hqlString = "select assay.name from Assay assay where assay.assayType ='" + assayTypeName + "'";
			List results = ida.search(hqlString);
			for (Object obj : results) {
				assays.add((String) obj);
			}
			ida.close();
		} catch (Exception e) {
			logger.error("Error in retrieving assay by assayType -- " + assayTypeName, e);
			throw new RuntimeException("Error in retrieving assay by assayType -- " + assayTypeName);
		}

		return assays;
	}
	
	

	/**
	 * Retrieve get Run Names By Assay
	 *
	 * @return a list of all assays in certain type
	 */
	public List<String> getRunByAssay(String assayTypeName) {
		// Detail here
		List<String> runs = new ArrayList<String>();
		runs.add("Run1");
		runs.add("Run2");
		return runs;
	}

	/**
	 * Retrieve get FolderNames 
	 *@param runName
	 * @return a list of all assays in certain type
	 */
	public List<String> getFolderNames(String runName) {
		// Detail here
		List<String> folderNames = new ArrayList<String>();
		folderNames.add("In");
		folderNames.add("Out");
		return folderNames;
	}
	
	/**
	 * Retrieve get FileNames 
	 *@param runName
	 * @return a list of all assays in certain type
	 */
	
	public List<String> getFileNames(String folderName) {
		// Detail here
		List<String> fileNames = new ArrayList<String>();
		fileNames.add("NCL6.vaf");
		fileNames.add("NCL14.vaf");
		return fileNames;
	}	
	
	/**
	 * Builds the Workflow in a dTree javaScript Tree view(Refer script.js)
	
	
	s	d = new dTree('d');	d.config.target="";	d.add(0,-1,'Workflow');
		
	s	d.add(1,0,'Pre-screening Assays','javascript:void(0)', '', '', '');
		
	s	d.add(2,1,'STE-1','javascript:gotoPage(\'assayActionMenu.jsp\')', '', '', '');
		
	s	d.add(3,2,'run 1','javascript:gotoPage(\'editRun.jsp\')', '', '', '');
		
	s	d.add(4,3,'In','javascript:gotoPage(\'inputActionMenu.jsp\')');
		
	s	d.add(5,4,'NCL6-7105-1','javascript:void(0)','','','');
		
		d.add(6,4,'NCL6-7105-2','javascript:void(0)','','','');
		
		d.add(7,3,'Out','javascript:gotoPage(\'outputActionMenu.jsp\')');
		
		d.add(8,7,'NCL6.vaf','doc/astra_5.doc');
		
		d.add(9,2,'run 2','javascript:void(0)');
		
		d.add(10,9,'In','javascript:gotoPage(\'inputActionMenu.jsp\')');
		
		d.add(11,9,'Out','javascript:gotoPage(\'outputActionMenu.jsp\')');
		d.add(12,1,'STE-2','javascript:gotoPage(\'assayActionMenu.jsp\')');
		d.add(13,1,'STE-3','javascript:gotoPage(\'assayActionMenu.jsp\')', '', '', '');
		d.add(14,1,'PCC-1','javascript:gotoPage(\'assayActionMenu.jsp\')', '', '', '');
		d.add(15,0,'In Vitro Assays','javascript:void(0)', '', '', '');
		d.add(16,0,'In Vivo Assays','javascript:void(0)', '', '', '');
		
		document.write(d);
		
	 * 
	 * 
	 * 
	 */
	
	public String getWorkflowTree() 
	{
		
		List<String> assayTypes = getAllAssayTypes();
		Iterator<String> atit = assayTypes.iterator();
		int ia=0, ib=-1;
		String treeStr = "d = new dTree('d');	d.config.target=\"\";	d.add(0,-1,'Workflow');";
		
	    while (atit.hasNext()) 
	    {
	      String assayTypeName = atit.next();
	      //Add AssyType node       1          0         Pre  
	      ia=ia++;
	      ib=ib++;
	      treeStr=treeStr+" d.add("+ia+" ,"+ib+",'"+assayTypeName+"','javascript:void(0)', '', '', '');";
	      
	      //    Add Assy node
		  List<String> assayNames = getAssayByType(assayTypeName);
		  Iterator<String> asit = assayTypes.iterator();
		  while(asit.hasNext())
		  {
			  ia=ia++;
			  ib=ia;
		      String assayName = asit.next();
			  //  						2		1       							1	
		      //					d.add(2,1,'STE-1','javascript:gotoPage(\'assayActionMenu.jsp\')', '', '', '');
		      treeStr=treeStr+" d.add("+ia+" ,"+ib+",'"+assayName+",'javascript:gotoPage(\'assayActionMenu.jsp\')', '', '', '');";
		      		//    Add Run nodes
			  		List<String> runNames = getRunByAssay(assayName);
			  		Iterator<String> rit = runNames.iterator();
			  		while(rit.hasNext())
			  		{
			  			ia=ia++;
			  			ib=ia;
			  			String runName = rit.next();
			  			//  						2		1       							1	
			  			//						d.add(3,2,'run 1','javascript:gotoPage(\'editRun.jsp\')', '', '', '');
			  			treeStr=treeStr+" d.add("+ia+" ,"+ib+",'"+assayName+",'javascript:gotoPage(\'editRun.jsp\')', '', '', '');";
			  			
			  			//		   Add Folder nodes
				  		List<String> folderNames = getFolderNames(runName);
				  		Iterator<String> folderit = runNames.iterator();
				  		while(folderit.hasNext())
				  		{
				  			ia=ia++;
				  			ib=ia;
				  			String folderName = folderit.next();
				  			//  						2		1       							1	
				  			//						d.add(4,3,'In','javascript:gotoPage(\'inputActionMenu.jsp\')');
				  			treeStr=treeStr+" d.add("+ia+" ,"+ib+",'"+folderName+",'javascript:gotoPage(\'inputActionMenu.jsp.jsp\')');";
				  			
				  			//	 		   Add Folder nodes
					  		List<String> fileNames = getFileNames(folderName);
					  		Iterator<String> fileit = fileNames.iterator();
					  		while(fileit.hasNext())
					  		{
					  			ia=ia++;
					  			ib=ia;
					  			String fileName = fileit.next();
					  			//  						2		1       							1	
					  			//						d.add(6,4,'NCL6-7105-2','javascript:void(0)','','','');
					  			treeStr=treeStr+" d.add("+ia+" ,"+ib+",'"+fileName+",'javascript:void(0)','','','');";
					  		}//file
				  		}//folder
			  		}//Run
		  		}//Assay
	    	}//AssayType

	      System.out.println(treeStr);
	      return treeStr;
	}	

	
}
