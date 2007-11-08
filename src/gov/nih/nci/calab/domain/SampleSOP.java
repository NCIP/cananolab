package gov.nih.nci.calab.domain;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */

/**
 * 
 */

public class SampleSOP implements java.io.Serializable {
	private static final long serialVersionUID = 1234567890L;

	private java.lang.Long id;

	public java.lang.Long getId() {
		return this.id;
	}

	public void setId(java.lang.Long id) {
		this.id = id;
	}

	private java.lang.String description;

	public java.lang.String getDescription() {
		return this.description;
	}

	public void setDescription(java.lang.String description) {
		this.description = description;
	}

	private java.lang.String name;

	public java.lang.String getName() {
		return this.name;
	}

	public void setName(java.lang.String name) {
		this.name = name;
	}

	private java.util.Collection sampleSOPFileCollection = new java.util.HashSet();

	public java.util.Collection getSampleSOPFileCollection() {
		// try {
		// if (sampleSOPFileCollection.size() == 0) {
		// }
		// } catch (Exception e) {
		// ApplicationService applicationService = ApplicationServiceProvider
		// .getApplicationService();
		// try {
		//
		// gov.nih.nci.calab.domain.SampleSOP thisIdSet = new
		// gov.nih.nci.calab.domain.SampleSOP();
		// thisIdSet.setId(this.getId());
		// java.util.Collection resultList = applicationService.search(
		// "gov.nih.nci.calab.domain.SampleSOPFile", thisIdSet);
		// sampleSOPFileCollection = resultList;
		// return resultList;
		//
		// } catch (Exception ex) {
		// System.out
		// .println("SampleSOP:getSampleSOPFileCollection throws exception ...
		// ...");
		// ex.printStackTrace();
		// }
		// }
		return this.sampleSOPFileCollection;
	}

	public void setSampleSOPFileCollection(
			java.util.Collection sampleSOPFileCollection) {
		this.sampleSOPFileCollection = sampleSOPFileCollection;
	}

	public boolean equals(Object obj) {
		boolean eq = false;
		if (obj instanceof SampleSOP) {
			SampleSOP c = (SampleSOP) obj;
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