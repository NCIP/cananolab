package gov.nih.nci.cananolab.domain.common;

import gov.nih.nci.cananolab.domain.particle.samplecomposition.functionalization.FunctionalizingEntity;import gov.nih.nci.cananolab.domain.particle.samplecomposition.base.NanoparticleEntity;import java.util.Collection;import gov.nih.nci.cananolab.domain.particle.samplecomposition.chemicalassociation.ChemicalAssociation;import gov.nih.nci.cananolab.domain.particle.samplecomposition.SampleComposition;
import java.io.Serializable;
	/**
	* The file of a facility equipped and competent to conduct scientific experiments, observations, tests, investigations, and/or to manufacture chemicals or medical products.	**/
public class LabFile  implements Serializable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	private String comments;
	private String extension;
	
	
	
		/**
	* Indicates the person or authoritative body who brought the item into existence.	**/
	private String createdBy;
	/**
	* Retreives the value of createdBy attribute
	* @return createdBy
	**/

	public String getCreatedBy(){
		return createdBy;
	}

	/**
	* Sets the value of createdBy attribue
	**/

	public void setCreatedBy(String createdBy){
		this.createdBy = createdBy;
	}
	
		/**
	* The date of the process by which something is brought into existence; having been brought into existence.	**/
	private java.util.Date createdDate;
	/**
	* Retreives the value of createdDate attribute
	* @return createdDate
	**/

	public java.util.Date getCreatedDate(){
		return createdDate;
	}

	/**
	* Sets the value of createdDate attribue
	**/

	public void setCreatedDate(java.util.Date createdDate){
		this.createdDate = createdDate;
	}
	
		/**
	* A written or verbal account, representation, statement, or explanation of something.	**/
	private String description;
	/**
	* Retreives the value of description attribute
	* @return description
	**/

	public String getDescription(){
		return description;
	}

	/**
	* Sets the value of description attribue
	**/

	public void setDescription(String description){
		this.description = description;
	}
	
		/**
	* One or more characters used to identify, name, or characterize the nature, properties, or contents of a thing.	**/
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
	* The words or language units by which a thing is known.	**/
	private String name;
	/**
	* Retreives the value of name attribute
	* @return name
	**/

	public String getName(){
		return name;
	}

	/**
	* Sets the value of name attribue
	**/

	public void setName(String name){
		this.name = name;
	}
	
		/**
	* An identifying appellation signifying status or function.	**/
	private String title;
	/**
	* Retreives the value of title attribute
	* @return title
	**/

	public String getTitle(){
		return title;
	}

	/**
	* Sets the value of title attribue
	**/

	public void setTitle(String title){
		this.title = title;
	}
	
		/**
	* Something distinguishable as an identifiable class based on common qualities.	**/
	private String type;
	/**
	* Retreives the value of type attribute
	* @return type
	**/

	public String getType(){
		return type;
	}

	/**
	* Sets the value of type attribue
	**/

	public void setType(String type){
		this.type = type;
	}
	
		/**
	* A character string that can identify any kind of resource on the Internet, including images, text, video, audio and programs.  The most commom type of a URI is a URL.	**/
	private String uri;
	/**
	* Retreives the value of uri attribute
	* @return uri
	**/

	public String getUri(){
		return uri;
	}

	/**
	* Sets the value of uri attribue
	**/

	public void setUri(String uri){
		this.uri = uri;
	}
	
		/**
	* Whether it is external uri.	**/
	private Boolean uriExternal;
	/**
	* Retreives the value of uriExternal attribute
	* @return uriExternal
	**/

	public Boolean getUriExternal(){
		return uriExternal;
	}

	/**
	* Sets the value of uriExternal attribue
	**/

	public void setUriExternal(Boolean uriExternal){
		this.uriExternal = uriExternal;
	}
	
		/**
	* A form or variant of a type or original; one of a sequence of copies of a program, each incorporating new modifications.	**/
	private String version;
	/**
	* Retreives the value of version attribute
	* @return version
	**/

	public String getVersion(){
		return version;
	}

	/**
	* Sets the value of version attribue
	**/

	public void setVersion(String version){
		this.version = version;
	}
	
	/**
	* An associated gov.nih.nci.cananolab.domain.common.Keyword object's collection 
	**/
			
	private Collection<Keyword> keywordCollection;
	/**
	* Retreives the value of keywordCollection attribue
	* @return keywordCollection
	**/

	public Collection<Keyword> getKeywordCollection(){
		return keywordCollection;
	}

	/**
	* Sets the value of keywordCollection attribue
	**/

	public void setKeywordCollection(Collection<Keyword> keywordCollection){
		this.keywordCollection = keywordCollection;
	}
		
	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof LabFile) 
		{
			LabFile c =(LabFile)obj; 			 
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

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

	/**
	 * @return the extension
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * @param extension the extension to set
	 */
	public void setExtension(String extension) {
		this.extension = extension;
	}
	
}