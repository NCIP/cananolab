/**
 * 
 */
package gov.nih.nci.calab.dto.common;

import java.util.List;
import gov.nih.nci.calab.dto.characterization.CharacterizationBean;
/**
 * @author zengje
 *
 */
public class ProtocolFileBean extends LabFileBean{
	private ProtocolBean protocolBean = new ProtocolBean();
	/**
	 * 
	 */
	public ProtocolFileBean() {
		super();
	}
	public ProtocolBean getProtocolBean() {
		return protocolBean;
	}
	public void setProtocolBean(ProtocolBean protocolBean) {
		this.protocolBean = protocolBean;
	}
}
