package gov.nih.nci.calab.domain;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * This is Sample Container. And it contains the information for the sample
 * contained in the container.
 * 
 */

public class SampleContainer implements java.io.Serializable {
	private static final long serialVersionUID = 1234567890L;

	private java.lang.Long id;

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	private java.lang.Float quantity;

	public java.lang.Float getQuantity() {
		return this.quantity;
	}

	public void setQuantity(java.lang.Float quantity) {
		this.quantity = quantity;
	}

	private java.lang.Float concentration;

	public java.lang.Float getConcentration() {
		return this.concentration;
	}

	public void setConcentration(java.lang.Float concentration) {
		this.concentration = concentration;
	}

	private java.lang.Float volume;

	public java.lang.Float getVolume() {
		return this.volume;
	}

	public void setVolume(java.lang.Float volume) {
		this.volume = volume;
	}

	private java.lang.String diluentsSolvent;

	public java.lang.String getDiluentsSolvent() {
		return this.diluentsSolvent;
	}

	public void setDiluentsSolvent(java.lang.String diluentsSolvent) {
		this.diluentsSolvent = diluentsSolvent;
	}

	private java.lang.String safetyPrecaution;

	public java.lang.String getSafetyPrecaution() {
		return this.safetyPrecaution;
	}

	public void setSafetyPrecaution(java.lang.String safetyPrecaution) {
		this.safetyPrecaution = safetyPrecaution;
	}

	private java.lang.String storageCondition;

	public java.lang.String getStorageCondition() {
		return this.storageCondition;
	}

	public void setStorageCondition(java.lang.String storageCondition) {
		this.storageCondition = storageCondition;
	}

	private java.lang.String comments;

	public java.lang.String getComments() {
		return this.comments;
	}

	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	private java.lang.String quantityUnit;

	public java.lang.String getQuantityUnit() {
		return this.quantityUnit;
	}

	public void setQuantityUnit(java.lang.String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}

	private java.lang.String concentrationUnit;

	public java.lang.String getConcentrationUnit() {
		return this.concentrationUnit;
	}

	public void setConcentrationUnit(java.lang.String concentrationUnit) {
		this.concentrationUnit = concentrationUnit;
	}

	private java.lang.String volumeUnit;

	public java.lang.String getVolumeUnit() {
		return this.volumeUnit;
	}

