package gov.nih.nci.calab.dto.common;

import gov.nih.nci.calab.service.util.CalabComparators;

/**
 * This wrapper class represents an String that is composed of a non-digit portion and 
 * one or more digit portions maybe concatenated by dashes. It is used in run name, 
 * container name, sample name and aliquot name.  It implements Comparable interface
 * to allow proper sorting of the String.
 * 
 * @author pansu
 *
 */
public class SortableName implements Comparable {
	private String name;

	public SortableName(String name) {
		this.name=name;
	}
	
	public int compareTo(Object obj) {
		if (obj instanceof SortableName) {
			SortableName sortableName2 = (SortableName) obj;
			return (new CalabComparators.SortableNameComparator()).compare(name,
					sortableName2.getName());
		}
		return 0;
	}

	public String getName() {
		return name;
	}
	
	public String toString() {
		return name;
	}
}
