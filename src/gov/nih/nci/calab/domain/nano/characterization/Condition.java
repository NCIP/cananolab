/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.calab.domain.nano.characterization;

import gov.nih.nci.calab.domain.Measurement;

import java.io.Serializable;

public class Condition implements Serializable {

	private static final long serialVersionUID = 1234567890L;

	private Long id;

	private String name;

	private Measurement value;

	public Condition() {
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

	public void setName(String type) {
		this.name = type;
	}

	public Measurement getValue() {
		return value;
	}

	public void setValue(Measurement value) {
		this.value = value;
	}

}
