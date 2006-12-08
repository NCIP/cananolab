package gov.nih.nci.calab.domain;

import gov.nih.nci.calab.service.util.CalabConstants;

import java.util.Collection;
import java.util.HashSet;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * 
 */

public class Sample implements java.io.Serializable {
	private static final long serialVersionUID = 1234567890L;

	private java.lang.Long id;

	public java.lang.Long getId() {
		return id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	private java.lang.Long sampleSequenceId;

	public java.lang.Long getSampleSequenceId() {
		return sampleSequenceId;
	}

	public void setSampleSequenceId(java.lang.Long sampleSequenceId) {
		this.sampleSequenceId = sampleSequenceId;
	}

	private java.lang.String name;

	public java.lang.String getName() {
		return name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	private java.lang.String type;

	public java.lang.String getType() {
		return type;
	}

	public void setType(java.lang.String type) {
		this.type = type;
	}

	private java.lang.String description;

	public java.lang.String getDescription() {
		return description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	private java.lang.String sourceSampleId;

	public java.lang.String getSourceSampleId() {
		return sourceSampleId;
	}

	public void setSourceSampleId(java.lang.String sourceSampleId) {
		this.sourceSampleId = sourceSampleId;
	}

	private java.lang.String solubility;

	public java.lang.String getSolubility() {
		return solubility;
	}

	public void setSolubility(java.lang.String solubility) {
		this.solubility = solubility;
	}

	private java.lang.String lotId;

	public java.lang.String getLotId() {
		return lotId;
	}

	public void setLotId(java.lang.String lotId) {
		this.lotId = lotId;
	}

	private java.lang.String lotDescription;

	public java.lang.String getLotDescription() {
		return lotDescription;
	}

	public void setLotDescription(java.lang.String lotDescription) {
		this.lotDescription = lotDescription;
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

	private java.lang.String receivedBy;

	public java.lang.String getReceivedBy() {
		return receivedBy;
	}

	public void setReceivedBy(java.lang.String receivedBy) {
		this.receivedBy = receivedBy;
	}

	private java.util.Date receivedDate;

	public java.util.Date getReceivedDate() {
		return receivedDate;
	}

	public void setReceivedDate(java.util.Date receivedDate) {
		this.receivedDate = receivedDate;
	}

	private gov.nih.nci.calab.domain.Source source;

	public gov.nih.nci.calab.domain.Source getSource() {

//		ApplicationService applicationService = ApplicationServiceProvider
//				.getApplicationService();
//		gov.nih.nci.calab.domain.Sample thisIdSet = new gov.nih.nci.calab.domain.Sample();
//		thisIdSet.setId(this.getId());
//
//		try {
//			java.util.List resultList = applicationService.search(
//					"gov.nih.nci.calab.domain.Source", thisIdSet);
//			if (resultList != null && resultList.size() > 0) {
//				source = (gov.nih.nci.calab.domain.Source) resultList.get(0);
//			}
//
//		} catch (Exception ex) {
//			System.out.println("Sample:getSource throws exception ... ...");
//			ex.printStackTrace();
//		}
		return source;

	}

	public void setSource(gov.nih.nci.calab.domain.Source source) {
		this.source = source;
	}

	private gov.nih.nci.calab.domain.SampleSOP sampleSOP;

	public gov.nih.nci.calab.domain.SampleSOP getSampleSOP() {

//		ApplicationService applicationService = ApplicationServiceProvider
//				.getApplicationService();
//		gov.nih.nci.calab.domain.Sample thisIdSet = new gov.nih.nci.calab.domain.Sample();
//		thisIdSet.setId(this.getId());
//		try {
//			java.util.List resultList = applicationService.search(
//					"gov.nih.nci.calab.domain.SampleSOP", thisIdSet);
//
//			if (resultList != null && resultList.size() > 0) {
//				sampleSOP = (gov.nih.nci.calab.domain.SampleSOP) resultList
//						.get(0);
//			}
//		} catch (Exception ex) {
//			System.out.println("Sample:getSampleSOP throws exception ... ...");
//			ex.printStackTrace();
//		}
		return sampleSOP;

	}

	public void setSampleSOP(gov.nih.nci.calab.domain.SampleSOP sampleSOP) {
		this.sampleSOP = sampleSOP;
	}

	private java.util.Collection sampleContainerCollection = new java.util.HashSet();

	public java.util.Collection getSampleContainerCollection() {
		// try{
		// if(sampleContainerCollection.size() == 0) {}
		// } catch(Exception e) {
		// ApplicationService applicationService =
		// ApplicationServiceProvider.getApplicationService();
		// try {
		//			      
		//			      
		//			         
		// gov.nih.nci.calab.domain.Sample thisIdSet = new
		// gov.nih.nci.calab.domain.Sample();
		// thisIdSet.setId(this.getId());
		// java.util.Collection resultList =
		// applicationService.search("gov.nih.nci.calab.domain.SampleContainer",
		// thisIdSet);
		// sampleContainerCollection = resultList;
		// return resultList;
		//				 
		//			      
		// }catch(Exception ex)
		// {
		// System.out.println("Sample:getSampleContainerCollection throws
		// exception ... ...");
		// ex.printStackTrace();
		// }
		// }
		return sampleContainerCollection;
	}

	public java.util.Collection getSampleContainerCollection(String type) {

		HashSet typedContainer = new HashSet();
		for (Object obj : sampleContainerCollection) {
			SampleContainer container = (SampleContainer) obj;
			if ((type.equals(CalabConstants.ALIQUOT) && (container instanceof Aliquot))) {
				typedContainer.add(container);
			} else if ((type.equals(CalabConstants.SAMPLE_CONTAINER) && !(container instanceof Aliquot))) {
				typedContainer.add(container);
			}
		}
		return typedContainer;
	}

	public void setSampleContainerCollection(
			java.util.Collection sampleContainerCollection) {
		this.sampleContainerCollection = sampleContainerCollection;
	}

	private java.util.Collection projectCollection = new java.util.HashSet();

	public java.util.Collection getProjectCollection() {
//		try {
//			if (projectCollection.size() == 0) {
//			}
//		} catch (Exception e) {
//			ApplicationService applicationService = ApplicationServiceProvider
//					.getApplicationService();
//			try {
//
//				gov.nih.nci.calab.domain.Sample thisIdSet = new gov.nih.nci.calab.domain.Sample();
//				thisIdSet.setId(this.getId());
//				java.util.Collection resultList = applicationService.search(
//						"gov.nih.nci.calab.domain.Project", thisIdSet);
//				projectCollection = resultList;
//				return resultList;
//
//			} catch (Exception ex) {
//				System.out
//						.println("Sample:getProjectCollection throws exception ... ...");
//				ex.printStackTrace();
//			}
//		}
		return projectCollection;
	}

	public void setProjectCollection(java.util.Collection projectCollection) {
		this.projectCollection = projectCollection;
	}

	private java.util.Collection<Report> reportCollection = new java.util.HashSet<Report>();

	public java.util.Collection<Report> getReportCollection() {
		return reportCollection;
	}

	public void setReportCollection(java.util.Collection<Report> reportCollection) {
		this.reportCollection = reportCollection;
	}
	
	public Collection<AssociatedFile> associatedFileCollection = new HashSet<AssociatedFile>();
		
	public Collection<AssociatedFile> getAssociatedFileCollection() {
		return associatedFileCollection;
	}

	public void setAssociatedFileCollection(Collection<AssociatedFile> associatedFileCollection) {
		this.associatedFileCollection = associatedFileCollection;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof Sample) {
			Sample c = (Sample) obj;
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