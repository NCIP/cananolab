package gov.nih.nci.cananolab.restful.view.edit;

import edu.emory.mathcs.backport.java.util.Arrays;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.restful.util.SampleUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.servlet.http.HttpServletRequest;

public class SimpleDataAvailabilityBean {
	
	String sampleName;
	
	String dataAvailability;
	String caNanoLabScore;
	String mincharScore;

	List<String> chemicalAssocs;
	List<String> physicoChars;
	List<String> invitroChars;
	List<String> invivoChars;
	
	List<String> availableEntityNames;
	
	Map<String, String> caNano2MINChar;
	
	public void transferSampleBeanForDataAvailability(SampleBean sampleBean,
			HttpServletRequest request, String[] availableEntityNames) {

		if (sampleBean == null)
			return;
		
		setSampleName(sampleBean.getDomain().getName());
		setDataAvailability(sampleBean.getDataAvailabilityMetricsScore());

		this.setCaNanoLabScore(sampleBean.getCaNanoLabScore());
		this.setMincharScore(sampleBean.getMincharScore());

		setCaNanoLabScore(sampleBean.getCaNanoLabScore());
		setMincharScore(sampleBean.getMincharScore());
		
		SortedSet<String> ca = (SortedSet<String>) request.getSession().getServletContext().getAttribute("chemicalAssocs");
		setChemicalAssocs(new ArrayList<String>(ca));
		
		setCaNano2MINChar((Map<String, String>) request.getSession().getServletContext()
				.getAttribute("caNano2MINChar"));
		
		SortedSet<String> pc = (SortedSet<String>) request.getSession().getServletContext().getAttribute("physicoChars");
		setPhysicoChars(new ArrayList<String>(pc));
		SortedSet<String> iv = (SortedSet<String>) request.getSession().getServletContext().getAttribute("invitroChars");
		setInvitroChars(new ArrayList<String>(iv));
		SortedSet<String> invivo = (SortedSet<String>) request.getSession().getServletContext().getAttribute("invivoChars");
		setInvivoChars(new ArrayList<String>(invivo));
		
		if (availableEntityNames != null)
			this.availableEntityNames = new ArrayList<String>(Arrays.asList(availableEntityNames));
		
		
		//setSampleName(sampleBean.getDomain().getName());
		
//		SortedSet<String> ca = (SortedSet<String>) request.getSession().getServletContext().getAttribute("chemicalAssocs");
//		this.chemicalAssocs = SampleUtil.getStringArrayFromSortedSet(ca);
//				// SampleUtil.getDefaultListFromSessionByType("chemicalAssocs", request.getSession());
//		
//		
//		this.caNano2MINChar = (Map<String, String>) request.getSession().getServletContext()
//				.getAttribute("caNano2MINChar");
//		
//		SortedSet<String> pc = (SortedSet<String>) request.getSession().getServletContext().getAttribute("physicoChars");
//		this.physicoChars = SampleUtil.getStringArrayFromSortedSet(pc);
//		SortedSet<String> iv = (SortedSet<String>) request.getSession().getServletContext().getAttribute("invitroChars");
//		this.invitroChars = SampleUtil.getStringArrayFromSortedSet(iv);
//		SortedSet<String> invivo = (SortedSet<String>) request.getSession().getServletContext().getAttribute("invivoChars");
//		this.invivoChars = SampleUtil.getStringArrayFromSortedSet(invivo);
//		

	}

	public String getSampleName() {
		return sampleName;
	}


	public void setSampleName(String sampleName) {
		this.sampleName = sampleName;
	}


	public String getDataAvailability() {
		return dataAvailability;
	}


	public void setDataAvailability(String dataAvailability) {
		this.dataAvailability = dataAvailability;
	}

	public List<String> getAvailableEntityNames() {
		return availableEntityNames;
	}
	public void setAvailableEntityNames(List<String> availableEntityNames) {
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
	public List<String> getChemicalAssocs() {
		return chemicalAssocs;
	}
	public void setChemicalAssocs(List<String> chemicalAssocs) {
		this.chemicalAssocs = chemicalAssocs;
	}
	public List<String> getPhysicoChars() {
		return physicoChars;
	}
	public void setPhysicoChars(List<String> physicoChars) {
		this.physicoChars = physicoChars;
	}
	public List<String> getInvitroChars() {
		return invitroChars;
	}
	public void setInvitroChars(List<String> invitroChars) {
		this.invitroChars = invitroChars;
	}
	public List<String> getInvivoChars() {
		return invivoChars;
	}
	public void setInvivoChars(List<String> invivoChars) {
		this.invivoChars = invivoChars;
	}
	public Map<String, String> getCaNano2MINChar() {
		return caNano2MINChar;
	}
	public void setCaNano2MINChar(Map<String, String> caNano2MINChar) {
		this.caNano2MINChar = caNano2MINChar;
	}
	
	

}
