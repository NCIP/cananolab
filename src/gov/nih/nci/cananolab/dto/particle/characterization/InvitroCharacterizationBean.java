package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.particle.characterization.invitro.Caspase3Activation;
import gov.nih.nci.cananolab.domain.particle.characterization.invitro.CellViability;
import gov.nih.nci.cananolab.domain.particle.characterization.invitro.InvitroCharacterization;

public class InvitroCharacterizationBean extends CharacterizationBean {
	private Caspase3Activation caspase3Activation = new Caspase3Activation();

	private CellViability cellViability = new CellViability();

	public InvitroCharacterizationBean() {
		super();
	}

	public InvitroCharacterizationBean(InvitroCharacterization chara) {
		super(chara);
		if (chara instanceof Caspase3Activation) {
			caspase3Activation=(Caspase3Activation)chara;
		}
		else if (chara instanceof CellViability) {
			cellViability=(CellViability)chara;
		}
	}

	public Caspase3Activation getCaspase3Activation() {
		domainChar = caspase3Activation;
		return caspase3Activation;
	}

	public CellViability getCellViability() {
		domainChar = cellViability;
		return cellViability;
	}
}
