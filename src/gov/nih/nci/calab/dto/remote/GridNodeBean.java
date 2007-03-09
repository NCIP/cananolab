package gov.nih.nci.calab.dto.remote;

/**
 * DTO object representing a grid node with a host name and a URL address.
 * 
 * @author pansu
 * 
 */
public class GridNodeBean {
	private String hostName;

	private String address;

	private String appServiceURL;

	public GridNodeBean(String hostName, String address) {
		super();
		// TODO Auto-generated constructor stub
		this.hostName = hostName;
		this.address = address;
	}

	public GridNodeBean(String hostName, String address, String appServiceURL) {
		this(hostName, address);
		this.appServiceURL = appServiceURL;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public String getAppServiceURL() {
		return appServiceURL;
	}

	public void setAppServiceURL(String appServiceURL) {
		this.appServiceURL = appServiceURL;
	}

}
