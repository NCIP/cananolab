package gov.nih.nci.calab.dto.workflow;

import java.util.List;

public class AssayBean {

	private String assayId="";

	private String assayName="";

	private String assayType="";

	private List<RunBean> runBeans;

	private Integer assayNumber;

	private String assayPrefix="";

	public AssayBean() {
		super();
	}

	//used in WorrkflowResultBean
	public AssayBean(String assayName, String assayType) {
		this.assayName = assayName;
		this.assayType = assayType;
	}
	
	public AssayBean(String assayId, String assayName, String assayType) {
		super();
		// TODO Auto-generated constructor stub
		this.assayId = assayId;
		this.assayName = assayName;
		this.assayType = assayType;
	}

	public String getAssayId() {
		return assayId;
	}

	public void setAssayId(String assayId) {
		this.assayId = assayId;
	}

	public String getAssayName() {
		return assayName;
	}

	public void setAssayName(String assayName) {
		this.assayName = assayName;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public String getAssayStr() {
		return this.assayType + " : " + this.assayName;
	}

	// public void setAssayStr(String assayStr) {
	// this.assayStr = assayStr;
	// }
	public List<RunBean> getRunBeans() {
		return runBeans;
	}

	public void setRunBeans(List<RunBean> runBeans) {
		this.runBeans = runBeans;
	}

	/**
	 * Assume assayName has a sequenceNumber at the end
	 * 
	 * @return the sequence number for an assay
	 */
	public Integer getAssayNumber() {
		assayNumber = -1;
		assayPrefix="";
		if (assayName.matches("(\\D+)(\\d+)")) {
			try {
				assayPrefix = assayName.replaceAll("(\\D+)(\\d+)", "$1");
				assayNumber = Integer.parseInt(assayName.replaceAll(
						"(\\D+)(\\d+)", "$2"));
			} catch (Exception e) {
				return -1;
			}
		}
		return assayNumber;
	}
	
	public String getAssayPrefix() {
		if (assayPrefix==null) {
			assayNumber=getAssayNumber();
		}
		return assayPrefix;
	}
}
