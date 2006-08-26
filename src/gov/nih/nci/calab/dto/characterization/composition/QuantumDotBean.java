/**
 * 
 */
package gov.nih.nci.calab.dto.characterization.composition;


/**
 * @author Zeng
 *
 */
public class QuantumDotBean extends CompositionBean {
	private String core;

	private String shell;

	private String treatment;

	public QuantumDotBean() {		
	}

	public String getCore() {
		return core;
	}

	public void setCore(String core) {
		this.core = core;
	}

	public String getShell() {
		return shell;
	}

	public void setShell(String shell) {
		this.shell = shell;
	}

	public String getTreatment() {
		return treatment;
	}

	public void setTreatment(String treatment) {
		this.treatment = treatment;
	}
}
