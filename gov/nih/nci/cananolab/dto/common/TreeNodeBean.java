package gov.nih.nci.cananolab.dto.common;

public class TreeNodeBean implements Comparable<TreeNodeBean> {
	private String nodeName;
	private Integer order;
	private Integer indentLevel; // root node indent level = 0;
	private boolean hasGrandChildernFlag;
	
	public TreeNodeBean(String nodeName, int order, int indentLevel) {
		this.nodeName = nodeName;
		this.order = order;
		this.indentLevel = indentLevel;
	}

	public int getIndentLevel() {
		return indentLevel;
	}

	public String getNodeName() {
		return nodeName;
	}

	public int getOrder() {
		return order;
	}

	public int compareTo(TreeNodeBean tnb) {
		return order.compareTo(tnb.getOrder());
	}

	public boolean hasGrandChildern() {
		return hasGrandChildernFlag;
	}

	public boolean isHasGrandChildernFlag() {
		return hasGrandChildernFlag;
	}

	public void setHasGrandChildernFlag(boolean hasGrandChildernFlag) {
		this.hasGrandChildernFlag = hasGrandChildernFlag;
	}

	public void setIndentLevel(Integer indentLevel) {
		this.indentLevel = indentLevel;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public void setOrder(Integer order) {
		this.order = order;
	}
}
