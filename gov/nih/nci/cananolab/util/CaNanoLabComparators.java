package gov.nih.nci.cananolab.util;

import gov.nih.nci.cananolab.domain.common.Source;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;

import java.util.Comparator;

/**
 * Contains a list of static comparators for use in caNanoLab
 * 
 * @author pansu
 * 
 */

/* CVS $Id: CaNanoLabComparators.java,v 1.1 2008-04-07 20:13:36 pansu Exp $ */

public class CaNanoLabComparators {

	public static class ParticleSourceComparator implements Comparator<Source> {
		public int compare(Source source1, Source source2) {
			int diff = new SortableNameComparator().compare(source1
					.getOrganizationName(), source2.getOrganizationName());
			return diff;
		}
	}

	public static class SortableNameComparator implements Comparator<String> {
		public int compare(String name1, String name2) {
			// in case of sample name, container name and aliquot name
			if (name1.matches("\\D+(-(\\d+)(\\D+)*)+")
					&& name2.matches("\\D+(-(\\d+)(\\D+)*)+")) {
				String[] toks1 = name1.split("-");
				String[] toks2 = name2.split("-");
				int num = 0;
				if (toks1.length >= toks2.length) {
					num = toks1.length;
				} else {
					num = toks2.length;
				}

				for (int i = 0; i < num; i++) {
					String str1 = "0", str2 = "0";
					if (i < toks1.length) {
						str1 = toks1[i];
					}
					if (i < toks2.length) {
						str2 = toks2[i];
					}
					try {
						int num1 = 0, num2 = 0;
						num1 = Integer.parseInt(str1);
						num2 = Integer.parseInt(str2);
						if (num1 != num2) {
							return num1 - num2;
						}
					} catch (Exception e) {
						if (!str1.equals(str2)) {
							return str1.compareTo(str2);
						}
					}
				}
			}
			// in case of run name
			else if (name1.matches("(\\D+)(\\d+)")
					&& name2.matches("(\\D+)(\\d+)")) {
				try {
					String str1 = name1.replaceAll("(\\D+)(\\d+)", "$1");
					String str2 = name2.replaceAll("(\\D+)(\\d+)", "$1");

					int num1 = Integer.parseInt(name1.replaceAll(
							"(\\D+)(\\d+)", "$2"));
					int num2 = Integer.parseInt(name2.replaceAll(
							"(\\D+)(\\d+)", "$2"));
					if (str1.equals(str2)) {
						return num1 - num2;
					} else {
						return str1.compareTo(str2);
					}
				} catch (Exception e) {
					return name1.compareTo(name2);
				}
			}
			return name1.compareTo(name2);
		}
	}

	public static class ParticleBeanComparator implements
			Comparator<ParticleBean> {
		public int compare(ParticleBean particle1, ParticleBean particle2) {
			return new SortableNameComparator().compare(particle1
					.getParticleSample().getName(), particle2
					.getParticleSample().getName());
		}
	}
}
