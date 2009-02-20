package gov.nih.nci.cananolab.dto.particle.characterization;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

public class CharacterizationSummaryViewBean {
	private Set<String> characterizationTypes;
	private SortedMap<String, List<CharacterizationBean>> type2Characterizations = new TreeMap<String, List<CharacterizationBean>>();

	public CharacterizationSummaryViewBean(List<CharacterizationBean> chars) {
		List<CharacterizationBean> typeChars = null;
		for (CharacterizationBean achar : chars) {
			String type = achar.getCharacterizationType();
			type="Physico-Chemical Characterization";
			if (type2Characterizations.get(type) != null) {
				typeChars = type2Characterizations.get(type);
			} else {
				typeChars = new ArrayList<CharacterizationBean>();
				type2Characterizations.put(type, typeChars);
			}
			typeChars.add(achar);
		}
		characterizationTypes = type2Characterizations.keySet();
	}

	public Set<String> getCharacterizationTypes() {
		return characterizationTypes;
	}

	public SortedMap<String, List<CharacterizationBean>> getType2Characterizations() {
		return type2Characterizations;
	}
}
