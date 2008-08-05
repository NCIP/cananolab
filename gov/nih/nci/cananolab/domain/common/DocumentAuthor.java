package gov.nih.nci.cananolab.domain.common;

import java.util.Collection;
import java.io.Serializable;
	/**
	* 	**/
public class DocumentAuthor  implements Serializable, Comparable<DocumentAuthor>
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	
		/**
	* 	**/
	private String firstName;
	/**
	* Retreives the value of firstName attribute
	* @return firstName
	**/

	public String getFirstName(){
		return firstName;
	}

	/**
	* Sets the value of firstName attribue
	**/

	public void setFirstName(String firstName){
		this.firstName = firstName;
	}
	
		/**
	* 	**/
	private Long id;
	/**
	* Retreives the value of id attribute
	* @return id
	**/

	public Long getId(){
		return id;
	}

	/**
	* Sets the value of id attribue
	**/

	public void setId(Long id){
		this.id = id;
	}
	
		/**
	* 	**/
	private String lastName;
	/**
	* Retreives the value of lastName attribute
	* @return lastName
	**/

	public String getLastName(){
		return lastName;
	}

	/**
	* Sets the value of lastName attribue
	**/

	public void setLastName(String lastName){
		this.lastName = lastName;
	}
	
		/**
	* 	**/
	private String middleInitial;
	/**
	* Retreives the value of middleInitial attribute
	* @return middleInitial
	**/

	public String getMiddleInitial(){
		return middleInitial;
	}

	/**
	* Sets the value of middleInitial attribue
	**/

	public void setMiddleInitial(String middleInitial){
		this.middleInitial = middleInitial;
	}
	
	/**
	* An associated gov.nih.nci.cananolab.domain.common.Publication object's collection 
	**/
			
	private Collection<Publication> publicationCollection;
	/**
	* Retreives the value of publicationCollection attribue
	* @return publicationCollection
	**/

	public Collection<Publication> getPublicationCollection(){
		return publicationCollection;
	}

	/**
	* Sets the value of publicationCollection attribue
	**/

	public void setPublicationCollection(Collection<Publication> publicationCollection){
		this.publicationCollection = publicationCollection;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof DocumentAuthor) 
		{
			DocumentAuthor c =(DocumentAuthor)obj; 			 
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}
		
	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}
	
	public int compareTo(DocumentAuthor author) {
		try {
			int lastCmp = id.compareTo(author.id);
			return lastCmp;
		}catch (Exception ex) {
			return 1;
		}
	}
	
}