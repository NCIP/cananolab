package gov.nih.nci.cananolab.restful.view.edit;

import gov.nih.nci.cananolab.dto.common.Row;
import gov.nih.nci.cananolab.dto.common.TableCell;

import java.util.ArrayList;
import java.util.List;

public class SimpleRowBean {
	List<SimpleCell> cells = new ArrayList<SimpleCell>();

	public List<SimpleCell> getCells() {
		return cells;
	}

	public void setCells(List<SimpleCell> cells) {
		this.cells = cells;
	}
	
	public void transferFromRow(Row beanRow) {
		if (beanRow == null) return;
		
		List<TableCell> cells = beanRow.getCells();
		if (cells == null) return;
		
		for (TableCell cell : cells) {
			SimpleCell simpleCell = new SimpleCell();
			simpleCell.transferFromTableCell(cell);
			this.cells.add(simpleCell);
		}
	}
}

