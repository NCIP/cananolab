package gov.nih.nci.calab.domain;


/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * 
 */

public class Protocol implements java.io.Serializable {
	private static final long serialVersionUID = 1234567890L;

	private java.lang.Long id;

	private String type;
	
	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	private java.lang.String name;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	private java.util.Collection protocolFileCollection = new java.util.HashSet();

	public java.util.Collection getProtocolFileCollection() {
//		try {
//			if (protocolFileCollection.size() == 0) {
//			}
//		} catch (Exception e) {
//			ApplicationService applicationService = ApplicationServiceProvider
//					.getApplicationService();
//			try {
//
//				gov.nih.nci.calab.domain.Protocol thisIdSet = new gov.nih.nci.calab.domain.Protocol();
//				thisIdSet.setId(this.getId());
//				java.util.Collection resultList = applicationService.search(
//						"gov.nih.nci.calab.domain.ProtocolFile", thisIdSet);
//				protocolFileCollection = resultList;
//				return resultList;
//
//			} catch (Exception ex) {
//				System.out
//						.println("Protocol:getProtocolFileCollection throws exception ... ...");
//				ex.printStackTrace();
//			}
//		}
		return protocolFileCollection;
	}

	public void setProtocolFileCollection(
			java.util.Collection protocolFileCollection) {
		this.protocolFileCollection = protocolFileCollection;
	}

	private java.util.Collection assayCollection = new java.util.HashSet();

	public java.util.Collection getAssayCollection() {
//		try {
//			if (assayCollection.size() == 0) {
//			}
//		} catch (Exception e) {
//			ApplicationService applicationService = ApplicationServiceProvider
//					.getApplicationService();
//			try {
//
//				gov.nih.nci.calab.domain.Protocol thisIdSet = new gov.nih.nci.calab.domain.Protocol();
//				thisIdSet.setId(this.getId());
//				java.util.Collection resultList = applicationService.search(
//						"gov.nih.nci.calab.domain.Assay", thisIdSet);
//				assayCollection = resultList;
//				return resultList;
//
//			} catch (Exception ex) {
//				System.out
//						.println("Protocol:getAssayCollection throws exception ... ...");
//				ex.printStackTrace();
//			}
//		}
		return assayCollection;
	}

	public void setAssayCollection(java.util.Collection assayCollection) {
		this.assayCollection = assayCollection;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof Protocol) {
			Protocol c = (Protocol) obj;
			Long thisId = getId();

			if (thisId != null && thisId.equals(c.getId())) {
				eq = true;
			}

		}
		return eq;
	}

	public int hashCode() {
		int h = 0;

		if (getId() != null) {
			h += getId().hashCode();
		}

		return h;
	}

}