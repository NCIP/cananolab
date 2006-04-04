

package gov.nih.nci.evs.domain;

import java.util.*;

/**
 * <!-- LICENSE_TEXT_START -->
The caBIO Software License, Version 3.1 Copyright 2001-2006 Science Applications International Corporation (SAIC)  
Copyright Notice.  The software subject to this notice and license includes both human readable source code form and machine readable, binary, object code form (the caBIO Software).  The caBIO Software was developed in conjunction with the National Cancer Institute (NCI) by NCI employees and employees of SAIC.  To the extent government employees are authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.    
This caBIO Software License (the License) is between NCI and You.  You (or Your) shall mean a person or an entity, and all other entities that control, are controlled by, or are under common control with the entity.  Control for purposes of this definition means (i) the direct or indirect power to cause the direction or management of such entity, whether by contract or otherwise, or (ii) ownership of fifty percent (50%) or more of the outstanding shares, or (iii) beneficial ownership of such entity.  
This License is granted provided that You agree to the conditions described below.  NCI grants You a non-exclusive, worldwide, perpetual, fully-paid-up, no-charge, irrevocable, transferable and royalty-free right and license in its rights in the caBIO Software to (i) use, install, access, operate, execute, copy, modify, translate, market, publicly display, publicly perform, and prepare derivative works of the caBIO Software; (ii) distribute and have distributed to and by third parties the caBIO Software and any modifications and derivative works thereof; and (iii) sublicense the foregoing rights set out in (i) and (ii) to third parties, including the right to license such rights to further third parties.  For sake of clarity, and not by way of limitation, NCI shall have no right of accounting or right of payment from You or Your sublicensees for the rights granted under this License.  This License is granted at no charge to You.
1.	Your redistributions of the source code for the Software must retain the above copyright notice, this list of conditions and the disclaimer and limitation of liability of Article 6, below.  Your redistributions in object code form must reproduce the above copyright notice, this list of conditions and the disclaimer of Article 6 in the documentation and/or other materials provided with the distribution, if any.
2.	Your end-user documentation included with the redistribution, if any, must include the following acknowledgment: This product includes software developed by SAIC and the National Cancer Institute.  If You do not include such end-user documentation, You shall include this acknowledgment in the Software itself, wherever such third-party acknowledgments normally appear.
3.	You may not use the names "The National Cancer Institute", "NCI" Science Applications International Corporation and "SAIC" to endorse or promote products derived from this Software.  This License does not authorize You to use any trademarks, service marks, trade names, logos or product names of either NCI or SAIC, except as required to comply with the terms of this License.
4.	For sake of clarity, and not by way of limitation, You may incorporate this Software into Your proprietary programs and into any third party proprietary programs.  However, if You incorporate the Software into third party proprietary programs, You agree that You are solely responsible for obtaining any permission from such third parties required to incorporate the Software into such third party proprietary programs and for informing Your sublicensees, including without limitation Your end-users, of their obligation to secure any required permissions from such third parties before incorporating the Software into such third party proprietary software programs.  In the event that You fail to obtain such permissions, You agree to indemnify NCI for any claims against NCI by such third parties, except to the extent prohibited by law, resulting from Your failure to obtain such permissions.
5.	For sake of clarity, and not by way of limitation, You may add Your own copyright statement to Your modifications and to the derivative works, and You may provide additional or different license terms and conditions in Your sublicenses of modifications of the Software, or any derivative works of the Software as a whole, provided Your use, reproduction, and distribution of the Work otherwise complies with the conditions stated in this License.
6.	THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY, NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED.  IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE, SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

 * <!-- LICENSE_TEXT_END -->
 */
 
  /**
   * The DescLogicConcept class represents the fundamental vocabulary entity in the NCI Thesaurus. 
   * 
   */


