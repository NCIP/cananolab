package gov.nih.nci.cananolab.dto;

/**
 * Contains information about a basic query
 * 
 * @author pansu
 * 
 */
public class BaseQueryBean {
	private String id;
	private String operand;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperand() {
		return operand;
	}

	public void setOperand(String operand) {
		this.operand = operand;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof BaseQueryBean) {
			BaseQueryBean q = (BaseQueryBean) obj;
			String thisId = this.getId();
			if (thisId != null && thisId.equals(q.getId())) {
				eq = true;
			}
		}
		return eq;
	}

	public String getDisplayName() {
		return "";
	}
}
