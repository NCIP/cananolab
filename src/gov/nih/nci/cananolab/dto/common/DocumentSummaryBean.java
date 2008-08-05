package gov.nih.nci.cananolab.dto.common;

import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * This class represents summary view properties to be shown in
 * document summary view pages.
 * 
 * @author tanq
 * 
 */
public class DocumentSummaryBean {
	private SortedSet<String> columnLabels = new TreeSet<String>();

	private List<LabFileBean> documentRows = new ArrayList<LabFileBean>();


	public DocumentSummaryBean() {
	}

	public SortedSet<String> getColumnLabels() {
		return columnLabels;
	}

	/**
	 * @return the documentRows
	 */
	public List<LabFileBean> getDocumentRows() {
		return documentRows;
	}

	/**
	 * @param documentRows the documentRows to set
	 */
	public void setDocumentRows(List<LabFileBean> documentRows) {
		this.documentRows = documentRows;
	}
}
