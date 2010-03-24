package gov.nih.nci.cananolab.dto.common;

/**
 * DTO object representing a grid node with a host name, a service URL address
 * and a domain model name.
 * 
 * @author pansu
 * 
 */
public class GridNodeBean {
	private String hostName;

	private String address;

	private String domainModelName;

	public enum NodeType {
		REMOTE, LOCAL
	};

	public NodeType nodeType = NodeType.REMOTE;

	public GridNodeBean(String hostName, String address) {

		this.hostName = hostName;
		this.address = address;
	}

	public GridNodeBean(String hostName, String address, String domainModelName) {
		this(hostName, address);
		this.domainModelName = domainModelName;
	}

	public GridNodeBean(String hostName, String address,
			String domainModelName, NodeType nodeType) {
		this(hostName, address, domainModelName);
		this.nodeType = nodeType;
	}

	public String getAddress() {
		return this.address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getDomainModelName() {
		return this.domainModelName;
	}

	public void setDomainModelName(String domainModelName) {
		this.domainModelName = domainModelName;
	}

	public NodeType getNodeType() {
		return nodeType;
	}

	public void setNodeType(NodeType nodeType) {
		this.nodeType = nodeType;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof GridNodeBean) {
			GridNodeBean node = (GridNodeBean) obj;
			String url = node.getAddress();
			String hostname = node.getHostName();
			//matching service URL or host name 
			if (url.equals(this.getAddress())
					|| hostname.equals(this.getHostName())) {
				eq = true;
			}
		}
		return eq;
	}
}
