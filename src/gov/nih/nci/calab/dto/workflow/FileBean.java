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
	private String filename;
	
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
		this.filename = parsePath(path);
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

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}
	
	private String parsePath(String path)
	{
		if (path == null) {
			return null;
		} else {
			if ((path.indexOf("/") < 0) && (path.indexOf("\\") <0)) {
				return path;
			} else if (path.indexOf("/") > 0) {
				return path.substring(path.lastIndexOf("/")+1);
			} else if (path.indexOf("\\") > 0) {
				return path.substring(path.lastIndexOf("\\") + 1);
			} else {
				return path;
			}
		}
	
		
	}

}
