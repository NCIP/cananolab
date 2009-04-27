package gov.nih.nci.cananolab.dto.common;

import java.util.ArrayList;
import java.util.List;

/**
 * View bean for a row in a table
 *
 * @author pansu
 *
 */
public class Row {
	private List<TableCell> cells = new ArrayList<TableCell>();

	public Row() {
	}

	public List<TableCell> getCells() {
		return cells;
	}

	public void setCells(List<TableCell> cells) {
		this.cells = cells;
	}
}
