package gov.nih.nci.cananolab.dto.particle.composition;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;
import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;

public class ChemicalAssociationBean {
	private String associationType;

	private ChemicalAssociation chemicalAssociation;

	private Integer associationNumber;

	public ChemicalAssociationBean(ChemicalAssociation chemicalAssociation) {
		associationType = chemicalAssociation.getClass().getCanonicalName();
	}

	public int compareTo(ChemicalAssociationBean other) {
		return associationType.compareTo(other.getAssociationType());
	}

	public Integer getAssociationNumber() {
		return associationNumber;
	}

	public void setAssociationNumber(Integer associationNumber) {
		this.associationNumber = associationNumber;
	}

	public ChemicalAssociation getChemicalAssociation() {
		return chemicalAssociation;
	}

	public void setChemicalAssociation(
			ChemicalAssociation chemicalAssociation) {
		this.chemicalAssociation =chemicalAssociation;
	}

	public String getAssociationType() {
		return associationType;
	}

}
