/**
 * 
 */
package gov.nih.nci.calab.dto.characterization;

/**
 * @author zengje
 *
 */
public class CharacterizationProtocolBean {
	private Long id;
	private String name;
	private String version;
	/**
	 * 
	 */
	public CharacterizationProtocolBean() {
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
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}

}
