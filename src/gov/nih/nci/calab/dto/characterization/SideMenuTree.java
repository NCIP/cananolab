package gov.nih.nci.calab.dto.characterization;

import gov.nih.nci.calab.domain.nano.characterization.Characterization;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * @author cais
 *
 */
public class SideMenuTree {
	
	private static Map<String, String> ascendTypeTreeMap;
	
	//all characterization types
	private static Map<String, Set<String>> typeTreeMap = null;
	
	/* 
	 * key: charType, category column of table def_characterization_category
	 * value: list of charaBeans under the charType
	 */
	private static Map<String, List<CharacterizationBean>> charTypeCharsMap = null;
	
	/* 
	 * same structure as the map, charTypeCharsMap
	 * but it saves charTypes that have particle characterizations under them.
	 */
	private Map<String, List<CharacterizationBean>> selectedCharTypeCharsMap;
	
	//characterization types that have particle characterization data
	private Map<String, Set<String>> selectedTypeTreeMap = null;
	
	/*
	 * list of characterizationBean of particles
	 */
	private List<CharacterizationBean> particleCharBeans;
	
	/*
	 * nameCharMap key: the 'name' column of table
	 * def_characterization_category These names do not exist in the
	 * 'category' column, i.e. they are the lowest level in the category
	 * tree. value: list of CharacterizationBean
	 */
	Map<String, List<CharacterizationBean>> charNameCharMap;
	
	public SideMenuTree(Map<String, List<CharacterizationBean>> charTypeChars,
			List<CharacterizationBean> particleCharBeans)
	{
		initStaticCharTypeTree(charTypeChars);
		this.particleCharBeans = particleCharBeans;
		selectedCharTypeCharsMap = new HashMap<String, List<CharacterizationBean>>();
		selectCharTypeChars();
	}
	
	public SideMenuTree(Map<String, List<CharacterizationBean>> charTypeChars,
			Map<String, List<CharacterizationBean>> selectedCharTypeCharsMap) {
		
		initStaticCharTypeTree(charTypeChars);
		this.selectedCharTypeCharsMap = selectedCharTypeCharsMap;
	}
	
	private void initStaticCharTypeTree(Map<String, List<CharacterizationBean>> charTypeChars) {
		if(charTypeCharsMap == null) {
			setCharTypeCharsMap(charTypeChars);
			setAscendTypeTreeMap();
			setTypeTreeMap();
		}
	}
	
	private void setSelectedTypeTreeMap() {
		selectedTypeTreeMap = createCharaTypeTree(
				selectedCharTypeCharsMap, getAscendTypeTreeMap());
	}
	
	public Map<String, Set<String>> getSelectedTypeTreeMap() {
		if(selectedTypeTreeMap == null)
			setSelectedTypeTreeMap();
		
		return selectedTypeTreeMap;
	}

	private void selectCharTypeChars() {
		for (String charType : charTypeCharsMap.keySet()) {
			List<CharacterizationBean> newCharBeans = new ArrayList<CharacterizationBean>();

			// get all characterizations for the characterization type
			List<CharacterizationBean> charList = (List<CharacterizationBean>) charTypeCharsMap
				.get(charType);
			// set abbreviation for each saved characterization
			for (CharacterizationBean displayBean : charList) {
				for (CharacterizationBean charBean : particleCharBeans) {
					if (displayBean.getName().equals(charBean.getName())) {
						//charBean.setAbbr(displayBean.getAbbr());
						newCharBeans.add(charBean);
					}
				}
			}
			if (!newCharBeans.isEmpty()) {
				selectedCharTypeCharsMap.put(charType, newCharBeans);
			}
		}
	} 
	
	public void setLeafCharaMap() {
		charNameCharMap = new HashMap<String, List<CharacterizationBean>>();
		for (String charCategory : selectedCharTypeCharsMap.keySet()) {
			List<CharacterizationBean> charList = (List<CharacterizationBean>) selectedCharTypeCharsMap
					.get(charCategory);
			for (CharacterizationBean cbean : charList) {
				String pname = cbean.getName();
				if (!selectedCharTypeCharsMap.containsKey(pname)) {
					if (charNameCharMap.containsKey(pname)) {
						List<CharacterizationBean> clist = (List<CharacterizationBean>) charNameCharMap
								.get(pname);
						clist.add(cbean);
					} else {
						List<CharacterizationBean> charBeanList = new ArrayList<CharacterizationBean>();
						charBeanList.add(cbean);
						charNameCharMap.put(pname, charBeanList);
					}
				}
			}
		}
	}
	
	public Map<String, List<CharacterizationBean>> getLeafCharaMap()
	{
		if(charNameCharMap == null)
			setLeafCharaMap();
			
		return charNameCharMap;
	}
	
	private static Map<String, Set<String>> createCharaTypeTree(
			Map<String, List<CharacterizationBean>> charMap,
			Map<String, String> ascendTypeTreeMap) {

		// key: parent type; value: set of child types;
		Map<String, Set<String>> typeTreeMapTmp = new HashMap<String, Set<String>>();

		for (String charType : charMap.keySet()) {
			List<CharacterizationBean> charBeans = (List<CharacterizationBean>) charMap
					.get(charType);

			for (CharacterizationBean charBean : charBeans) {

				String childType = charBean.getName();
				while (childType != null
						&& !childType
								.equalsIgnoreCase(Characterization.PHYSICAL_CHARACTERIZATION)
						&& !childType
								.equalsIgnoreCase(Characterization.INVITRO_CHARACTERIZATION)) {
					String parentType = ascendTypeTreeMap.get(childType);

					if (typeTreeMapTmp.containsKey(parentType)) {
						typeTreeMapTmp.get(parentType).add(childType);
					} else {
						Set<String> typeSet = new TreeSet<String>();
						typeSet.add(childType);
						typeTreeMapTmp.put(parentType, typeSet);
					}
					childType = parentType;
				}
			}
		}
		return typeTreeMapTmp;
	}
	
	public static void setCharTypeCharsMap (Map<String, 
			List<CharacterizationBean>> charTypeChars) {
		charTypeCharsMap = charTypeChars;
	}

	public static Map<String, List<CharacterizationBean>> getCharTypeCharsMap() {
		return charTypeCharsMap;
	}
	
	private static void setAscendTypeTreeMap() {
		ascendTypeTreeMap = new HashMap<String, String>();
		for (String ctype : charTypeCharsMap.keySet()) {
			List<CharacterizationBean> cbeanList = (List<CharacterizationBean>) charTypeCharsMap
					.get(ctype);
			for (CharacterizationBean cbean : cbeanList) {
				String cname = cbean.getName();
				ascendTypeTreeMap.put(cname, ctype);
			}
		}
	}
	
	private static Map<String, String> getAscendTypeTreeMap() {
		if(ascendTypeTreeMap == null)
			setAscendTypeTreeMap();
		return ascendTypeTreeMap;
	}
	
	private static void setTypeTreeMap() {
		typeTreeMap = createCharaTypeTree(
			charTypeCharsMap, getAscendTypeTreeMap());
	}
	
	public static Map<String, Set<String>> getTypeTreeMap() {
		if(typeTreeMap == null)
			setTypeTreeMap();
		
		return typeTreeMap;
	}

}
