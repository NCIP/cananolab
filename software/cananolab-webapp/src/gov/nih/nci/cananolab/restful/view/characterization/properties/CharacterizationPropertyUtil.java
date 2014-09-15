package gov.nih.nci.cananolab.restful.view.characterization.properties;

public class CharacterizationPropertyUtil {
	
	public static SimpleCharacterizationProperty getPropertyClassByCharName(String charName) 
	{
		if (charName.contains("physical"))
			return new SimplePhysicalState();
		else if (charName.contains("shape"))
			return new SimpleShape();
		else if (charName.contains("solubility"))
			return new SimpleSolubility();
		else if (charName.contains("surface"))
			return new SimpleSurface();
		else if (charName.contains("cytotoxicity"))
			return new SimpleCytotoxicity();
		else if (charName.contains("enzyme"))
			return new SimpleEnzymeInduction();
		else if (charName.contains("transfection"))
			return new SimpleTransfection();
		else 
			return null;

	}

}
