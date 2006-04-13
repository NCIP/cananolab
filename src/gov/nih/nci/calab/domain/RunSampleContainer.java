package gov.nih.nci.calab.domain;


/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * 
 */

public class RunSampleContainer implements java.io.Serializable {
	private static final long serialVersionUID = 1234567890L;

	private java.lang.Long id;

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	private java.lang.String comments;

	public java.lang.String getComments() {
		return comments;
	}

	public void setComments(java.lang.String comments) {
		this.comments = comments;
	}

	private java.lang.String createdBy;

	public java.lang.String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(java.lang.String createdBy) {
		this.createdBy = createdBy;
	}

	private java.util.Date createdDate;

	public java.util.Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(java.util.Date createdDate) {
		this.createdDate = createdDate;
	}

	private gov.nih.nci.calab.domain.SampleContainer sampleContainer;

	public gov.nih.nci.calab.domain.SampleContainer getSampleContainer() {

//		ApplicationService applicationService = ApplicationServiceProvider
//				.getApplicationService();
//		gov.nih.nci.calab.domain.RunSampleContainer thisIdSet = new gov.nih.nci.calab.domain.RunSampleContainer();
//		thisIdSet.setId(this.getId());
//
//		try {
//			java.util.List resultList = applicationService.search(
//					"gov.nih.nci.calab.domain.SampleContainer", thisIdSet);
//			if (resultList != null && resultList.size() > 0) {
//				sampleContainer = (gov.nih.nci.calab.domain.SampleContainer) resultList
//						.get(0);
//			}
//
//		} catch (Exception ex) {
//			System.out
//					.println("RunSampleContainer:getSampleContainer throws exception ... ...");
//			ex.printStackTrace();
//		}
		return sampleContainer;

	}

	public void setSampleContainer(
			gov.nih.nci.calab.domain.SampleContainer sampleContainer) {
		this.sampleContainer = sampleContainer;
	}

	private gov.nih.nci.calab.domain.DataStatus dataStatus;

	public gov.nih.nci.calab.domain.DataStatus getDataStatus() {

//		ApplicationService applicationService = ApplicationServiceProvider
//				.getApplicationService();
//		gov.nih.nci.calab.domain.RunSampleContainer thisIdSet = new gov.nih.nci.calab.domain.RunSampleContainer();
//		thisIdSet.setId(this.getId());
//		try {
//			java.util.List resultList = applicationService.search(
//					"gov.nih.nci.calab.domain.DataStatus", thisIdSet);
//
//			if (resultList != null && resultList.size() > 0) {
//				dataStatus = (gov.nih.nci.calab.domain.DataStatus) resultList
//						.get(0);
//			}
//		} catch (Exception ex) {
//			System.out
//					.println("RunSampleContainer:getDataStatus throws exception ... ...");
//			ex.printStackTrace();
//		}
		return dataStatus;

	}

	public void setDataStatus(gov.nih.nci.calab.domain.DataStatus dataStatus) {
		this.dataStatus = dataStatus;
	}

	private gov.nih.nci.calab.domain.Run run;

	public gov.nih.nci.calab.domain.Run getRun() {

//		ApplicationService applicationService = ApplicationServiceProvider
//				.getApplicationService();
//		gov.nih.nci.calab.domain.RunSampleContainer thisIdSet = new gov.nih.nci.calab.domain.RunSampleContainer();
//		thisIdSet.setId(this.getId());
//
//		try {
//			java.util.List resultList = applicationService.search(
//					"gov.nih.nci.calab.domain.Run", thisIdSet);
//			if (resultList != null && resultList.size() > 0) {
//				run = (gov.nih.nci.calab.domain.Run) resultList.get(0);
//			}
//
//		} catch (Exception ex) {
//			System.out
//					.println("RunSampleContainer:getRun throws exception ... ...");
//			ex.printStackTrace();
//		}
		return run;

	}

	public void setRun(gov.nih.nci.calab.domain.Run run) {
		this.run = run;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof RunSampleContainer) {
			RunSampleContainer c = (RunSampleContainer) obj;
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