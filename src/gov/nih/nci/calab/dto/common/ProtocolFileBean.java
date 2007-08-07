/**
 * 
 */
package gov.nih.nci.calab.dto.common;

import gov.nih.nci.calab.domain.ProtocolFile;

/**
 * @author zengje
 * 
 */
public class ProtocolFileBean extends LabFileBean {
	private ProtocolBean protocolBean = new ProtocolBean();

	/**
	 * 
	 */
	public ProtocolFileBean() {
		super();
	}

	public ProtocolFileBean(ProtocolFile protocolFile) {
		super(protocolFile);
		protocolBean=new ProtocolBean(protocolFile.getProtocol());
	}

	public ProtocolBean getProtocolBean() {
		return protocolBean;
	}

	public void setProtocolBean(ProtocolBean protocolBean) {
		this.protocolBean = protocolBean;
	}
//	public int compareTo(Object obj) {
//		int comparison = 0;
//		if (obj instanceof ProtocolFileBean) {
//			ProtocolFileBean inPb = (ProtocolFileBean) obj;
//			
//			if (this.getName() != null){
//				comparison = this.getName().compareTo(inPb.getName());
//				if (comparison == 0){
//					if (this.getVersion() != null)
//						comparison = this.getVersion().compareTo(inPb.getVersion());
//				}
//			}
//			else {
//				if (this.getVersion() != null){
//					comparison = this.getVersion().compareTo(inPb.getVersion());
//				}
//			}
//		}
//		return comparison;
//	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof ProtocolFileBean) {
			ProtocolFileBean c = (ProtocolFileBean) obj;
			String thisId = this.getId();
			// String name = this.getName();
			if (thisId != null && thisId.equals(c.getId())) { // &&
				// name != null && name.equals(c.getName())) {
				eq = true;
			}
		}
		return eq;
	}
}
