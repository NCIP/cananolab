package gov.nih.nci.cananolab.restful.view;

import gov.nih.nci.cananolab.dto.common.PointOfContactBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.util.SampleUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.annotate.JsonFilter;

/**
 * SimpleSampleBean to hold a subset of the data in SampleBean for display on web page.
 * 
 * @author yangs8
 *
 */
@JsonFilter("MyFilter")
public class SimpleSampleBean {
	long sampleId;
	String sampleName;
	String pointOfContact;
	String composition;
	String[] functions;
	String[] characterizations;
	String dataAvailability;
	Date createdDate;
	
	String keywords;
	
	public Map<String, String> pointOfContactInfo;
	long pocBeanDomainId;
	
	String[] availableEntityNames; 
	
	String caNanoLabScore;
	String mincharScore;	
	
	String[] chemicalAssocs;
	String[] physicoChars;
	String[] invitroChars;
	
	Map<String, String> caNanoMINChar;
	private List<PointOfContactBean> otherPOCBeans = new ArrayList<PointOfContactBean>();
	
	public String[] getInvitroChars() {
		return invitroChars;
	}

	public void setInvitroChars(String[] invitroChars) {
		this.invitroChars = invitroChars;
	}

	public String[] getPhysicoChars() {
		return physicoChars;
	}

	public void setPhysicoChars(String[] physicoChars) {
		this.physicoChars = physicoChars;
	}

	public Map<String, String> getCaNanoMINChar() {
		return caNanoMINChar;
	}

	public void setCaNanoMINChar(Map<String, String> caNanoMINChar) {
		this.caNanoMINChar = caNanoMINChar;
	}

	public List<PointOfContactBean> getOtherPOCBeans() {
		return otherPOCBeans;
	}

	public void setOtherPOCBeans(List<PointOfContactBean> otherPOCBeans) {
		this.otherPOCBeans = otherPOCBeans;
	}
	
		public String getComposition() {
		return composition;
	}

	public void setComposition(String composition) {
		this.composition = composition;
	}

		public long getPocBeanDomainId() {
		return pocBeanDomainId;
	}

	public void setPocBeanDomainId(long pocBeanDomainId) {
		this.pocBeanDomainId = pocBeanDomainId;
	}

	public String getKeywords() {
		return keywords;
	}

	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public long getSampleId() {
		return sampleId;
	}

	public void setSampleId(long sampleId) {
		this.sampleId = sampleId;
	}

	public String getSampleName() {
		return sampleName;
	}

	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}

	public String getPointOfContact() {
		return pointOfContact;
	}

	public void setPointOfContact(String pointOfContact) {
		this.pointOfContact = pointOfContact;
	}

	public String[] getFunctions() {
		return functions;
	}

	public void setFunctions(String[] functions) {
		this.functions = functions;
	}

	public String[] getCharacterizations() {
		return characterizations;
	}

	public void setCharacterizations(String[] characterizations) {
		this.characterizations = characterizations;
	}

	public String getDataAvailability() {
		return dataAvailability;
	}

	public void setDataAvailability(String dataAvailability) {
		this.dataAvailability = dataAvailability;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}

	public Map<String, String> getPointOfContactInfo() {
		return pointOfContactInfo;
	}

	public void setPointOfContactInfo(Map<String, String> pointOfContactInfo) {
		this.pointOfContactInfo = pointOfContactInfo;
	}

	public String[] getAvailableEntityNames() {
		return availableEntityNames;
	}

	public void setAvailableEntityNames(String[] availableEntityNames) {
		this.availableEntityNames = availableEntityNames;
	}
	

	public String getCaNanoLabScore() {
		return caNanoLabScore;
	}

	public void setCaNanoLabScore(String caNanoLabScore) {
		this.caNanoLabScore = caNanoLabScore;
	}

	public String getMincharScore() {
		return mincharScore;
	}

	public void setMincharScore(String mincharScore) {
		this.mincharScore = mincharScore;
	}

	public String[] getChemicalAssocs() {
		return chemicalAssocs;
	}

	public void setChemicalAssocs(String[] chemicalAssocs) {
		this.chemicalAssocs = chemicalAssocs;
	}

	public void transferSampleBeanForBasicResultView(SampleBean sampleBean) {
		
		if (sampleBean == null) return;
		setSampleId(sampleBean.getDomain().getId());
		setSampleName(sampleBean.getDomain().getName());
		setPointOfContact(sampleBean.getThePOC().getOrganizationDisplayName());
		setComposition(sampleBean.getDomain().getSampleComposition().getSample().getName());
		setFunctions(sampleBean.getFunctionClassNames());
		setCharacterizations(sampleBean.getCharacterizationClassNames());
		setDataAvailability(sampleBean.getDataAvailabilityMetricsScore());
		
		setCreatedDate(sampleBean.getPrimaryPOCBean().getDomain().getCreatedDate());
		
	}
	
	public void transferSampleBeanForSummaryView(SampleBean sampleBean) {
		
		if (sampleBean == null) return;
		setSampleName(sampleBean.getDomain().getName());
		setCreatedDate(sampleBean.getPrimaryPOCBean().getDomain().getCreatedDate());
		setKeywords(sampleBean.getKeywordsDisplayName());
		setPocBeanDomainId(sampleBean.getPrimaryPOCBean().getDomain().getId());
		setOtherPOCBeans(sampleBean.getOtherPOCBeans());
		
		pointOfContactInfo = new HashMap<String, String>();
		pointOfContactInfo.put("contactPerson", sampleBean.getPrimaryPOCBean().getPersonDisplayName());
		pointOfContactInfo.put("organizationDisplayName", sampleBean.getPrimaryPOCBean().getOrganizationDisplayName());
		pointOfContactInfo.put("role", sampleBean.getPrimaryPOCBean().getDomain().getRole());
		pointOfContactInfo.put("primaryContact", sampleBean.getPrimaryPOCBean().getPrimaryStatus().toString());
		setPointOfContactInfo(pointOfContactInfo);
	}
    
	public void transferSampleBeanForDataAvailability(SampleBean sampleBean, HttpServletRequest request) {
		
		if (sampleBean == null) return;
		setDataAvailability(sampleBean.getDataAvailabilityMetricsScore());
		
		this.setCaNanoLabScore(sampleBean.getCaNanoLabScore());
		this.setMincharScore(sampleBean.getMincharScore());
		
		setSampleName(sampleBean.getDomain().getName());
		
		this.chemicalAssocs= SampleUtil.getDefaultListFromSessionByType("chemicalAssocs", request.getSession());
		
		this.caNanoMINChar = (Map<String, String>) request.getSession().getServletContext()
				.getAttribute("caNano2MINChar");
		
		this.physicoChars = SampleUtil.getDefaultListFromSessionByType("physicoChars", request.getSession());
		this.invitroChars = SampleUtil.getDefaultListFromSessionByType("invitroChars", request.getSession());
		
	}
	
}
