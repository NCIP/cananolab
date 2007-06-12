/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

/**
 * @author zengje
 *
 */
public class CharacterizationCategory {

	private Long id;
	private String category;
	private String name;
	/**
	 * 
	 */
	public CharacterizationCategory() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public CharacterizationCategory(Long id, String category, String name) {
		this.id = id;
		this.category = category;
		this.name = name;
	}
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