public  class DescLogicConcept
	implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

		
	
	   
	 /**
	 * Specifies the name of the concept. 	   
	 */
	   	
	
	private java.lang.String name;	   
	   
	   
	/**
	* Returns the name for this DescLogicConcept	   
	* @return - name
	*/
	public  java.lang.String getName(){
	      return name;
	      }   
	   		
	/**
	* Sets the specified name for this DescLogicConcept	   
	* @param - name
	*/
	public void setName( java.lang.String name){
	      this.name = name;
	   }	   
	
	   
	 /**
	 * The namespaceId is used to identify a set of concepts within a terminology such as the NCI Thesaurus.	   
	 */
	   	
	
	private int namespaceId;	   
	   
	   
	/**
	* Returns the namespaceId for this DescLogicConcept	   
	* @return - namespaceId
	*/
	public int getNamespaceId(){
	      return namespaceId;
	      }   
	   		
	/**
	* Sets the specified namespaceId for this DescLogicConcept	   
	* @param - namespaceId
	*/
	public void setNamespaceId(int namespaceId){
	      this.namespaceId = namespaceId;
	   }	   
	
	   
	 /**
	 * Sets this value to 'true'  if a concept is retired. 	   
	 */
	   	
	
	private java.lang.Boolean isRetired;	   
	   
	   
	/**
	* Returns the isRetired value for this DescLogicConcept	   
	* @return - isRetired
	*/
	public  java.lang.Boolean getIsRetired(){
	      return isRetired;
	      }   
	   	  
	
	/**
	* @deprecated  - The preffered way to do this is by using the getIsRetired method
	*/
	public  java.lang.Boolean isRetired(){
	   	      return isRetired;
	   	   }	   	   
	   		
	/**
	* Sets the isRetired value for this DescLogicConcept	   
	* @param - isRetired
	*/
	public void setIsRetired( java.lang.Boolean isRetired){
	      this.isRetired = isRetired;
	   }	   
	
	   
	 /**
	 * This value is set to 'true' if a concept has a parent	   
	 */
	   	
	
	private java.lang.Boolean hasParents;	   
	   
	   
	/**
	* Returns the hasParent value for this DescLogicConcept	   
	* @return - hasParents
	*/
	public  java.lang.Boolean getHasParents(){
	      return hasParents;
	      }   
	   	  
	
	/**
	* @deprecated  - The preffered way to do this is by using the getHasParents method
	*/
	public  java.lang.Boolean hasParents(){
	   	      return hasParents;
	   	   }	   	   
	   		
	/**
	* Sets the specified hasParent value for this DescLogicConcept	   
	* @param - hasParents
	*/
	public void setHasParents( java.lang.Boolean hasParents){
	      this.hasParents = hasParents;
	   }	   
	
	   
	 /**
	 * Sets this value to 'true' if a concept has children	   
	 */
	   	
	
	private java.lang.Boolean hasChildren;	   
	   
	   
	/**
	* Returns the hasChildren value for this DescLogicConcept	   
	* @return - hasChildren
	*/
	public  java.lang.Boolean getHasChildren(){
	      return hasChildren;
	      }   
	   	  
	
	/**
	* @deprecated  - The preffered way to do this is by using the getHasChildren method
	*/
	public  java.lang.Boolean hasChildren(){
	   	      return hasChildren;
	   	   }	   	   
	   		
	/**
	* Sets the specified hasChildren value for this DescLogicConcept	   
	* @param - hasChildren
	*/
	public void setHasChildren( java.lang.Boolean hasChildren){
	      this.hasChildren = hasChildren;
	   }	   
	
	   
	 /**
	 * This is an unique code associated with a concept within the specified vocabulary. 	   
	 */
	   	
	
	private java.lang.String code;	   
	   
	   
	/**
	* Returns the concept code for this DescLogicConcept	   
	* @return - code
	*/
	public  java.lang.String getCode(){
	      return code;
	      }   
	   		
	/**
	* Sets the specified concept code for this DescLogicConcept	   
	* @param - code
	*/
	public void setCode( java.lang.String code){
	      this.code = code;
	   }	   
	
	   
	 /**
	 * Stores the inverse relationship between two concepts	   
	 */
	   	
		private java.util.Vector inverseRoleCollection = new Vector();	   
	   
	   
	/**
	* Returns the inverse role value for this DescLogicConcept	   
	* @return - inverseRoleCollection
	*/
	public  java.util.Vector getInverseRoleCollection(){
	      return inverseRoleCollection;
	      }   
	   		
	/**
	* Sets the specified inverse role value for this DescLogicConcept	   
	* @param - inverseRoleCollection
	*/
	public void setInverseRoleCollection( java.util.Vector inverseRoleCollection){
	      this.inverseRoleCollection = inverseRoleCollection;
	   }	   
	
	   
	 /**
	 * 	   
	 */
	   	
		private java.util.Vector semanticTypeVector = new Vector();	   
	   
	   
	/**
	* Returns the SemanticTypes of this DescLogicConcept	   
	* @return - semanticTypeVector
	*/
	public  java.util.Vector getSemanticTypeVector(){
	      return semanticTypeVector;
	      }   
	   		
	/**
	* Sets the specified Semantic Types for this DescLogicConcept	   
	* @param - semanticTypeVector
	*/
	public void setSemanticTypeVector( java.util.Vector semanticTypeVector){
	      this.semanticTypeVector = semanticTypeVector;
	   }	   
	
	   
	 /**
	 * This attribute holds association information between concepts	   
	 */
	   	
		private java.util.Vector inverseAssociationCollection = new Vector();	   
	   
	   
	/**
	* Returns the inverse association information of this DescLogicConcept	   
	* @return - inverseAssociationCollection
	*/
	public  java.util.Vector getInverseAssociationCollection(){
	      return inverseAssociationCollection;
	      }   
	   		
	/**
	* Sets the specified inverse association value for this DescLogicConcept	   
	* @param - inverseAssociationCollection
	*/
	public void setInverseAssociationCollection( java.util.Vector inverseAssociationCollection){
	      this.inverseAssociationCollection = inverseAssociationCollection;
	   }	   
	
	
	   
	   	
	   
	   
	   
	
	   
	   	
	   
	   
	   
	      			
			
	private gov.nih.nci.evs.domain.TreeNode treeNode;
	/**
	* @deprecated - The preferred way of doing this is by using the getEdgeProperties method	   
	* @return - treeNode
	*/

	public gov.nih.nci.evs.domain.TreeNode getTreeNode(){
		return treeNode;			
        }		   
	      
	     
		   
	   
	/**
	* @deprecated - the preferred way of doing this is by using the setEdgeProperties method	   
	* @param - treeNode
	*/
		
	public void setTreeNode(gov.nih.nci.evs.domain.TreeNode treeNode){
		this.treeNode = treeNode;
	}	
	
	
	   
	   	
	   
	   
	   
	      	      		
	      		
	private java.util.Vector associationCollection = new java.util.Vector();
	/**
	* Returns the associations of this DescLogicConcept	   
	* @return - associationCollection
	*/

	public java.util.Vector getAssociationCollection(){
		return associationCollection;
	}
	              	  
	      
	     
		   
	   	
	/**
	* Sets the specified associations for this DescLogicConcept	   
	* @return - associationCollection
	*/

	public void setAssociationCollection(java.util.Vector associationCollection){
	   	
	   	this.associationCollection = associationCollection;
	        }	
	   
	
	   
	   	
	   
	   
	   
	      	      		
	      		
	private java.util.Vector roleCollection = new java.util.Vector();
	/**
	* Returns the roles of this DescLogicConcept	   
	* @return - roleCollection
	*/

	public java.util.Vector getRoleCollection(){
		return roleCollection;
	}
	              	  
	      
	     
		   
	   	
	/**
	* Sets the specified roles for this DescLogicConcept	   
	* @return - roleCollection
	*/

	public void setRoleCollection(java.util.Vector roleCollection){
	   	
	   	this.roleCollection = roleCollection;
	        }	
	   
	
	   
	   	
	   
	   
	   
	      			
			
	private gov.nih.nci.evs.domain.EdgeProperties edgeProperties;
	/**
	* Returns the edgeProperties for this DescLogicConcept.	   
	* @return - edgeProperties
	*/

	public gov.nih.nci.evs.domain.EdgeProperties getEdgeProperties(){
		return edgeProperties;			
        }		   
	      
	     
		   
	   
	/**
	* Sets the specified edgeProperties value for this DescLogicConcept when a tree is generated.	   
	* @param - edgeProperties
	*/
		
	public void setEdgeProperties(gov.nih.nci.evs.domain.EdgeProperties edgeProperties){
		this.edgeProperties = edgeProperties;
	}	
	
	
	   
	   	
	   
	   
	   
	      	      		
	      		
	private java.util.Vector propertyCollection = new java.util.Vector();
	/**
	* Returns the properties of this DescLogicConcept	   
	* @return - propertyCollection
	*/

	public java.util.Vector getPropertyCollection(){
		return propertyCollection;
	}
	              	  
	      
	     
		   
	   	
	/**
	* Sets the specified properties for this DescLogicConcept	   
	* @return - propertyCollection
	*/

	public void setPropertyCollection(java.util.Vector propertyCollection){
	   	
	   	this.propertyCollection = propertyCollection;
	        }	
	   
	
	public boolean equals(Object obj){
		boolean eq = false;
		if(obj instanceof DescLogicConcept) {
			DescLogicConcept c =(DescLogicConcept)obj; 
								
				String thisKey =  getCode();			
				if(thisKey!= null && thisKey.equals(c.getCode())) {
					eq = true;
				}		
				
			}
			return eq;
		}
		

	public int hashCode(){
		int h = 0;					
		if(getCode() != null) {
			h += getCode().hashCode();
		}
		return h;
	}
	
	
}
