package gov.nih.nci.cananolab.dto.characterization;

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

	public CharacterizationSummaryViewBean(List<CharacterizationBean> chars) {
		SortedSet<CharacterizationBean> typeChars = null;
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
		}
		characterizationTypes = type2Characterizations.keySet();
	}

	public Set<String> getCharacterizationTypes() {
		return characterizationTypes;
	}

	public Map<String, SortedSet<CharacterizationBean>> getType2Characterizations() {
		return type2Characterizations;
	}
}
