package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalState;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Shape;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Solubility;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Surface;

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
		}
	}

	public PhysicalState getPhysicalState() {
		domainChar = physicalState;
		return physicalState;
	}

	public Shape getShape() {
		domainChar = shape;
		return shape;
	}

	public Solubility getSolubility() {
		domainChar = solubility;
		return solubility;
	}

	public SurfaceBean getSurfaceBean() {
		domainChar = surfaceBean.getDomainSurface();
		return surfaceBean;
	}
}
