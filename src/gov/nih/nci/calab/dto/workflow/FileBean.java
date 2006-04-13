/**
 * 
 */
package gov.nih.nci.calab.dto.workflow;

/**
 * @author zengje
 *
 */
public class FileBean {

	private String id;
	private String path;
	
	/**
	 * 
	 */
	public FileBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public FileBean(String id, String path) {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
		this.path = path;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

}
