
package gov.nih.nci.evs.query;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import org.apache.log4j.*;


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
  * @author caBIO Team 
  * @version 1.0
 */
/**
 * EVSQueryImpl class implements the EVSQuery interface.
 * The query object generated by this class can hold one query at a time. 
 */
public class EVSQueryImpl implements EVSQuery, Serializable {
	
	private static Logger log = Logger.getLogger(EVSQueryImpl.class.getName());

	public HashMap descLogicValues;
	public HashMap metaThesaurusValues;
	private  static final long serialVersionUID =  1468599299281466395L;
	
	public EVSQueryImpl(){
		descLogicValues = new HashMap();
		metaThesaurusValues = new HashMap();
	}
	

	/**
	 * get tree for the specified rootName
	 * @param vocabularyName
	 * @param rootName - RootNode of the tree
	 * @param direction - a boolean value; set to true, if traverse down; to false, otherwise
	 * @param isaFlag - a boolean value; set to true, if the tree contains taxonomy (is a relationships); to false, otherwise
	 * @param attributes - an AttributeSetDescriptor instance
	 * @param levels - the depth of the tree
	 * @param roles - the names of role relationships
	 */
	
	public void getTree(String vocabularyName, String rootName, boolean direction, boolean isaFlag, 
			            int attributes, String levels, Vector roles) 
	{
		
		descLogicValues.put("getTree$vocabularyName", vocabularyName);
		descLogicValues.put("getTree$rootName", rootName);
		descLogicValues.put("getTree$direction", new Boolean(direction));
		descLogicValues.put("getTree$isaFlag", new Boolean(isaFlag));
		descLogicValues.put("getTree$attributes", new Integer(attributes));
		descLogicValues.put("getTree$levels", levels);
		descLogicValues.put("getTree$roles", roles);
	}

    /**
     * get tree for the specified rootName
     * @param vocabularyName
     * @param rootName - RootNode of the tree
     * @param direction - a boolean value; set to true, if traverse down; to false, otherwise
     * @param isaFlag - a boolean value; set to true, if the tree contains taxonomy (is a relationships); to false, otherwise
     * @param attributes - an AttributeSetDescriptor instance
     * @param levels - the depth of the tree
     * @param roles - the names of role relationships
     */
    
    public void getTree(String vocabularyName, String rootName, boolean direction, boolean isaFlag, 
                        int attributes, int levels, Vector roles) 
    {
        getTree(vocabularyName, rootName, direction, isaFlag, attributes, String.valueOf(levels), roles);        
    }	
	/**
	 * Search DescLogicConcepts
	 * @param vocabularyName
	 * @param searchTerm
	 * @param limit
	 */
	
	public void searchDescLogicConcepts(String vocabularyName, String searchTerm, int limit)
	{
		descLogicValues.put("searchDescLogicConcepts$vocabularyName", vocabularyName);
		descLogicValues.put("searchDescLogicConcepts$searchTerm", searchTerm);
		descLogicValues.put("searchDescLogicConcepts$matchLimit", new Integer(limit));		
	}
	
	/**
	 * Search DescLogicConcepts
	 * @param vocabularyName - Specifies the namespace
	 * @param searchTerm  - Specifies the search term
	 * @param limit		  - Specifies the maximum limit of search results
	 * @param matchOption - Specifies the match option <br>
	 * 						Example: option values 			<br>			
	 * 						0 - MATCH_BY_NAME <br>
	 * 						1 - MATCH_BY_ROLE <br>
	 * 						2 - MATCH_BY_PROPERTY <br>
	 * 						3 - MATCH_BY_SILO<br>
	 * 						4 - MATCH_BY_ASSOCIATION<br>
	 * 						5 - MATCH_BY_SYNONYM<br>
	 * 						6 - MATCH_BY_INVERSE_ROLE<br>
	 * 						7 - MATCH_BY_INVERSE_ASSOCIATION<br>
	 * 						8 - MATCH_BY_ROLE_PROPERTY<br>
	 * @param matchType -  	Depends on the match options (not all the time)<br>
	 * 						If match option is 0 match type should be "" <br>
	 * 						If match option is 1 match type should be role name <br>
	 * 						If match option is 2 match type should be property name <br>
	 *  					If match option is 3 match type should be "" <br>
	 *  					If match option is 4 match type should be association name <br>
	 *  					If match option is 5 match type should be a synonym name <br>
	 *  					If match option is 6 match type should be role name <br>
	 * 						If match option is 7 match type should be a association name <br>
	 * 						If match option is 8 match type should be a "" <br>
	 * @param ASDIndex -	Takes an integer ranging from 0-3 <br>
	 * 						0 - WITH_NO_ATTRIBUTES <br>
	 * 						1 - WITH_ALL_ATTRIBUTES <br>
	 * 						2 - WITH_ALL_ROLES <br>
	 * 						3 - WITH_ALL_PROPERTIES <br>
	 */
	
