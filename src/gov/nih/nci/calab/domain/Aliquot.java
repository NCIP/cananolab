package gov.nih.nci.calab.domain;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * 
 */

public class Aliquot extends SampleContainer implements java.io.Serializable {
	private static final long serialVersionUID = 1234567890L;

	// private java.lang.String name;
	//
	// public java.lang.String getName() {
	// return name;
	// }
	//
	// public void setName(java.lang.String name) {
	// this.name = name;
	// }

	// private gov.nih.nci.calab.domain.DataStatus dataStatus;
	// public gov.nih.nci.calab.domain.DataStatus getDataStatus(){
	//			
	// ApplicationService applicationService =
	// ApplicationServiceProvider.getApplicationService();
	// gov.nih.nci.calab.domain.Aliquot thisIdSet = new
	// gov.nih.nci.calab.domain.Aliquot();
	// thisIdSet.setId(this.getId());
	// try {
	// java.util.List resultList =
	// applicationService.search("gov.nih.nci.calab.domain.DataStatus",
	// thisIdSet);
	//			 
	// if (resultList!=null && resultList.size()>0) {
	// dataStatus = (gov.nih.nci.calab.domain.DataStatus)resultList.get(0);
	// }
	// } catch(Exception ex)
	// {
	// System.out.println("Aliquot:getDataStatus throws exception ... ...");
	// ex.printStackTrace();
	// }
	// return dataStatus;
	//			 		
	// }
	//                        
	//	      
	//	               
	//	   
	//	   
	//	   
	// public void setDataStatus(gov.nih.nci.calab.domain.DataStatus
	// dataStatus){
	// this.dataStatus = dataStatus;
	// }

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof Aliquot) {
			Aliquot c = (Aliquot) obj;
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