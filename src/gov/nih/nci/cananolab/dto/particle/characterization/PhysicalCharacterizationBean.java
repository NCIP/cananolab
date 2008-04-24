package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.particle.characterization.physical.MolecularWeight;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalCharacterization;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.PhysicalState;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Purity;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Shape;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Size;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Solubility;
import gov.nih.nci.cananolab.domain.particle.characterization.physical.Surface;
import gov.nih.nci.cananolab.util.ClassUtils;

public class PhysicalCharacterizationBean extends CharacterizationBean {
	private PhysicalState physicalState = new PhysicalState();

	private Shape shape = new Shape();

	private Solubility solubility = new Solubility();

	private SurfaceBean surfaceBean = new SurfaceBean();

	public PhysicalCharacterizationBean() {
	}

	public PhysicalCharacterizationBean(PhysicalCharacterization chara) {
		super(chara);
		if (chara instanceof MolecularWeight) {
			domainChar = (MolecularWeight) chara; 
			setClassName(ClassUtils.getShortClassName(MolecularWeight.class
					.getName()));
		} else if (chara instanceof Size) {
			domainChar = (Size) chara;
			setClassName(ClassUtils.getShortClassName(Size.class.getName()));
		} else if (chara instanceof PhysicalState) {
			physicalState = (PhysicalState) chara;
			domainChar = physicalState;
			setClassName(ClassUtils.getShortClassName(PhysicalState.class
					.getName()));
		} else if (chara instanceof Shape) {
			shape = (Shape) chara;
			domainChar = shape;
			setClassName(ClassUtils.getShortClassName(Shape.class.getName()));
		} else if (chara instanceof Solubility) {
			solubility = (Solubility) chara;
			domainChar = solubility;
			setClassName(ClassUtils.getShortClassName(Solubility.class
					.getName()));
		} else if (chara instanceof Surface) {
			domainChar = (Surface) chara;
			surfaceBean = new SurfaceBean((Surface) domainChar);
			setClassName(ClassUtils.getShortClassName(Surface.class.getName()));
		} else if (chara instanceof Purity) {
			domainChar = (Purity) chara;
			setClassName(ClassUtils.getShortClassName(Purity.class.getName()));
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
