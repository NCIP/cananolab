package gov.nih.nci.calab.dto.characterization.composition;

import gov.nih.nci.calab.dto.characterization.CharacterizationBean;

import java.util.List;

public class PolymerBean extends CharacterizationBean {
	private String crosslinked;

	private String crosslinkDegree;

	private String initiator;

	private String numberOfMonomers;

	private List<MonomerBean> monomers;

	public PolymerBean() {
	}

	public String getCrosslinkDegree() {
		return crosslinkDegree;
	}

	public String getCrosslinked() {
		return crosslinked;
	}

	public String getInitiator() {
		return initiator;
	}

	public String getNumberOfMonomers() {
		return numberOfMonomers;
	}

	public void setCrosslinkDegree(String crosslinkDegree) {
		this.crosslinkDegree = crosslinkDegree;
	}

	public void setCrosslinked(String crosslinked) {
		this.crosslinked = crosslinked;
	}

	public void setInitiator(String initiator) {
		this.initiator = initiator;
	}

	public void setNumberOfMonomers(String numberOfMonomers) {
		this.numberOfMonomers = numberOfMonomers;
	}

	public List<MonomerBean> getMonomers() {
		return monomers;
	}

	public void setMonomers(List<MonomerBean> monomers) {
		this.monomers = monomers;
	}

	public MonomerBean getMonomer(int ind) {
		return monomers.get(ind);
	}

	public void setMonomer(int ind, MonomerBean monomer) {
		monomers.set(ind, monomer);
	}
}
