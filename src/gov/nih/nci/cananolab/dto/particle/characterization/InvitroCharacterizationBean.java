package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.domain.particle.characterization.invitro.Caspase3Activation;
import gov.nih.nci.cananolab.domain.particle.characterization.invitro.CellViability;
import gov.nih.nci.cananolab.domain.particle.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.Map;

public class InvitroCharacterizationBean extends CharacterizationBean {
	private Caspase3Activation caspase3Activation = new Caspase3Activation();

	private CellViability cellViability = new CellViability();

	public InvitroCharacterizationBean() {
		super();
	}

	public InvitroCharacterizationBean(InvitroCharacterization chara) {
		super(chara);
		if (chara instanceof Caspase3Activation) {
			caspase3Activation = (Caspase3Activation) chara;
		} else if (chara instanceof CellViability) {
			cellViability = (CellViability) chara;
		}
	}

	public Caspase3Activation getCaspase3Activation() {
		return caspase3Activation;
	}

	public CellViability getCellViability() {
		return cellViability;
	}

	public void setupDomainChar(Map<String, String> typeToClass,
			String createdBy, String internalUriPath) throws Exception {
		// take care of characterizations that don't have any special
		// properties shown in the form, e.g. Hemolysis
		if (domainChar == null) {
			Class clazz = ClassUtils.getFullClass(getClassName());
			domainChar = (Characterization) clazz.newInstance();
		}
		if (domainChar instanceof Caspase3Activation) {
			domainChar = caspase3Activation;
		} else if (domainChar instanceof CellViability) {
			domainChar = cellViability;
		}
		super.setupDomainChar(typeToClass, createdBy, internalUriPath);
	}
}