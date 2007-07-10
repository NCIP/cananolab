package gov.nih.nci.calab.domain;


/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * 
 */

public class OutputFile extends LabFile implements java.io.Serializable {
	private static final long serialVersionUID = 1234567890L;

	private gov.nih.nci.calab.domain.DataStatus dataStatus;

	public gov.nih.nci.calab.domain.DataStatus getDataStatus() {

		// ApplicationService applicationService =
		// ApplicationServiceProvider.getApplicationService();
		// gov.nih.nci.calab.domain.OutputFile thisIdSet = new
		// gov.nih.nci.calab.domain.OutputFile();
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
		// System.out.println("OutputFile:getDataStatus throws exception ...
		// ...");
		// ex.printStackTrace();
		// }
		return dataStatus;

	}

	public void setDataStatus(gov.nih.nci.calab.domain.DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}

	private gov.nih.nci.calab.domain.Run run;

	public gov.nih.nci.calab.domain.Run getRun() {

		// ApplicationService applicationService =
		// ApplicationServiceProvider.getApplicationService();
		// gov.nih.nci.calab.domain.OutputFile thisIdSet = new
		// gov.nih.nci.calab.domain.OutputFile();
		// thisIdSet.setId(this.getId());
		//			  
		// try {
		// java.util.List resultList =
		// applicationService.search("gov.nih.nci.calab.domain.Run", thisIdSet);
		// if (resultList!=null && resultList.size()>0) {
		// run = (gov.nih.nci.calab.domain.Run)resultList.get(0);
		// }
		//		          
		// } catch(Exception ex)
		// {
		// System.out.println("OutputFile:getRun throws exception ... ...");
		// ex.printStackTrace();
		// }
		return run;

	}

	public void setRun(gov.nih.nci.calab.domain.Run run) {
		this.run = run;
	}
}