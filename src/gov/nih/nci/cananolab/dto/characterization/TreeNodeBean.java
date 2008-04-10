package gov.nih.nci.cananolab.dto.characterization;

public class TreeNodeBean implements Comparable<TreeNodeBean> {
	private String nodeName;
	private Integer order;
	private Integer indentLevel; // root node indent level = 0;
	
	public TreeNodeBean(String nodeName, int order, int indentLevel) {
		this.nodeName = nodeName;
		this.order = order;
		this.indentLevel = indentLevel;
	}

	public int getIndentLevel() {
		return indentLevel;
	}

	public void setIndentLevel(int indentLevel) {
		this.indentLevel = indentLevel;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public int compareTo(TreeNodeBean tnb) {
		return order.compareTo(tnb.getOrder());
	}
	
}
