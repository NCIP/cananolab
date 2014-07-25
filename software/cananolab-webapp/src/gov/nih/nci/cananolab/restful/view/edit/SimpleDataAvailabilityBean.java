package gov.nih.nci.cananolab.restful.view.edit;

import java.util.List;
import java.util.Map;

public class SimpleDataAvailabilityBean {
	
	String caNanoLabScore;
	String mincharScore;

	List<String> chemicalAssocs;
	List<String> physicoChars;
	List<String> invitroChars;
	List<String> invivoChars;
	
	Map<String, String> caNano2MINChar;
	
	
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
