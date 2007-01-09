package gov.nih.nci.calab.dto.inventory;

import gov.nih.nci.calab.service.util.StringUtils;

/**
 * This class captures all properties of a storage location.
 * 
 * @author pansu
 * 
 */
/* CVS $Id: StorageLocation.java,v 1.3 2007-01-09 22:53:58 pansu Exp $ */

public class StorageLocation {
	private String lab = "";

	private String room = "";

	private String otherRoom = "";

	private String freezer = "";

	private String otherFreezer = "";

	private String shelf = "";

	private String otherShelf = "";

	private String rack = "";

	private String box = "";

	private String otherBox = "";

	public StorageLocation() {
	}

	public String getOtherBox() {
		return otherBox;
	}

	public void setOtherBox(String otherBox) {
		this.otherBox = otherBox;
	}

	public String getOtherFreezer() {
		return otherFreezer;
	}

	public void setOtherFreezer(String otherFreezer) {
		this.otherFreezer = otherFreezer;
	}

	public String getOtherRoom() {
		return otherRoom;
	}

	public void setOtherRoom(String otherRoom) {
		this.otherRoom = otherRoom;
	}

	public String getOtherShelf() {
		return otherShelf;
	}

	public void setOtherShelf(String otherShelf) {
		this.otherShelf = otherShelf;
	}

	public StorageLocation(StorageLocation loc) {
		lab = loc.getLab();
		room = loc.getRoom();
		freezer = loc.getFreezer();
		shelf = loc.getShelf();
		rack = loc.getRack();
		box = loc.getBox();
		otherRoom=loc.getOtherRoom();
		otherFreezer=loc.getOtherFreezer();
		otherShelf=loc.getOtherShelf();
		otherBox=loc.getOtherBox();
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

	public StorageLocation(String room, String otherRoom, String freezer,
			String otherFreezer, String shelf, String otherShelf, String box,
			String otherBox) {
		super();
		// TODO Auto-generated constructor stub
		this.room = room;
		this.otherRoom = otherRoom;
		this.freezer = freezer;
		this.otherFreezer = otherFreezer;
		this.shelf = shelf;
		this.otherShelf = otherShelf;
		this.box = box;
		this.otherBox = otherBox;
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
		String freezer = (getFreezer() == null) ? "" : "Freezer " + getFreezer();
		String shelf = (getShelf() == null) ? "" : "Shelf " + getShelf();
		String rack = (getRack() == null) ? "" : "Rack " + getRack();
		String box = (getBox() == null) ? "" : "Box " + getBox();
		String[] strs = new String[] { lab, room, freezer, shelf, rack, box };
		locationStr = StringUtils.join(strs, "-");
		return locationStr;
	}
}
