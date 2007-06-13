package gov.nih.nci.calab.dto.characterization;

public class CharacterizationTypeBean {
	private String type;

	// for display use
	private int indentLevel;

	// for display use
	private boolean hasAction;

	public CharacterizationTypeBean(String type, int indentLevel,
			boolean hasAction) {
		super();
		this.type = type;
		this.indentLevel = indentLevel;
		this.hasAction = hasAction;
	}

	public boolean isHasCharNode() {
		return hasAction;
	}

	public void setHasCharNode(boolean hasAction) {
		this.hasAction = hasAction;
	}

	public int getIndentLevel() {
		return indentLevel;
	}

	public void setIndentLevel(int indentLevel) {
		this.indentLevel = indentLevel;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isHasAction() {
		return hasAction;
	}

	public void setHasAction(boolean hasAction) {
		this.hasAction = hasAction;
	}
}
