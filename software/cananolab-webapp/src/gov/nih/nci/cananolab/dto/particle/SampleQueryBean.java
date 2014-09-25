/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.dto.BaseQueryBean;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonTypeName;

/**
 * Information for the sample query form
 * 
 * @author pansu
 * 
 */
@JsonTypeName("SampleQueryBean")
public class SampleQueryBean {//extends BaseQueryBean {
	private String nameType="";
	private String name="";
	
	
	
	
	private String id;
	private String operand;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOperand() {
		return operand;
	}

	public void setOperand(String operand) {
		this.operand = operand;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof BaseQueryBean) {
			BaseQueryBean q = (BaseQueryBean) obj;
			String thisId = this.getId();
			if (thisId != null && thisId.equals(q.getId())) {
				eq = true;
			}
		}
		return eq;
	}
	
	
	

	public String getNameType() {
		return nameType;
	}

	public void setNameType(String nameType) {
		this.nameType = nameType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}
	
//	private void writeObject(ObjectOutputStream o)
//		    throws IOException {  
//		    
//		    o.writeObject(propertyOne);  
//		    o.writeObject(propertyTwo);
//		  }
//		  
//		  private void readObject(ObjectInputStream o)
//		    throws IOException, ClassNotFoundException {  
//		    
//		    propertyOne = (String) o.readObject();  
//		    propertyTwo = (String) o.readObject();
//		    validate();
//		  }

//	public String getDisplayName() {
//		List<String> strs = new ArrayList<String>();
//		strs.add(nameType);
//		strs.add(getOperand());
//		strs.add(name);
//		return StringUtils.join(strs, " ");
//	}
//
//	public String getQueryAsColumnName() {
//		if (nameType.equals("sample name")) {
//			return null;
//		}
//		return nameType;
//	}
}