	public void searchDescLogicConcepts(String vocabularyName, String searchTerm, int limit, int matchOption, String matchType, int ASDIndex) throws Exception{
		String msg = null;
		if(matchOption <0 || matchOption >8){
			msg = "Invalid option - value should be between 0-8";
			log.warn(msg);
			}
		else if(ASDIndex <0 || ASDIndex >3){
			msg = "Invalid ASDIndex - value should be between 0-3 ";
			log.warn(msg);
			}
		else if(searchTerm == null ){
			msg = "Search term not specified";
			log.warn(msg);
			}
		else if(vocabularyName == null){
			msg = "Vocabulary name not specified";
			log.warn(msg);			
			}
		
		descLogicValues.put("searchDescLogicConcepts$vocabularyName", vocabularyName);
		descLogicValues.put("searchDescLogicConcepts$searchTerm", searchTerm);
		descLogicValues.put("searchDescLogicConcepts$matchLimit", new Integer(limit));	
		descLogicValues.put("searchDescLogicConcepts$matchOption", new Integer(matchOption));
		descLogicValues.put("searchDescLogicConcepts$matchType", matchType);
		descLogicValues.put("searchDescLogicConcepts$ASDIndex", new Integer(ASDIndex));	
		}


	/**
	 * Gets all concepts that matches the argument pattern of a property.
	 * @param vocabularyName
	 * @param propertyName - all returned concepts should have the value of this Property
	 * @param propertyValue - pattern for searching
	 * @param matchLimit - number of records to get
	 */
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getConceptWithPropertyMatching(java.lang.String, java.lang.String, java.lang.String, int)
	 */
	public void getConceptWithPropertyMatching(String vocabularyName, String propertyName, 
											   String propertyValue, int matchLimit) 
	{
		descLogicValues.put("getConceptWithPropertyMatching$vocabularyName", vocabularyName);
		descLogicValues.put("getConceptWithPropertyMatching$propertyName", propertyName);
		descLogicValues.put("getConceptWithPropertyMatching$propertyValue", propertyValue);
		descLogicValues.put("getConceptWithPropertyMatching$matchLimit", new Integer(matchLimit));

	}
	
	/**
	 * Gets all the concept names from the Silos that matches the  pattern.
	 * @param vocabularyName - specifies the namespace
	 * @param searchTerm - specifies the search string
	 * @param matchLimit - number of records to get
	 */
	public void getConceptWithSiloMatching(String vocabularyName, String searchTerm, int matchLimit){
	    descLogicValues.put("getConceptWithSiloMatching$vocabularyName", vocabularyName);
		descLogicValues.put("getConceptWithSiloMatching$searchTerm", searchTerm);		
		descLogicValues.put("getConceptWithSiloMatching$matchLimit", new Integer(matchLimit));
	}

