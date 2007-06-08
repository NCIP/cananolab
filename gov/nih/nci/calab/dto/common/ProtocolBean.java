/**
 * 
 */
package gov.nih.nci.calab.dto.common;

import gov.nih.nci.calab.domain.Protocol;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zengje
 * 
 */
public class ProtocolBean implements Comparable {
	private String id;

	private String name;

	private String type;

	public ProtocolBean(Protocol protocol) {
		id = protocol.getId().toString();
		name = protocol.getName();
		type = protocol.getType();
	}

	private List<ProtocolFileBean> fileBeanList = new ArrayList<ProtocolFileBean>();

	/**
	 * 
	 */
	public ProtocolBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ProtocolFileBean> getFileBeanList() {
		return fileBeanList;
	}

	public void setFileBeanList(List<ProtocolFileBean> fileBeanList) {
		this.fileBeanList = fileBeanList;
	}

	public int compareTo(Object obj) {
		if (obj instanceof ProtocolBean) {
			ProtocolBean inPb = (ProtocolBean) obj;
			int comparison = this.getName().compareTo(inPb.getName());
			return comparison;
		}
		return -1;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof ProtocolBean) {
			ProtocolBean c = (ProtocolBean) obj;
			String thisId = this.getId();
			// String name = this.getName();
			if (thisId != null && thisId.equals(c.getId())) { // &&
				// name != null && name.equals(c.getName())) {
				eq = true;
			}
		}
		return eq;
	}
}
