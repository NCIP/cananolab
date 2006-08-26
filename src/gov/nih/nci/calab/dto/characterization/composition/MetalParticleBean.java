/**
 * 
 */
package gov.nih.nci.calab.dto.characterization.composition;


/**
 * @author Zeng
 *
 */
public class MetalParticleBean extends CompositionBean {
	private String core;

	private String shell;
	
	private String composition;
	
	private String coating;

	public MetalParticleBean() {		
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

	public String getComposition() {
		return composition;
	}

	public void setComposition(String composition) {
		this.composition = composition;
	}

	public String getCoating() {
		return coating;
	}

	public void setCoating(String coating) {
		this.coating = coating;
	}
	
	
}