    /**
     * Gets all the concept names from the spcified Silos that matches the  pattern.
     * @param vocabularyName - specifies the namespace
     * @param searchTerm - specifies the search string
     * @param matchLimit - number of records to get
     * @param siloName - spcifies the name of the Silo
     */
    public void getConceptWithSiloMatching(String vocabularyName, String searchTerm, int matchLimit, String siloName){
        descLogicValues.put("getConceptWithSiloMatching$vocabularyName", vocabularyName);
        descLogicValues.put("getConceptWithSiloMatching$searchTerm", searchTerm);       
        descLogicValues.put("getConceptWithSiloMatching$matchLimit", new Integer(matchLimit));
        descLogicValues.put("getConceptWithSiloMatching$siloName", siloName);
    }


	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getDescLogicConceptNameByCode(java.lang.String, java.lang.String)
	 */
	public void getDescLogicConceptNameByCode(String vocabularyName, String conceptCode) 
	{	
		descLogicValues.put("getDescLogicConceptNameByCode$vocabularyName", vocabularyName);
		descLogicValues.put("getDescLogicConceptNameByCode$conceptCode", conceptCode);
	}
	

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#isSubConcept(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void isSubConcept(String vocabularyName, String code1, String code2) 
	{
		descLogicValues.put("isSubConcept$vocabularyName", vocabularyName);
		descLogicValues.put("isSubConcept$conceptCode1", code1);
		descLogicValues.put("isSubConcept$conceptCode2", code2);
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#isRetired(java.lang.String, java.lang.String)
	 */
	public void isRetired(String vocabularyName, String code) 
	{
		descLogicValues.put("isRetired$vocabularyName", vocabularyName);
		descLogicValues.put("isRetired$conceptCode", code);
	}
	

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getDescendants(java.lang.String, java.lang.String, boolean, java.lang.String, java.lang.String)
	 */
	public  void getDescendants(String vocabularyName, String code, boolean flag, String iBaseLineDate, String fBaseLineDate)
	{
		descLogicValues.put("getDescendants$vocabularyName", vocabularyName);
		descLogicValues.put("getDescendants$conceptCode", code);
		descLogicValues.put("getDescendants$flag", new Boolean(flag));
		descLogicValues.put("getDescendants$iBaseLineDate", iBaseLineDate);
		descLogicValues.put("getDescendants$fBaseLineDate", fBaseLineDate);
	}

	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getPropertyValues(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void getPropertyValues(String vocabularyName, String conceptName, String propertyName) 
	{
		descLogicValues.put("getPropertyValues$vocabularyName", vocabularyName);
		descLogicValues.put("getPropertyValues$conceptName", conceptName);
		descLogicValues.put("getPropertyValues$propertyName", propertyName);
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAncestors(java.lang.String, java.lang.String, boolean, java.lang.String, java.lang.String)
	 */
	public void getAncestors(String vocabularyName, String code, boolean flag, String iBaseLineDate, String fBaseLineDate)
	{
		descLogicValues.put("getAncestors$vocabularyName", vocabularyName);
		descLogicValues.put("getAncestors$conceptCode", code);
		descLogicValues.put("getAncestors$flag", new Boolean(flag));
		descLogicValues.put("getAncestors$iBaseLineDate", iBaseLineDate);
		descLogicValues.put("getAncestors$fBaseLineDate", fBaseLineDate);
	}

	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getSubConcepts(java.lang.String, java.lang.String, java.lang.Boolean, java.lang.Boolean)
	 */
	public void getSubConcepts(String vocabularyName, String conceptName, Boolean inputFlag, Boolean outputFlag) 
	{
		descLogicValues.put("getSubConcepts$vocabularyName", vocabularyName);
		descLogicValues.put("getSubConcepts$conceptName", conceptName);
		descLogicValues.put("getSubConcepts$inputFlag", inputFlag);
		descLogicValues.put("getSubConcepts$outputFlag", outputFlag);
	}
	

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getSuperConcepts(java.lang.String, java.lang.String, java.lang.Boolean, java.lang.Boolean)
	 */
	public void getSuperConcepts(String vocabularyName, String conceptName, Boolean inputFlag, Boolean outputFlag)
	{
		descLogicValues.put("getSuperConcepts$vocabularyName", vocabularyName);
		descLogicValues.put("getSuperConcepts$conceptName", conceptName);
		descLogicValues.put("getSuperConcepts$inputFlag", inputFlag);
		descLogicValues.put("getSuperConcepts$outputFlag", outputFlag);
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getRolesByConceptName(java.lang.String, java.lang.String)
	 */
	public void getRolesByConceptName(String vocabularyName, String conceptName) 
	{
		descLogicValues.put("getRolesByConceptName$vocabularyName", vocabularyName);
		descLogicValues.put("getRolesByConceptName$conceptName", conceptName);
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getPropertiesByConceptName(java.lang.String, java.lang.String)
	 */
	public void getPropertiesByConceptName(String vocabularyName, String conceptName) 
	{
		descLogicValues.put("getPropertiesByConceptName$vocabularyName", vocabularyName);
		descLogicValues.put("getPropertiesByConceptName$conceptName", conceptName);
	}
	
			
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getVocabularyNames()
	 */
	public void getVocabularyNames() 
	{
		descLogicValues.put("getVocabularyNames$name", " ");
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getConceptCodeByName(java.lang.String, java.lang.String)
	 */
	public void getConceptCodeByName(String vocabularyName, String conceptName) 
	{
		descLogicValues.put("getConceptCodeByName$vocabularyName", vocabularyName);
		descLogicValues.put("getConceptCodeByName$conceptName", conceptName);
	}	
	
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getConceptByName(java.lang.String, java.lang.String)
	 */
	public void getConceptByName(String vocabularyName, String conceptName) {
		descLogicValues.put("getConceptByName$vocabularyName", vocabularyName);
		descLogicValues.put("getConceptByName$conceptName", conceptName);

	}


	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getVocabularyHost(java.lang.String)
	 */
	public void getVocabularyHost(String vocabularyName) 
	{
		descLogicValues.put("getVocabularyHost$vocabularyName", vocabularyName);
	}
	

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getVocabularyPort(java.lang.String)
	 */
	public void getVocabularyPort(String vocabularyName) 
	{
		descLogicValues.put("getVocabularyPort$vocabularyName", vocabularyName);
	}
     /**
     * Returns a list of versions for the given vocabulary in the knowledgebase.
     * @param vocabularyName
     */
    public void getVocabularyVersion(String vocabularyName){
        descLogicValues.put("getVocabularyVersion$vocabularyName",vocabularyName);
    }

	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getConceptEditAction(java.lang.String, java.lang.String)
	 */
	public void getConceptEditAction(String vocabularyName, String code) 
	{
		descLogicValues.put("getConceptEditAction$vocabularyName", vocabularyName);
		descLogicValues.put("getConceptEditAction$conceptCode", code);		
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getConceptEditActionDates(java.lang.String, java.lang.String, java.lang.String)
	 */
	public void getConceptEditActionDates(String vocabularyName, String code, String action) 
	{
		descLogicValues.put("getConceptEditActionDates$vocabularyName", vocabularyName);
		descLogicValues.put("getConceptEditActionDates$conceptCode", code);		
		descLogicValues.put("getConceptEditActionDates$action", action);		
	}


	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getRootConcepts(java.lang.String, boolean)
	 */
	public void getRootConcepts(String vocabularyName, boolean allAttributes) 
	{
		descLogicValues.put("getRootConcepts$vocabularyName", vocabularyName);
		descLogicValues.put("getRootConcepts$allAttributes", new Boolean(allAttributes));		
	}




/*  #################   MetaThesaurus #########################################*/ 

	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#searchMetaThesaurus(java.lang.String, int, java.lang.String, boolean, boolean, boolean)
	 */
	public void searchMetaThesaurus(String searchTerm, int limit, String source, 
									boolean cui, boolean shortResult, boolean score) 
	{
		metaThesaurusValues.put("searchMetaThesaurus$searchTerm", searchTerm);
		metaThesaurusValues.put("searchMetaThesaurus$limit", new Integer(limit));
		metaThesaurusValues.put("searchMetaThesaurus$source", source);
		metaThesaurusValues.put("searchMetaThesaurus$cui", new Boolean(cui));
		metaThesaurusValues.put("searchMetaThesaurus$shortResult", new Boolean(shortResult));
		metaThesaurusValues.put("searchMetaThesaurus$score", new Boolean(score));
	}
	
	/**
	 * Searches a concept in MetaThesaurus using the concept unique identifier
	 * @param cui - Concept Unique Identifier
	 */
	
	public void searchMetaThesaurus(String cui){
		metaThesaurusValues.put("searchMetaThesaurus$searchTerm", cui);
		metaThesaurusValues.put("searchMetaThesaurus$cui", new Boolean(true));
	}
	
	
	/**
	 * @deprecated - The preferred method is searchSourceByCode
	 * @see gov.nih.nci.evs.query.EVSQuery#searchByLoincId(java.lang.String, java.lang.String)
	 */
	public void searchByLoincId(String code, String sourceAbbr) 
	{
		searchSourceByCode(code,sourceAbbr);
	}
	

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#searchSourceByCode(String, String)
	 */
	public void searchSourceByCode(String code, String sourceAbbr) 
	{
		metaThesaurusValues.put("searchSourceByCode$code", code);
		metaThesaurusValues.put("searchSourceByCode$sourceAbbr", sourceAbbr);
	}

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getSemanticTypes()
	 */
	public void getSemanticTypes() 
	{
		metaThesaurusValues.put("getSemanticTypes$name", " ");
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getConceptsBySource(java.lang.String)
	 */
	public void getConceptsBySource(String sourceAbbr) 
	{
		metaThesaurusValues.put("getConceptsBySource$sourceAbbr", sourceAbbr);
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getMetaConceptNameByCode(java.lang.String)
	 */
	public void getMetaConceptNameByCode(String conceptCode) 
	{
		metaThesaurusValues.put("getMetaConceptNameByCode$conceptCode", conceptCode);
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getMetaSources()
	 */
	public void getMetaSources() 
	{
		metaThesaurusValues.put("getMetaSources$name", " ");
	}
	

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getChildren(java.lang.String, java.lang.String)
	 */
	public void getChildren(String conceptCode, String sourceAbbr) 
	{
		metaThesaurusValues.put("getChildren$conceptCode", conceptCode);
		metaThesaurusValues.put("getChildren$sourceAbbr", sourceAbbr);	
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getParent(java.lang.String, java.lang.String)
	 */
	public void getParent(String conceptCode, String sourceAbbr) 
	{
		metaThesaurusValues.put("getParent$conceptCode", conceptCode);
		metaThesaurusValues.put("getParent$sourceAbbr", sourceAbbr);	
	}
	

	/**
	 *  @see gov.nih.nci.evs.query.EVSQuery#getBroaderConcepts(java.lang.String)
	 */
	public void getBroaderConcepts(String conceptCode) 
	{
		metaThesaurusValues.put("getBroaderConcepts$conceptCode", conceptCode);
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getBroaderConcepts(java.lang.String, java.lang.String)
	 */
	public void getBroaderConcepts(String conceptCode, String sourceAbbr)
	{
		metaThesaurusValues.put("getBroaderConcepts$conceptCode", conceptCode);
		metaThesaurusValues.put("getBroaderConcepts$sourceAbbr", sourceAbbr);
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getNarrowerConcepts(java.lang.String)
	 */
	public void getNarrowerConcepts(String conceptCode) 
	{
		metaThesaurusValues.put("getNarrowerConcepts$conceptCode", conceptCode);
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getNarrowerConcepts(java.lang.String, java.lang.String)
	 */
	public void getNarrowerConcepts(String conceptCode, String sourceAbbr) 
	{
		metaThesaurusValues.put("getNarrowerConcepts$conceptCode", conceptCode);
		metaThesaurusValues.put("getNarrowerConcepts$sourceAbbr", sourceAbbr);
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getRelatedConcepts(java.lang.String)
	 */
	public void getRelatedConcepts(String conceptCode) 
	{
		metaThesaurusValues.put("getRelatedConcepts$conceptCode", conceptCode);
	}
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getRelatedConcepts(java.lang.String, java.lang.String)
	 */
	public void getRelatedConcepts(String conceptCode, String sourceAbbr) 
	{
		metaThesaurusValues.put("getRelatedConcepts$conceptCode", conceptCode);
		metaThesaurusValues.put("getRelatedConcepts$sourceAbbr", sourceAbbr);
	}
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getRelatedConcepts(String, String, String)
	 */
	public void getRelatedConcepts(String conceptCode, String sourceAbbr, String relation) 
	{
		metaThesaurusValues.put("getRelatedConcepts$conceptCode", conceptCode);
		metaThesaurusValues.put("getRelatedConcepts$sourceAbbr", sourceAbbr);
		metaThesaurusValues.put("getRelatedConcepts$relation", relation);
	}
	
	
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getConceptsByCategories(String, String)
	 */
	public void getConceptsByCategories(String conceptCode, String category) 
	{
		metaThesaurusValues.put("getConceptsByCategories$conceptCode", conceptCode);
		metaThesaurusValues.put("getConceptsByCategories$category", category);
	}
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#containsInverseRole(String, String, String, String)
	 */
	public void containsInverseRole(String vocabularyName,String roleName, String roleValue, String conceptName){
		descLogicValues.put("containsInverseRole$vocabularyName", vocabularyName);
		descLogicValues.put("containsInverseRole$roleName", roleName);
		descLogicValues.put("containsInverseRole$roleValue", roleValue);
		descLogicValues.put("containsInverseRole$conceptName", conceptName);
		}
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#containsRole(String, String, String, String)
	 */
	public void containsRole(String vocabularyName,String roleName, String roleValue, String conceptName){
		descLogicValues.put("containsRole$vocabularyName", vocabularyName);
		descLogicValues.put("containsRole$roleName", roleName);
		descLogicValues.put("containsRole$roleValue", roleValue);
		descLogicValues.put("containsRole$conceptName", conceptName);
		}
	


	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllAssociationTypes(String)
	 */
	public void getAllAssociationTypes(String vocabularyName){
	 	descLogicValues.put("getAllAssociationTypes$vocabularyName",vocabularyName);
	 	}
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllSilos(String)
	 */
	public void getAllSilos(String vocabularyName){
	 	descLogicValues.put("getAllSilos$vocabularyName",vocabularyName);
	 	}
	

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllConceptAssociationQualifierTypes(String)
	 */
	public void getAllConceptAssociationQualifierTypes(String vocabularyName){
		descLogicValues.put("getAllConceptAssociationQualifierTypes$vocabularyName",vocabularyName);
		}
	

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllConceptAssociationTypes(String)
	 */
	public void getAllConceptAssociationTypes(String vocabularyName){
	    descLogicValues.put("getAllConceptAssociationTypes$vocabularyName",vocabularyName);
	    }
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllConceptPropertyQualifierTypes(String)
	 */
	public void getAllConceptPropertyQualifierTypes(String vocabularyName){
	    descLogicValues.put("getAllConceptPropertyQualifierTypes$vocabularyName",vocabularyName);
	    }
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllConceptPropertyTypes(String)
	 */
	public void getAllConceptPropertyTypes(String vocabularyName){
	    descLogicValues.put("getAllConceptPropertyTypes$vocabularyName",vocabularyName);
	    }
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllLicenses(String, String)
	 */
	public void getAllLicenses(String vocabularyName,String condition){
	    descLogicValues.put("getAllLicenses$vocabularyName",vocabularyName);
	    descLogicValues.put("getAllLicenses$condition",condition);
	    }

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllPropertyTypes(String)
	 */
	public void getAllPropertyTypes(String vocabularyName){
	    descLogicValues.put("getAllPropertyTypes$vocabularyName",vocabularyName);
	    }

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllQualifierTypes(String)
	 */
	public void getAllQualifierTypes(String vocabularyName){
	    descLogicValues.put("getAllQualifierTypes$vocabularyName",vocabularyName);
	    }

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllRoleNames(String)
	 */
	public void getAllRoleNames(String vocabularyName){
	    descLogicValues.put("getAllRoleNames$vocabularyName",vocabularyName);
	    }

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllSubConceptCodes(String, String)
	 */
	public void getAllSubConceptCodes(String vocabularyName, String conceptCode){
	    descLogicValues.put("getAllSubConceptCodes$vocabularyName",vocabularyName);
	    descLogicValues.put("getAllSubConceptCodes$conceptCode",conceptCode);
	    }

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllSubConceptNames(String, String)
	 */
	public void getAllSubConceptNames(String vocabularyName, String conceptName){
	    descLogicValues.put("getAllSubConceptNames$vocabularyName",vocabularyName);
	    descLogicValues.put("getAllSubConceptNames$conceptName",conceptName);
	    }
	
	

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllSynonymTypes(String)
	 */
	public void getAllSynonymTypes(String vocabularyName){
	    descLogicValues.put("getAllSynonymTypes$vocabularyName",vocabularyName);	    
	    }
	
	

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllTermAssociationQualifierTypes(String)
	 */
	public void getAllTermAssociationQualifierTypes(String vocabularyName){
	    descLogicValues.put("getAllTermAssociationQualifierTypes$vocabularyName",vocabularyName);
	    }
		

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllTermPropertyQualifierTypes(String)
	 */
	public void getAllTermPropertyQualifierTypes(String vocabularyName){
	    descLogicValues.put("getAllTermPropertyQualifierTypes$vocabularyName",vocabularyName);
	    }

	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getAllTermPropertyTypes(String)
	 */
	public void getAllTermPropertyTypes(String vocabularyName){
	    descLogicValues.put("getAllTermPropertyTypes$vocabularyName",vocabularyName);
	    }	
	
	/**
	 * Retrievs all the parent DescLogicConcepts for a given conceptName
	 * @param vocabularyName - Specifies the vocabulary name 
	 * @param conceptName 	 - Specifies the conceptName
	 * @param inputFlag		 - If the input parameter is a concpeName inputFlag should be false 
	 * 						   If the input parameter is a conceptCode this value should be true.
	 */
	public void getParentConcepts(String vocabularyName, String conceptName, boolean inputFlag){
		descLogicValues.put("getParentConcepts$vocabularyName",vocabularyName);
		descLogicValues.put("getParentConcepts$conceptName",conceptName);
		descLogicValues.put("getParentConcepts$inputFlag", new Boolean(inputFlag));
		}
	
	/**
	 * Retrievs all the child DescLogicConcepts for a given conceptName
	 * @param vocabularyName - Specifies the vocabulary name 
	 * @param conceptName 	 - Specifies the conceptName
	 * @param inputFlag		 - If the input parameter is a concpeName inputFlag should be false 
	 * 						   If the input parameter is a conceptCode this value should be true.
	 */
	
	public void getChildConcepts(String vocabularyName, String conceptName, boolean inputFlag){
		descLogicValues.put("getChildConcepts$vocabularyName",vocabularyName);
		descLogicValues.put("getChildConcepts$conceptName",conceptName);
		descLogicValues.put("getChildConcepts$inputFlag", new Boolean(inputFlag));
		}
	
	/**
	 * Returns true if a given concept has a parent
	 * @param vocabularyName - Specifies the vocabulary name 
	 * @param conceptName 	 - Specifies the conceptName
	 */
	public void hasParents(String vocabularyName, String conceptName){
		descLogicValues.put("hasParents$vocabularyName",vocabularyName);
		descLogicValues.put("hasParents$conceptName",conceptName);
		}
	
	/**
	 * Returns true if a given concept has a child
	 * @param vocabularyName - Specifies the vocabulary name 
	 * @param conceptName 	 - Specifies the conceptName
	 */
	public void hasChildren(String vocabularyName, String conceptName){
		descLogicValues.put("hasChildren$vocabularyName",vocabularyName);
		descLogicValues.put("hasChildren$conceptName",conceptName);
		}
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#fetchDTSProperties(String, String)
	 */
	
	public void fetchDTSProperties(String vocabularyName, String term){
		descLogicValues.put("fetchDTSProperties$vocabularyName", vocabularyName);
		descLogicValues.put("fetchDTSProperties$term", term);		
		}
	
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#fetchTermAssociations(String, String)
	 */
	
	public void fetchTermAssociations(String vocabularyName,String term) {
		descLogicValues.put("fetchTermAssociations$vocabularyName", vocabularyName);
		descLogicValues.put("fetchTermAssociations$term", term);		
		}
	/**
	 * @see gov.nih.nci.evs.query.EVSQuery#getConceptNameByCode(String, String)
	 * @deprecated - The preferred method is getDescLogicConceptNameByCode
	 */
	public void getConceptNameByCode(String vocabularyName, String conceptCode){
		descLogicValues.put("getConceptNameByCode$vocabularyName", vocabularyName);
		descLogicValues.put("getConceptNameByCode$conceptCode", conceptCode);		
		}
	
	/**
     * Gets a DescLogicConcept for a given conceptName or concept code
     * @param conceptName - Specifies the conceptName or concept code
     * @param inputFlag - If a concept code is entered this value is a Boolean.TRUE or if a concept name has been entered this value should be Boolean.FALSE
     */
    public void getDescLogicConcept(String vocabularyName, String conceptName, boolean inputFlag){
    	descLogicValues.put("getDescLogicConcept$vocabularyName", vocabularyName);
		descLogicValues.put("getDescLogicConcept$conceptName", conceptName);
		descLogicValues.put("getDescLogicConcept$inputFlag", new Boolean(inputFlag));
    }
    public void getHistoryRecords(String vocabularyName, String conceptCode){
        descLogicValues.put("getHistoryRecords$vocabularyName", vocabularyName);
        descLogicValues.put("getHistoryRecords$conceptCode", conceptCode);
        
    }
    public void getHistoryRecords(String vocabularyName, String initialDate, String finalDate, String conceptCode){
        descLogicValues.put("getHistoryRecords$vocabularyName", vocabularyName);
        descLogicValues.put("getHistoryRecords$conceptCode", conceptCode);
        descLogicValues.put("getHistoryRecords$initialDate", initialDate);
        descLogicValues.put("getHistoryRecords$finalDate", finalDate);         
    }
    
    public void getHistoryRecords(String vocabularyName, String initialDate, String finalDate){
        descLogicValues.put("getHistoryRecords$vocabularyName", vocabularyName);
        descLogicValues.put("getHistoryRecords$initialDate", initialDate);
        descLogicValues.put("getHistoryRecords$finalDate", finalDate);
    }
}
