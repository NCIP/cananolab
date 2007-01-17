/**
 * 
 */
package gov.nih.nci.calab.domain;

/**
 * @author zengje
 * 
 */
public class Keyword {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String name;

	/**
	 * 
	 */
	public Keyword() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
