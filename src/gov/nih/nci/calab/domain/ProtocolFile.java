package gov.nih.nci.calab.domain;


/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * 
 */

public class ProtocolFile extends LabFile implements java.io.Serializable {
	private static final long serialVersionUID = 1234567890L;

	private gov.nih.nci.calab.domain.Protocol protocol;

	public gov.nih.nci.calab.domain.Protocol getProtocol() {

//		ApplicationService applicationService = ApplicationServiceProvider
//				.getApplicationService();
//		gov.nih.nci.calab.domain.ProtocolFile thisIdSet = new gov.nih.nci.calab.domain.ProtocolFile();
//		thisIdSet.setId(this.getId());
//
//		try {
//			java.util.List resultList = applicationService.search(
//					"gov.nih.nci.calab.domain.Protocol", thisIdSet);
//			if (resultList != null && resultList.size() > 0) {
//				protocol = (gov.nih.nci.calab.domain.Protocol) resultList
//						.get(0);
//			}
//
//		} catch (Exception ex) {
//			System.out
//					.println("ProtocolFile:getProtocol throws exception ... ...");
//			ex.printStackTrace();
//		}
		return protocol;

	}

	public void setProtocol(gov.nih.nci.calab.domain.Protocol protocol) {
		this.protocol = protocol;
	}

	// public boolean equals(Object obj){
	// boolean eq = false;
	// if(obj instanceof ProtocolFile) {
	// ProtocolFile c =(ProtocolFile)obj;
	// Long thisId = getId();
	//				
	// if(thisId != null && thisId.equals(c.getId())) {
	// eq = true;
	// }
	//				
	// }
	// return eq;
	// }
	//		
	// public int hashCode(){
	// int h = 0;
	//			
	// if(getId() != null) {
	// h += getId().hashCode();
	// }
	//			
	// return h;
	// }

}