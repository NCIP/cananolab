package gov.nih.nci.cananolab.dto.particle.characterization;

import gov.nih.nci.cananolab.util.Comparators;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

public class CharacterizationSummaryViewBean {
	private Set<String> characterizationTypes;
	private Map<String, SortedSet<CharacterizationBean>> type2Characterizations = new HashMap<String, SortedSet<CharacterizationBean>>();
	private Map<String, SortedSet<String>> type2CharacterizationNames = new HashMap<String, SortedSet<String>>();

	public CharacterizationSummaryViewBean(List<CharacterizationBean> chars) {
		SortedSet<CharacterizationBean> typeChars = null;
		SortedSet<String> typeCharNames = null;
		for (CharacterizationBean achar : chars) {
			String type = achar.getCharacterizationType();
			if (type2Characterizations.get(type) != null) {
				typeChars = type2Characterizations.get(type);
			} else {
				typeChars = new TreeSet<CharacterizationBean>(
						new Comparators.CharacterizationBeanNameDateComparator());
				type2Characterizations.put(type, typeChars);
			}
			typeChars.add(achar);

			if (type2CharacterizationNames.get(type) != null) {
				typeCharNames = type2CharacterizationNames.get(type);
			} else {
				typeCharNames = new TreeSet<String>();
				type2CharacterizationNames.put(type, typeCharNames);
			}
			typeCharNames.add(achar.getCharacterizationName());
		}
		characterizationTypes = type2Characterizations.keySet();
	}

	public Set<String> getCharacterizationTypes() {
		return characterizationTypes;
	}

	public Map<String, SortedSet<CharacterizationBean>> getType2Characterizations() {
		return type2Characterizations;
	}

	public Map<String, SortedSet<String>> getType2CharacterizationNames() {
		return type2CharacterizationNames;
	}
}
