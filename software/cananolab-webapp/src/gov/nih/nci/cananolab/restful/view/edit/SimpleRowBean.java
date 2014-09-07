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
		this.cells.clear();
		if (cells == null) return;
		
		for (TableCell cell : cells) {
			SimpleCell simpleCell = new SimpleCell();
			simpleCell.transferFromTableCell(cell);
			this.cells.add(simpleCell);
		}
	}
	
	public void transferToRow(Row rowBean) {
		if (this.cells == null) return;
		if (rowBean == null) return;
		
		rowBean.getCells().clear();
		
		for (SimpleCell cell : cells) {
			TableCell cellBean = new TableCell();
			cell.transferToTableCell(cellBean);
			rowBean.getCells().add(cellBean);
		}
	}
}

