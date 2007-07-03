package gov.nih.nci.calab.dto.inventory;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class captures the database pre-loaded information about containers and
 * is used to prepopulate the Create Sample and Create Aliquot pages.
 * 
 * @author pansu
 * 
 */
/* CVS $Id: ContainerInfoBean.java,v 1.3.2.1 2007-07-03 19:41:49 pansu Exp $ */

public class ContainerInfoBean {

	private SortedSet<String> quantityUnits=new TreeSet<String>();

	private SortedSet<String> concentrationUnits=new TreeSet<String>();

	private SortedSet<String> volumeUnits=new TreeSet<String>();

	private SortedSet<String> storageLabs=new TreeSet<String>();

	private SortedSet<String> storageRooms=new TreeSet<String>();

	private SortedSet<String> storageFreezers=new TreeSet<String>();

	private SortedSet<String> storageShelves=new TreeSet<String>();

	private SortedSet<String> storageBoxes=new TreeSet<String>();

	public SortedSet<String> getStorageBoxes() {
		return storageBoxes;
	}

	public void setStorageBoxes(SortedSet<String> storageBoxes) {
		this.storageBoxes = storageBoxes;
	}

	public SortedSet<String> getStorageShelves() {
		return storageShelves;
	}

	public void setStorageShelves(SortedSet<String> storageShelves) {
		this.storageShelves = storageShelves;
	}

	public ContainerInfoBean(SortedSet<String> quantityUnits,
			SortedSet<String> concentrationUnits, SortedSet<String> volumeUnits,
			SortedSet<String> storageLabs, SortedSet<String> storeageRooms,
			SortedSet<String> storageFreezers, SortedSet<String> storageShelves,
			SortedSet<String> storageBoxes) {
		super();
		// TODO Auto-generated constructor stub
		this.quantityUnits = quantityUnits;
		this.concentrationUnits = concentrationUnits;
		this.volumeUnits = volumeUnits;
		this.storageLabs = storageLabs;
		this.storageRooms = storeageRooms;
		this.storageFreezers = storageFreezers;
		this.storageShelves = storageShelves;
		this.storageBoxes = storageBoxes;
	}

	public SortedSet<String> getConcentrationUnits() {
		return concentrationUnits;
	}

	public void setConcentrationUnits(SortedSet<String> concentrationUnits) {
		this.concentrationUnits = concentrationUnits;
	}

	public SortedSet<String> getStorageFreezers() {
		return storageFreezers;
	}

	public void setStorageFreezers(SortedSet<String> freezers) {
		this.storageFreezers = freezers;
	}

	public SortedSet<String> getQuantityUnits() {
		return quantityUnits;
	}

	public void setQuantityUnits(SortedSet<String> quantityUnits) {
		this.quantityUnits = quantityUnits;
	}

	public SortedSet<String> getStorageRooms() {
		return storageRooms;
	}

	public void setStorageRooms(SortedSet<String> rooms) {
		this.storageRooms = rooms;
	}

	public SortedSet<String> getVolumeUnits() {
		return volumeUnits;
	}

	public void setVolumeUnits(SortedSet<String> volumeUnits) {
		this.volumeUnits = volumeUnits;
	}

	public SortedSet<String> getStorageLabs() {
		return storageLabs;
	}

	public void setStorageLabs(SortedSet<String> storageLabs) {
		this.storageLabs = storageLabs;
	}

	public SortedSet<String> getStoreageRooms() {
		return storageRooms;
	}

	public void setStoreageRooms(SortedSet<String> storeageRooms) {
		this.storageRooms = storeageRooms;
	}
}

