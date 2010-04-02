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
	private Map<String, SortedSet<String>> type2CharacterizationNames = new HashMap<String, SortedSet<String>>();
	private Map<String, SortedSet<CharacterizationBean>> type2Characterizations = new HashMap<String, SortedSet<CharacterizationBean>>();
	private Map<String, SortedSet<CharacterizationBean>> charName2Characterizations = new HashMap<String, SortedSet<CharacterizationBean>>();

	private Map<String, Integer> charName2Counts=new HashMap<String, Integer>();
	
	public CharacterizationSummaryViewBean(List<CharacterizationBean> chars) {
		SortedSet<CharacterizationBean> typeChars = null;
		SortedSet<CharacterizationBean> nameChars = null;
		SortedSet<String> typeCharNames = null;
		Integer charNameCount=0;
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
			String charName=achar.getCharacterizationName();
			typeCharNames.add(charName);
			
			if (charName2Characterizations.get(charName) != null) {
				nameChars = charName2Characterizations.get(charName);
			} else {
				nameChars = new TreeSet<CharacterizationBean>(
						new Comparators.CharacterizationBeanNameDateComparator());
				charName2Characterizations.put(charName, nameChars);
			}
			nameChars.add(achar);

			if (charName2Counts.get(charName)!=null) {
				charNameCount=charName2Counts.get(charName);
			}
			else {				
				charNameCount=0;
			}
			charNameCount++;
			charName2Counts.put(charName, charNameCount);
		}
		characterizationTypes = type2Characterizations.keySet();
	}

	public Set<String> getCharacterizationTypes() {
		return characterizationTypes;
	}

	public Map<String, SortedSet<CharacterizationBean>> getType2Characterizations() {
		return type2Characterizations;
	}

	public Map<String, SortedSet<CharacterizationBean>> getCharName2Characterizations() {
		return charName2Characterizations;
	}
	
	public Map<String, SortedSet<String>> getType2CharacterizationNames() {
		return type2CharacterizationNames;
	}

	public Map<String, Integer> getCharName2Counts() {
		return charName2Counts;
	}
}
