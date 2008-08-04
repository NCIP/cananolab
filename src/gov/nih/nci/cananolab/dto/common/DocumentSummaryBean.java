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

	private List<LabFileBean> documentBeans = new ArrayList<LabFileBean>();

	private String characterizationClassName;

	public DocumentSummaryBean() {
	}

	public DocumentSummaryBean(String characterizationClassName) {
		this.characterizationClassName = characterizationClassName;
	}

	public String getCharacterizationClassName() {
		return characterizationClassName;
	}

	public SortedSet<String> getColumnLabels() {
		return columnLabels;
	}

	public List<LabFileBean> getDocumentBeans() {
		return documentBeans;
	}
}
