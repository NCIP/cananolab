package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.domain.characterization.physical.Surface;
import gov.nih.nci.cananolab.domain.characterization.physical.SurfaceChemistry;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SurfaceBean {
	private List<SurfaceChemistry> surfaceChemistryList = new ArrayList<SurfaceChemistry>();

	private Surface domainSurface = new Surface();

	public SurfaceBean() {
	}

	public SurfaceBean(Surface surface) {
		domainSurface = surface;
		if (surface.getSurfaceChemistryCollection()!=null){
			for (SurfaceChemistry chem : surface.getSurfaceChemistryCollection()) {
				surfaceChemistryList.add(chem);
			}
		}
		Collections.sort(surfaceChemistryList,
				new CaNanoLabComparators.SurfaceChemistryDateComparator());
	}

	public Surface getDomainSurface() {
		return domainSurface;
	}

	public List<SurfaceChemistry> getSurfaceChemistryList() {
		return surfaceChemistryList;
	}

	public void addSurfaceChemistry() {
		surfaceChemistryList.add(new SurfaceChemistry());
	}

	public void removeSurfaceChemistry(int ind) {
		surfaceChemistryList.remove(ind);
	}
}
