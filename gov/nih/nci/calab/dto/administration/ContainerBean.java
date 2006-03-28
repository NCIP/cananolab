package gov.nih.nci.calab.dto.administration;

import gov.nih.nci.calab.service.util.StringUtils;

/**
 * This class represents all properties of a container that need to be viewed
 * and saved.
 * 
 * @author pansu
 * 
 */
/* CVS $Id: ContainerBean.java,v 1.3 2006-03-28 22:58:35 pansu Exp $ */

public class ContainerBean {
	private String containerType;

	private String otherContainerType;

	private String quantity;

	private String quantityUnit;

	private String concentration;

	private String concentrationUnit;

	private String volume;

	private String volumeUnit;

	private String solvent;

	private String safetyPrecaution;

	private String storageCondition;

	private StorageLocation storageLocation;

	private String storageLocationStr;

	private String containerComments;

	public ContainerBean() {
		storageLocation=new StorageLocation();
	}

	public ContainerBean(String containerType, String otherContainerType,
			String quantity, String quantityUnit, String concentration,
			String concentrationUnit, String volume, String volumeUnit,
			String solvent, String safetyPrecaution, String storageCondition,
			StorageLocation storageLocation, String containerComments) {
		super();
		// TODO Auto-generated constructor stub
		this.containerType = containerType;
		this.otherContainerType = otherContainerType;
		this.quantity = quantity;
		this.quantityUnit = quantityUnit;
		this.concentration = concentration;
		this.concentrationUnit = concentrationUnit;
		this.volume = volume;
		this.volumeUnit = volumeUnit;
		this.solvent = solvent;
		this.safetyPrecaution = safetyPrecaution;
		this.storageCondition = storageCondition;
		this.storageLocation = storageLocation;
		this.containerComments = containerComments;
	}

	public String getConcentration() {
		return concentration;
	}

	public void setConcentration(String concentration) {
		this.concentration = concentration;
	}

	public String getConcentrationUnit() {
		return concentrationUnit;
	}

	public void setConcentrationUnit(String concentrationUnit) {
		this.concentrationUnit = concentrationUnit;
	}

	public String getContainerComments() {
		return containerComments;
	}

	public void setContainerComments(String containerComments) {
		this.containerComments = containerComments;
	}

	public String getContainerType() {
		return containerType;
	}

	public void setContainerType(String containerType) {
		this.containerType = containerType;
	}

	public String getOtherContainerType() {
		return otherContainerType;
	}

	public void setOtherContainerType(String otherContainerType) {
		this.otherContainerType = otherContainerType;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	public String getQuantityUnit() {
		return quantityUnit;
	}

	public void setQuantityUnit(String quantityUnit) {
		this.quantityUnit = quantityUnit;
	}

	public String getSafetyPrecaution() {
		return safetyPrecaution;
	}

	public void setSafetyPrecaution(String safetyPrecaution) {
		this.safetyPrecaution = safetyPrecaution;
	}

	public String getSolvent() {
		return solvent;
	}

	public void setSolvent(String solvent) {
		this.solvent = solvent;
	}

	public String getStorageCondition() {
		return storageCondition;
	}

	public void setStorageCondition(String storageCondition) {
		this.storageCondition = storageCondition;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getVolumeUnit() {
		return volumeUnit;
	}

	public void setVolumeUnit(String volumeUnit) {
		this.volumeUnit = volumeUnit;
	}

	public StorageLocation getStorageLocation() {
		return storageLocation;
	}

	public void setStorageLocation(StorageLocation storageLocation) {
		this.storageLocation = storageLocation;
	}

	public String getStorageLocationStr() {
		String lab = (storageLocation.getLab() == null) ? "" : "Lab"
				+ storageLocation.getLab();
		String room = (storageLocation.getRoom() == null) ? "" : "Room"
				+ storageLocation.getRoom();
		String freezer = (storageLocation.getFreezer() == null) ? ""
				: "Freezer" + storageLocation.getFreezer();
		String shelf = (storageLocation.getShelf() == null) ? "" : "Shelf"
				+ storageLocation.getShelf();
		String rack = (storageLocation.getRack() == null) ? "" : "Rack"
				+ storageLocation.getRack();
		String box = (storageLocation.getBox() == null) ? "" : "Box"
				+ storageLocation.getBox();
		String[] strs = new String[] { lab, room, freezer, shelf, rack, box };
		storageLocationStr = StringUtils.join(strs, "-");
		return storageLocationStr;
	}

	public void setStorageLocationStr(String storageLocationStr) {
		this.storageLocationStr = storageLocationStr;
	}
}
