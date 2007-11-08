package gov.nih.nci.calab.domain.nano.function;

public class ImageContrastAgent extends Agent {
	private static final long serialVersionUID = 1234567890L;

	private String name;

	private String type;

	public ImageContrastAgent() {

	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}
}