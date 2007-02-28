package gov.nih.nci.calab.dto.workflow;

import gov.nih.nci.calab.service.util.SpecialCharReplacer;

import javax.servlet.jsp.PageContext;

import org.displaytag.decorator.DisplaytagColumnDecorator;
import org.displaytag.exception.DecoratorException;
import org.displaytag.properties.MediaTypeEnum;

/**
 * This decorator is used to for display tag to correctly display dynamic links on files
 * @author pansu
 *
 */
public class FileURLDecorator implements DisplaytagColumnDecorator {

	public Object decorate(Object arg0, PageContext arg1, MediaTypeEnum arg2)
			throws DecoratorException {
		WorkflowResultBean workflow = (WorkflowResultBean) arg0;
		String assayType = workflow.getAssay().getAssayType();
		String assayName = workflow.getAssay().getAssayName();
		String runName = workflow.getRun().getName();

		SpecialCharReplacer specialCharReplacer = new SpecialCharReplacer();
		assayType = specialCharReplacer.getReplacedString(assayType);
		assayName = specialCharReplacer.getReplacedString(assayName);
		runName = specialCharReplacer.getReplacedString(runName);

		String fileURL = "runFile.do?dispatch=downloadFile&fileName="
				+ workflow.getFile().getFilename()
				+ "&runId="
				+ workflow.getRun().getId()
				+ "&inout="
				+ workflow.getFile().getInoutType();

		String link = "<a href=" + fileURL + ">"
				+ workflow.getFile().getShortFilename() + "<br>"
				+ workflow.getFile().getTimePrefix() + "</a>";

		return link;
	}
}
