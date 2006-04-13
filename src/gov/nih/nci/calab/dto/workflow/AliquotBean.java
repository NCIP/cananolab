/**
 * 
 */
package gov.nih.nci.calab.dto.workflow;

/**
 * @author zengje
 *
 */
public class AliquotBean {
	
	private String id;
	private String name;
	/**
	 * 
	 */
	public AliquotBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public AliquotBean(String id, String name) {
		super();
		// TODO Auto-generated constructor stub
		this.id = id;
		this.name = name;
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

}
