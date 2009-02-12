package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.characterization.invitro.Cytotoxicity;
import gov.nih.nci.cananolab.domain.characterization.invitro.InvitroCharacterization;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.util.ClassUtils;

import java.util.Map;

public class InvitroCharacterizationBean extends CharacterizationBean {
	private Cytotoxicity cytotoxicity = new Cytotoxicity();

	public InvitroCharacterizationBean() {
		super();
	}

	public InvitroCharacterizationBean(InvitroCharacterization chara) {
		super(chara);
		if (chara instanceof Cytotoxicity) {
			cytotoxicity = (Cytotoxicity) chara;
		}
	}

	public void setupDomainChar(Map<String, String> typeToClass,
			String createdBy, String internalUriPath) throws Exception {
		// take care of characterizations that don't have any special
		// properties shown in the form, e.g. Hemolysis
		if (domainChar == null) {
			Class clazz = ClassUtils.getFullClass(getClassName());
			domainChar = (Characterization) clazz.newInstance();
		}
		if (domainChar instanceof Cytotoxicity) {
			domainChar = cytotoxicity;
		}
		super.setupDomainChar(typeToClass, createdBy, internalUriPath);
	}
}