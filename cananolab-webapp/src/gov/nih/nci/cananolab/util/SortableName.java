package gov.nih.nci.cananolab.util;


/**
 * This wrapper class represents an String (with optional url value) that is
 * composed of a non-digit portion and one or more digit portions maybe
 * concatenated by dashes. It is used in run name, container name, sample name
 * and aliquot name. It implements Comparable interface to allow proper sorting
 * of the String.
 * 
 * 
 * @author pansu
 * 
 */
public class SortableName implements Comparable {
	private String name;

	private String url;

	public SortableName(String name) {
		this.name = name;
	}

	public SortableName(String name, String url) {
		this.name = name;
		this.url = url;
	}

	public int compareTo(Object obj) {
		if (obj instanceof SortableName) {
			SortableName sortableName2 = (SortableName) obj;
			return (new Comparators.SortableNameComparator()).compare(
					this.name, sortableName2.getName());
		}
		return 0;
	}

	public String getName() {
		return this.name;
	}

	public String toString() {
		if (this.url == null) {
			return this.name;
		} else {
			return this.url;
		}
	}
}
