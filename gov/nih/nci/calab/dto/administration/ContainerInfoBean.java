package gov.nih.nci.calab.dto.administration;

import java.util.List;

/**
 * This class captures the database pre-loaded information about containers and
 * is used to prepopulate the Create Sample and Create Aliquot pages.
 * 
 * @author pansu
 * 
 */

public class ContainerInfoBean {

	private List<String> containerTypes;

	private List<String> quantityUnits;

	private List<String> concentrationUnits;

	private List<String> volumeUnits;

	private List<String> storeageRooms;

	private List<String> storageFreezers;

	private List<String> howCreated;

	public ContainerInfoBean(List<String> containerTypes,
			List<String> quantityUnits, List<String> concentrationUnits,
			List<String> volumeUnits, List<String> rooms, List<String> freezers) {
		this.containerTypes = containerTypes;
		this.quantityUnits = quantityUnits;
		this.concentrationUnits = concentrationUnits;
		this.volumeUnits = volumeUnits;
		this.storeageRooms = rooms;
		this.storageFreezers = freezers;
	}

	public ContainerInfoBean(List<String> containerTypes,
			List<String> quantityUnits, List<String> concentrationUnits,
			List<String> volumnUnits, List<String> rooms,
			List<String> freezers, List<String> howCreated) {
		this(containerTypes, quantityUnits, concentrationUnits, volumnUnits,
				rooms, freezers);
		this.howCreated = howCreated;
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

	public List<String> getHowCreated() {
		return howCreated;
	}

	public void setHowCreated(List<String> howCreated) {
		this.howCreated = howCreated;
	}

}
