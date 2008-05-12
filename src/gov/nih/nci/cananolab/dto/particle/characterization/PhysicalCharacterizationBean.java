package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalState;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Shape;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Solubility;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Surface;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.SurfaceChemistry;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;

public class PhysicalCharacterizationBean extends CharacterizationBean {
	private PhysicalState physicalState = new PhysicalState();

	private Shape shape = new Shape();

	private Solubility solubility = new Solubility();

	private SurfaceBean surfaceBean = new SurfaceBean();

	public PhysicalCharacterizationBean() {
		super();
	}

	public PhysicalCharacterizationBean(PhysicalCharacterization chara) {
		super(chara);
		if (chara instanceof Surface) {
			surfaceBean = new SurfaceBean((Surface) chara);
		} else if (chara instanceof Shape) {
			shape = (Shape) chara;
		} else if (chara instanceof PhysicalState) {
			physicalState = (PhysicalState) chara;
		} else if (chara instanceof Solubility) {
			solubility = (Solubility) chara;
		}
	}

	public PhysicalState getPhysicalState() {
		return physicalState;
	}

	public Shape getShape() {
		return shape;
	}

	public Solubility getSolubility() {
		return solubility;
	}

	public SurfaceBean getSurfaceBean() {
		return surfaceBean;
	}

	public void setupDomainChar(Map<String, String> typeToClass,
			String createdBy, String internalUriPath) throws Exception {
		super.setupDomainChar(typeToClass, createdBy, internalUriPath);
		if (domainChar instanceof Surface) {
			Surface surface = ((Surface) domainChar);
			if (surface.getSurfaceChemistryCollection() != null) {
				surface.getSurfaceChemistryCollection().clear();
			} else {
				surface
						.setSurfaceChemistryCollection(new HashSet<SurfaceChemistry>());
			}
			for (SurfaceChemistry chem : surfaceBean.getSurfaceChemistryList()) {
				if (chem.getId() == null
						|| chem.getCreatedBy() != null
						&& chem.getCreatedBy().equals(
								CaNanoLabConstants.AUTO_COPY_ANNOTATION_PREFIX)) {
					chem.setCreatedBy(createdBy);
					chem.setCreatedDate(new Date());
				}
				surface.getSurfaceChemistryCollection().add(chem);
			}
		}
	}
}
