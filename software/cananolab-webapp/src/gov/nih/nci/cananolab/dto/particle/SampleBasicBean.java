/*L
 *  Copyright SAIC
 *  Copyright SAIC-Frederick
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/cananolab/LICENSE.txt for details.
 */

package gov.nih.nci.cananolab.dto.particle;

import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.dto.common.SecuredDataBean;

/**
 * This class represents shared properties of samples to be shown in the view
 * pages.
 *
 * @author pansu
 *
 */
public class SampleBasicBean extends SecuredDataBean {
	

	private Sample domain = new Sample();

//	private SortedSet<String> keywordSet = new TreeSet<String>();
//
//	private String[] nanomaterialEntityClassNames = new String[0];
//
//	private String[] functionalizingEntityClassNames = new String[0];
//
//	private String[] chemicalAssociationClassNames = new String[0];
//
//	private String[] functionClassNames = new String[0];
//
//	private String[] characterizationClassNames = new String[0];
//
//	private PointOfContactBean primaryPOCBean = new PointOfContactBean();
//
//	private List<PointOfContactBean> otherPOCBeans = new ArrayList<PointOfContactBean>();
//
//	private Boolean hasComposition = false;
//
//	private Boolean hasCharacterizations = false;
//
//	private Boolean hasPublications = false;
//
//	private Boolean hasDataAvailability = false;
//
//	private PointOfContactBean thePOC = new PointOfContactBean();
//
//	private String cloningSampleName;
//
//	private Set<DataAvailabilityBean> dataAvailability = new HashSet<DataAvailabilityBean>();
//
//	private String dataAvailabilityMetricsScore = Constants.EMPTY;
//	private String caNanoLabScore;
//	private String mincharScore;

	public SampleBasicBean() {
	}

	public SampleBasicBean(String sampleId) {
		domain.setId(new Long(sampleId));
	}

	public SampleBasicBean(Sample sample) {
		this.domain = sample;
		this.createdBy = sample.getCreatedBy();
//		if (sample.getKeywordCollection() != null) {
//			for (Keyword keyword : sample.getKeywordCollection()) {
//				keywordSet.add(keyword.getName());
//			}
//		}
//		keywordsStr = StringUtils.join(keywordSet, "\r\n");
//		if (domain != null) {
//			PointOfContact primaryPOC = domain.getPrimaryPointOfContact();
//			primaryPOCBean = new PointOfContactBean(primaryPOC);
//			if (domain.getOtherPointOfContactCollection() != null) {
//				for (PointOfContact poc : domain
//						.getOtherPointOfContactCollection()) {
//					PointOfContactBean pocBean = new PointOfContactBean(poc);
//					pocBean.setPrimaryStatus(false);
//					otherPOCBeans.add(pocBean);
//				}
//				Collections.sort(otherPOCBeans,
//						new Comparators.PointOfContactBeanDateComparator());
//			}
//			thePOC = primaryPOCBean;
//		}
//		if (sample.getSampleComposition() != null
//				&& sample.getSampleComposition().getId() != null) {
//			hasComposition = true;
//		}
//		if (sample.getCharacterizationCollection() != null
//				&& !sample.getCharacterizationCollection().isEmpty()) {
//			hasCharacterizations = true;
//		}
//		if (sample.getPublicationCollection() != null
//				&& !sample.getPublicationCollection().isEmpty()) {
//			hasPublications = true;
//		}
	}

	
	public Sample getDomain() {
		return domain;
	}

	

	
	
}