	public void setVolumeUnit(java.lang.String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	private java.lang.String createdBy;

	public java.lang.String getCreatedBy() {
		return this.createdBy;
	}

	public void setCreatedBy(java.lang.String createdBy) {
		this.createdBy = createdBy;
	}

	private java.util.Date createdDate;

	public java.util.Date getCreatedDate() {
		return this.createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	private java.lang.String barcode;

	public java.lang.String getBarcode() {
		return this.barcode;
	}

	public void setBarcode(java.lang.String barcode) {
		this.barcode = barcode;
	}

	private java.lang.String containerType;

	public java.lang.String getContainerType() {
		return this.containerType;
	}

	public void setContainerType(java.lang.String containerType) {
		this.containerType = containerType;
	}

	private java.lang.String createdMethod;

	public java.lang.String getCreatedMethod() {
		return this.createdMethod;
	}

	public void setCreatedMethod(java.lang.String createdMethod) {
		this.createdMethod = createdMethod;
	}

	private java.lang.String name;

	public java.lang.String getName() {
		return this.name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	private java.util.Collection storageElementCollection;

	public java.util.Collection getStorageElementCollection() {
		// try{
		// if(storageElementCollection.size() == 0) {}
		// } catch(Exception e) {
		// ApplicationService applicationService =
		// ApplicationServiceProvider.getApplicationService();
		// try {
		// gov.nih.nci.calab.domain.SampleContainer thisIdSet = new
		// gov.nih.nci.calab.domain.SampleContainer();
		// thisIdSet.setId(this.getId());
		// java.util.Collection resultList =
		// applicationService.search("gov.nih.nci.calab.domain.StorageElement",
		// thisIdSet);
		// storageElementCollection = resultList;
		// return resultList;
		//				 
		//			      
		// }catch(Exception ex)
		// {
		// System.out.println("SampleContainer:getStorageElementCollection
		// throws exception ... ...");
		// ex.printStackTrace();
		// }
		// }
		return this.storageElementCollection;

	}

	public void setStorageElementCollection(
			java.util.Collection storageElementCollection) {
		this.storageElementCollection = storageElementCollection;
	}

	private java.util.Collection parentSampleContainerCollection = new java.util.HashSet();

	public java.util.Collection getParentSampleContainerCollection() {
		// try{
		// if(parentSampleContainerCollection.size() == 0) {}
		// } catch(Exception e) {
		// ApplicationService applicationService =
		// ApplicationServiceProvider.getApplicationService();
		// try {
		//		      
		//		      
		//		         
		// gov.nih.nci.calab.domain.SampleContainer thisIdSet = new
		// gov.nih.nci.calab.domain.SampleContainer();
		// thisIdSet.setId(this.getId());
		// java.util.Collection resultList =
		// applicationService.search("gov.nih.nci.calab.domain.SampleContainer",
		// thisIdSet);
		// parentSampleContainerCollection = resultList;
		// return resultList;
		//			 
		//		      
		// }catch(Exception ex)
		// {
		// System.out.println("SampleContainer:getParentSampleContainerCollection
		// throws exception ... ...");
		// ex.printStackTrace();
		// }
		// }
		return this.parentSampleContainerCollection;
	}

	public void setParentSampleContainerCollection(
			java.util.Collection parentSampleContainerCollection) {
		this.parentSampleContainerCollection = parentSampleContainerCollection;
	}

	private java.util.Collection childSampleContainerCollection = new java.util.HashSet();

	public java.util.Collection getChildSampleContainerCollection() {
		// try{
		// if(childSampleContainerCollection.size() == 0) {}
		// } catch(Exception e) {
		// ApplicationService applicationService =
		// ApplicationServiceProvider.getApplicationService();
		// try {
		//		      
		//		      
		//		         
		// gov.nih.nci.calab.domain.SampleContainer thisIdSet = new
		// gov.nih.nci.calab.domain.SampleContainer();
		// thisIdSet.setId(this.getId());
		// java.util.Collection resultList =
		// applicationService.search("gov.nih.nci.calab.domain.SampleContainer",
		// thisIdSet);
		// childSampleContainerCollection = resultList;
		// return resultList;
		//			 
		//		      
		// }catch(Exception ex)
		// {
		// System.out.println("SampleContainer:getChildSampleContainerCollection
		// throws exception ... ...");
		// ex.printStackTrace();
		// }
		// }
		return this.childSampleContainerCollection;
	}

	public void setChildSampleContainerCollection(
			java.util.Collection childSampleContainerCollection) {
		this.childSampleContainerCollection = childSampleContainerCollection;
	}

	private java.util.Collection runSampleContainerCollection = new java.util.HashSet();

	public java.util.Collection getRunSampleContainerCollection() {
		// try{
		// if(runSampleContainerCollection.size() == 0) {}
		// } catch(Exception e) {
		// ApplicationService applicationService =
		// ApplicationServiceProvider.getApplicationService();
		// try {
		//		      
		//		      
		//		         
		// gov.nih.nci.calab.domain.SampleContainer thisIdSet = new
		// gov.nih.nci.calab.domain.SampleContainer();
		// thisIdSet.setId(this.getId());
		// java.util.Collection resultList =
		// applicationService.search("gov.nih.nci.calab.domain.RunSampleContainer",
		// thisIdSet);
		// runSampleContainerCollection = resultList;
		// return resultList;
		//			 
		//		      
		// }catch(Exception ex)
		// {
		// System.out.println("SampleContainer:getRunSampleContainerCollection
		// throws exception ... ...");
		// ex.printStackTrace();
		// }
		// }
		return this.runSampleContainerCollection;
	}

	public void setRunSampleContainerCollection(
			java.util.Collection runSampleContainerCollection) {
		this.runSampleContainerCollection = runSampleContainerCollection;
	}

	private gov.nih.nci.calab.domain.Sample sample;

	public gov.nih.nci.calab.domain.Sample getSample() {

		// ApplicationService applicationService =
		// ApplicationServiceProvider.getApplicationService();
		// gov.nih.nci.calab.domain.SampleContainer thisIdSet = new
		// gov.nih.nci.calab.domain.SampleContainer();
		// thisIdSet.setId(this.getId());
		//		  
		// try {
		// java.util.List resultList =
		// applicationService.search("gov.nih.nci.calab.domain.Sample",
		// thisIdSet);
		// if (resultList!=null && resultList.size()>0) {
		// sample = (gov.nih.nci.calab.domain.Sample)resultList.get(0);
		// }
		//	          
		// } catch(Exception ex)
		// {
		// System.out.println("SampleContainer:getSample throws exception ...
		// ...");
		// ex.printStackTrace();
		// }
		return this.sample;
	}

	public void setSample(gov.nih.nci.calab.domain.Sample sample) {
		this.sample = sample;
	}

	private gov.nih.nci.calab.domain.DataStatus dataStatus;

	public gov.nih.nci.calab.domain.DataStatus getDataStatus() {
		return this.dataStatus;
	}

	public void setDataStatus(DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof SampleContainer) {
			SampleContainer c = (SampleContainer) obj;
			Long thisId = getId();

			if (thisId != null && thisId.equals(c.getId())) {
				eq = true;
			}

		}
		return eq;
	}

	public int hashCode() {
		int h = 0;

		if (getId() != null) {
			h += getId().hashCode();
		}

		return h;
	}

}