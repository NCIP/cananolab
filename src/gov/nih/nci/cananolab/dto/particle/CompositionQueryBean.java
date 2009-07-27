package gov.nih.nci.cananolab.dto.particle;

/**
 * Information for the composition query form
 * 
 * @author pansu
 *
 */
public class CompositionQueryBean {
	private String id;
	private String compositionType;
	private String entityType;
	private String chemicalName;
	private String operand;
	
	public String getCompositionType() {
		return compositionType;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public void setCompositionType(String compositionType) {
		this.compositionType = compositionType;
	}
	public String getEntityType() {
		return entityType;
	}
	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}
	public String getChemicalName() {
		return chemicalName;
	}
	public void setChemicalName(String chemicalName) {
		this.chemicalName = chemicalName;
	}
	public String getOperand() {
		return operand;
	}
	public void setOperand(String operand) {
		this.operand = operand;
	}
}
