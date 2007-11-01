package gov.nih.nci.calab.service.util;

import gov.nih.nci.calab.dto.sample.AliquotBean;
import gov.nih.nci.calab.dto.sample.ContainerBean;
import gov.nih.nci.calab.dto.sample.SampleBean;

import java.util.Comparator;

/**
 * Contains a list of static comparators for use in caLAB
 * 
 * @author pansu
 * 
 */

/* CVS $Id: CaNanoLabComparators.java,v 1.4 2007-11-01 17:35:00 pansu Exp $ */

public class CaNanoLabComparators {

	public static class AliquotBeanComparator implements
			Comparator<AliquotBean> {
		public int compare(AliquotBean aliquot1, AliquotBean aliquot2) {
			int diff = new SortableNameComparator().compare(aliquot1
					.getAliquotName(), aliquot2.getAliquotName());
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

	public static class SampleBeanComparator implements Comparator<SampleBean> {
		public int compare(SampleBean sample1, SampleBean sample2) {
			return new SortableNameComparator().compare(
					sample1.getSampleName(), sample2.getSampleName());
		}
	}

	public static class ContainerBeanComparator implements
			Comparator<ContainerBean> {
		public int compare(ContainerBean container1, ContainerBean container2) {
			return new SortableNameComparator().compare(container1
					.getContainerName(), container2.getContainerName());
		}
	}
}
