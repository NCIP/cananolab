package gov.nih.nci.calab.dto.workflow;

import org.displaytag.decorator.TableDecorator;

/**
 * This decorator is used to for decorate different properties of a run to be
 * shown properly in the view page using display tag lib.
 * 
 * @author pansu
 * 
 */
public class RunDecorator extends TableDecorator {
	public String getRunId() throws Exception {
		RunBean run = (RunBean) getCurrentRowObject();
		String checked = "";
		if (getListIndex() == 0) {
			checked = "checked";
		}
		String runIdstr = "<input type='radio' name='runId' value='"
				+ run.getId() + "' " + checked + ">";

		return runIdstr;
	}
}
