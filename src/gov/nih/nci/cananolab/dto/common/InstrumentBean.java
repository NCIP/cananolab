package gov.nih.nci.cananolab.dto.common;

import gov.nih.nci.cananolab.domain.common.Instrument;

/**
 * This class represents attributes of an instrument to be viewed in a view
 * page.
 *
 * @author pansu
 *
 */
public class InstrumentBean {
	private Instrument domain;
	private String displayName;

	public InstrumentBean() {
		super();
		domain = new Instrument();
	}

	public InstrumentBean(Instrument instrument) {
		domain = instrument;
	}

	public Instrument getDomain() {
		return domain;
	}

	public String getDisplayName() {
		if (domain != null) {
			if (domain.getManufacturer() != null && domain.getModel() != null) {
				displayName = domain.getType() + "(" + domain.getManufacturer()
						+ " " + domain.getModel() + ")";
			} else if (domain.getModel() == null) {
				displayName = domain.getType() + "(" + domain.getManufacturer()
						+ ")";
			} else {
				displayName = domain.getType();
			}
		}
		return displayName;
	}
}
