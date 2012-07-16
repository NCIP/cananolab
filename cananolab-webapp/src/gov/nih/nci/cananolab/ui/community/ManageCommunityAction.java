/*
 The caNanoLab Software License, Version 1.5.2

 Copyright 2006 SAIC. This software was developed in conjunction with the National
 Cancer Institute, and so to the extent government employees are co-authors, any
 rights in such works shall be subject to Title 17 of the United States Code,
 section 105.

 */
package gov.nih.nci.cananolab.ui.community;

/**
 * This class calls the Struts ForwardAction to forward to a page
 *
 * @author pansu
 */

import gov.nih.nci.cananolab.ui.core.AbstractForwardAction;

public class ManageCommunityAction extends AbstractForwardAction {
	public boolean loginRequired() {
		return true;
	}
}
