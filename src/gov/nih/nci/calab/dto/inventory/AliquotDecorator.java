package gov.nih.nci.calab.dto.inventory;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a sample container to be
 * shown properly in the view page using display tag lib.
 * 
 * @author pansu
 * 
 */
public class AliquotDecorator extends TableDecorator {
	public String getAliquotId() throws Exception {
		AliquotBean aliquot = (AliquotBean) getCurrentRowObject();
		String checked = "";
		if (getListIndex() == 0) {
			checked = "checked";
		}
		String aliquotIdstr = "<input type='radio' name='aliquotId' value='"
				+ aliquot.getAliquotId() + "' " + checked + ">";

		return aliquotIdstr;
	}
}
