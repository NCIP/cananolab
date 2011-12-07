/*
 The caNanoLab Software License, Version 1.4

 Copyright 2006 SAIC. This software was developed in conjunction with the National
 Cancer Institute, and so to the extent government employees are co-authors, any
 rights in such works shall be subject to Title 17 of the United States Code,
 section 105.

 */
package gov.nih.nci.cananolab.ui.core;

/**
 * This class calls the Struts ForwardAction to forward to a page, also extends
 * AbstractBaseAction to inherit the user authentication features.
 *
 * @author pansu
 */


public class WelcomeAction extends AbstractForwardAction {
	public boolean loginRequired() {
		return false;
	}
}
