package gov.nih.nci.calab.dto.inventory;

import gov.nih.nci.calab.service.util.StringUtils;

/**
 * This class captures all properties of a storage location.
 * 
 * @author pansu
 * 
 */
/* CVS $Id: StorageLocation.java,v 1.3.2.1 2007-06-29 14:55:33 pansu Exp $ */

public class StorageLocation {
	private String lab = "";

	private String room = "";

	private String freezer = "";

	private String shelf = "";

	private String rack = "";

	private String box = "";

	public StorageLocation() {
	}

	public StorageLocation(StorageLocation loc) {
		lab = loc.getLab();
		room = loc.getRoom();
		freezer = loc.getFreezer();
		shelf = loc.getShelf();
		rack = loc.getRack();
		box = loc.getBox();
	}

	public StorageLocation(String lab, String room, String freezer,
			String shelf, String rack, String box) {
		super();
		// TODO Auto-generated constructor stub
		this.lab = lab;
		this.room = room;
		this.freezer = freezer;
		this.shelf = shelf;
		this.rack = rack;
		this.box = box;
	}

	public StorageLocation(String room, String freezer, String shelf, String box) {
		super();
		// TODO Auto-generated constructor stub
		this.room = room;
		this.freezer = freezer;
		this.shelf = shelf;
		this.box = box;
	}

	public String getBox() {
		return box;
	}

	public void setBox(String box) {
		this.box = box;
	}

	public String getFreezer() {
		return freezer;
	}

	public void setFreezer(String freezer) {
		this.freezer = freezer;
	}

	public String getLab() {
		return lab;
	}

	public void setLab(String lab) {
		this.lab = lab;
	}

	public String getRack() {
		return rack;
	}

	public void setRack(String rack) {
		this.rack = rack;
	}

	public String getRoom() {
		return room;
	}

	public void setRoom(String room) {
		this.room = room;
	}

	public String getShelf() {
		return shelf;
	}

	public void setShelf(String shelf) {
		this.shelf = shelf;
	}

	public String toString() {
		String locationStr = "";
		String lab = (getLab() == null) ? "" : "Lab " + getLab();
		String room = (getRoom() == null) ? "" : "Room " + getRoom();
		String freezer = (getFreezer() == null) ? "" : "Freezer "
				+ getFreezer();
		String shelf = (getShelf() == null) ? "" : "Shelf " + getShelf();
		String rack = (getRack() == null) ? "" : "Rack " + getRack();
		String box = (getBox() == null) ? "" : "Box " + getBox();
		String[] strs = new String[] { lab, room, freezer, shelf, rack, box };
		locationStr = StringUtils.join(strs, "-");
		return locationStr;
	}
}
