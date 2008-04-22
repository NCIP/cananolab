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

	private MolecularWeight molecularWeight = new MolecularWeight();

	private Size size = new Size();

	private Solubility solubility = new Solubility();

	private Purity purity = new Purity();

	private SurfaceBean surfaceBean = new SurfaceBean();

	public PhysicalCharacterizationBean() {
		domainChar = new PhysicalCharacterization();
	}

	public PhysicalCharacterizationBean(PhysicalCharacterization achar) {
		super(achar);
		if (achar instanceof MolecularWeight) {
			molecularWeight = (MolecularWeight) achar;
			domainChar = molecularWeight;
			setClassName(ClassUtils.getShortClassName(MolecularWeight.class
					.getName()));
		} else if (achar instanceof Size) {
			size = (Size) achar;
			domainChar = size;
			setClassName(ClassUtils.getShortClassName(Size.class.getName()));
		} else if (achar instanceof PhysicalState) {
			physicalState = (PhysicalState) achar;
			domainChar = physicalState;
			setClassName(ClassUtils.getShortClassName(PhysicalState.class
					.getName()));
		} else if (achar instanceof Shape) {
			shape = (Shape) achar;
			domainChar = shape;
			setClassName(ClassUtils.getShortClassName(Shape.class.getName()));
		} else if (achar instanceof Solubility) {
			solubility = (Solubility) achar;
			domainChar = solubility;
			setClassName(ClassUtils.getShortClassName(Solubility.class
					.getName()));
		} else if (achar instanceof Surface) {
			domainChar = (Surface) achar;
			surfaceBean = new SurfaceBean((Surface) domainChar);
			setClassName(ClassUtils.getShortClassName(Surface.class.getName()));
		} else if (achar instanceof Purity) {
			purity = (Purity) achar;
			domainChar = purity;
			setClassName(ClassUtils.getShortClassName(Purity.class.getName()));
		}
	}

	public MolecularWeight getMolecularWeight() {
		domainChar = molecularWeight;
		setDomainChar();
		return molecularWeight;
	}

	public PhysicalState getPhysicalState() {
		domainChar = physicalState;
		setDomainChar();
		return physicalState;
	}

	public Purity getPurity() {
		domainChar = purity;
		setDomainChar();
		return purity;
	}

	public Shape getShape() {
		domainChar = shape;
		setDomainChar();
		return shape;
	}

	public Size getSize() {
		domainChar = size;
		setDomainChar();
		return size;
	}

	public Solubility getSolubility() {
		domainChar = solubility;
		setDomainChar();
		return solubility;
	}

	public SurfaceBean getSurfaceBean() {
		domainChar = surfaceBean.getDomainSurface();
		setDomainChar();
		return surfaceBean;
	}
}
