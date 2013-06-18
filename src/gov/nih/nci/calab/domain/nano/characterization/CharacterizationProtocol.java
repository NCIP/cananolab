/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

/**
 * 
 */
package gov.nih.nci.calab.domain.nano.characterization;

import java.io.Serializable;

/**
 * @author zengje
 *
 */
public class CharacterizationProtocol implements Serializable{
	
	private static final long serialVersionUID = 1234567890L;
	
	private Long id;
	private String name;
	private String version;
	/**
	 * 
	 */
	public CharacterizationProtocol() {
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
