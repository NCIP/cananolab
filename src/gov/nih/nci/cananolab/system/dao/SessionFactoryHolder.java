package gov.nih.nci.cananolab.system.dao;

import org.hibernate.SessionFactory;

public class SessionFactoryHolder
{
	private SessionFactory instance;

	public SessionFactory getInstance() {
		return instance;
	}

	public void setInstance(SessionFactory instance) {
		this.instance = instance;
	}
}