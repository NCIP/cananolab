package gov.nih.nci.calab.dto.administration;

import java.util.List;

/**
 * This class captures the database pre-loaded information about containers and
 * is used to prepopulate the Create Sample and Create Aliquot pages.
 * 
 * @author pansu
 * 
 */
/* CVS $Id: ContainerInfoBean.java,v 1.4 2006-03-28 22:59:07 pansu Exp $ */

public class ContainerInfoBean {

	private List<String> containerTypes;

	private List<String> quantityUnits;

	private List<String> concentrationUnits;

	private List<String> volumeUnits;

	private List<String> storageLabs;

	private List<String> storeageRooms;

	private List<String> storageFreezers;

	public ContainerInfoBean(List<String> containerTypes,
			List<String> quantityUnits, List<String> concentrationUnits,
			List<String> volumeUnits, List<String> storageLabs,
			List<String> storeageRooms, 
			List<String> storageFreezers) {
		super();
		// TODO Auto-generated constructor stub
		this.containerTypes = containerTypes;
		this.quantityUnits = quantityUnits;
		this.concentrationUnits = concentrationUnits;
		this.volumeUnits = volumeUnits;
		this.storageLabs = storageLabs;
		this.storeageRooms = storeageRooms;
		this.storageFreezers = storageFreezers;
	}

	public List<String> getConcentrationUnits() {
		return concentrationUnits;
	}

	public void setConcentrationUnits(List<String> concentrationUnits) {
		this.concentrationUnits = concentrationUnits;
	}

	public List<String> getContainerTypes() {
		return containerTypes;
	}

	public void setContainerTypes(List<String> containerTypes) {
		this.containerTypes = containerTypes;
	}

	public List<String> getStorageFreezers() {
		return storageFreezers;
	}

	public void setStorageFreezers(List<String> freezers) {
		this.storageFreezers = freezers;
	}

	public List<String> getQuantityUnits() {
		return quantityUnits;
	}

	public void setQuantityUnits(List<String> quantityUnits) {
		this.quantityUnits = quantityUnits;
	}

	public List<String> getStorageRooms() {
		return storeageRooms;
	}

	public void setStorageRooms(List<String> rooms) {
		this.storeageRooms = rooms;
	}

	public List<String> getVolumeUnits() {
		return volumeUnits;
	}

	public void setVolumeUnits(List<String> volumeUnits) {
		this.volumeUnits = volumeUnits;
	}

	public List<String> getStorageLabs() {
		return storageLabs;
	}

	public void setStorageLabs(List<String> storageLabs) {
		this.storageLabs = storageLabs;
	}


	public List<String> getStoreageRooms() {
		return storeageRooms;
	}

	public void setStoreageRooms(List<String> storeageRooms) {
		this.storeageRooms = storeageRooms;
	}

}
