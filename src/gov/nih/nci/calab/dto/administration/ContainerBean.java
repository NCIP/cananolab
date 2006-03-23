package gov.nih.nci.calab.dto.administration;

/**
 * This class represents all properties of a container that need to be viewed
 * and saved.
 * 
 * @author pansu
 * 
 */
/* CVS $Id: ContainerBean.java,v 1.2 2006-03-23 19:37:41 pansu Exp $ */
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

	private String storageLab;

	private String storageRoom;

	private String storageRack;

	private String storageFreezer;

	private String storageShelf;

	private String storageBox;

	private String containerComments;

	public ContainerBean() {

	}

	public ContainerBean(String containerType, String otherContainerType,
			String quantity, String quantityUnit, String concentration,
			String concentrationUnit, String volume, String volumeUnit,
			String solvent, String safetyPrecaution, String storageCondition,
			String storageLab, String storageRoom, String storageFreezer,
			String storageShelf, String storageRack, String storageBox,
			String containerComments) {
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
		this.storageLab = storageLab;
		this.storageRoom = storageRoom;
		this.storageFreezer = storageFreezer;
		this.storageShelf = storageShelf;
		this.storageRack=storageRack;
		this.storageBox = storageBox;
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

	public String getStorageBox() {
		return storageBox;
	}

	public void setStorageBox(String storageBox) {
		this.storageBox = storageBox;
	}

	public String getStorageCondition() {
		return storageCondition;
	}

	public void setStorageCondition(String storageCondition) {
		this.storageCondition = storageCondition;
	}

	public String getStorageFreezer() {
		return storageFreezer;
	}

	public void setStorageFreezer(String storageFreezer) {
		this.storageFreezer = storageFreezer;
	}

	public String getStorageRoom() {
		return storageRoom;
	}

	public void setStorageRoom(String storageRoom) {
		this.storageRoom = storageRoom;
	}

	public String getStorageShelf() {
		return storageShelf;
	}

	public void setStorageShelf(String storageShelf) {
		this.storageShelf = storageShelf;
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

	public String getStorageLab() {
		return storageLab;
	}

	public void setStorageLab(String storageLab) {
		this.storageLab = storageLab;
	}

	public String getStorageRack() {
		return storageRack;
	}

	public void setStorageRack(String storageRack) {
		this.storageRack = storageRack;
	}
}
