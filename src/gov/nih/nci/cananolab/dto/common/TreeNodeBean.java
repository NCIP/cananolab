package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.List;

public class TreeNodeBean implements Comparable<TreeNodeBean> {
	private String nodeName;
	private Integer order;
	private Integer indentLevel; // root node indent level = 0;
	private boolean hasGrandChildrenFlag;
	
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
		return hasGrandChildrenFlag;
	}

	public void setHasGrandChildrenFlag(String childClassName) throws Exception {
		if(childClassName == null) {
			hasGrandChildrenFlag = false;
			return;
		}
		List<String> subclassList = ClassUtils
			.getChildClassNames(childClassName);
		if(subclassList == null || subclassList.size() == 0)
			hasGrandChildrenFlag = false;
		else
			hasGrandChildrenFlag = true;
		
	}
	
}
