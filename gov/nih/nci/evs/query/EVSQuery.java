
package gov.nih.nci.evs.query;

import java.util.Date;
import java.util.Vector;
import java.io.Serializable;

/**
  * <!-- LICENSE_TEXT_START -->
* Copyright 2001-2004 SAIC. Copyright 2001-2003 SAIC. This software was developed in conjunction with the National Cancer Institute,
* and so to the extent government employees are co-authors, any rights in such works shall be subject to Title 17 of the United States Code, section 105.
* Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
* 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the disclaimer of Article 3, below. Redistributions
* in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the documentation and/or other
* materials provided with the distribution.
* 2. The end-user documentation included with the redistribution, if any, must include the following acknowledgment:
* "This product includes software developed by the SAIC and the National Cancer Institute."
* If no such end-user documentation is to be included, this acknowledgment shall appear in the software itself,
* wherever such third-party acknowledgments normally appear.
* 3. The names "The National Cancer Institute", "NCI" and "SAIC" must not be used to endorse or promote products derived from this software.
* 4. This license does not authorize the incorporation of this software into any third party proprietary programs. This license does not authorize
* the recipient to use any trademarks owned by either NCI or SAIC-Frederick.
* 5. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, (INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
* MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL CANCER INSTITUTE,
* SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
* PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
* WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
  * <!-- LICENSE_TEXT_END -->
  */
/**
  * @author caBIO Team
  * @version 1.0
 */

/**
 * EVSQuery interface describes the methods that can be implemented to access the Enterprise Vocabulary Service.
 */

public interface EVSQuery extends Serializable{

	/**
	 * Generates a DefaultMutableTreeNode for the specified rootName <br>
	 * Eeach node is a DescLogicConcept instance
	 * @param vocabularyName
	 * @param rootName - RootNode of the tree
	 * @param direction - a boolean value (true if traverse down)
	 * @param isaFlag - a boolean value (true if relationship is child)
	 * @param attributes - Sets the AttributeSetDescriptor value for the root node. 
	 * @param levels - the depth of the tree
	 * @param roles - the names of role relationships
	 */
	public void getTree(String vocabularyName, String rootName, boolean direction,
                        boolean isaFlag, int attributes, String levels, Vector roles);

    /**
     * Generates a DefaultMutableTreeNode for the specified rootName <br>
     * Eeach node is a DescLogicConcept instance
     * @param vocabularyName
     * @param rootName - RootNode of the tree
     * @param direction - a boolean value (true if traverse down)
     * @param isaFlag - a boolean value (true if relationship is child)
     * @param attributes - Sets the AttributeSetDescriptor value for the root node. 
     * @param levels - the depth of the tree
     * @param roles - the names of role relationships
     */
    public void getTree(String vocabularyName, String rootName, boolean direction,
                        boolean isaFlag, int attributes, int levels, Vector roles);

	/**
	 * Search DescLogicConcepts
	 * @param vocabularyName - Specifies the namespace
	 * @param searchTerm - Specifies the search term
	 * @param limit - Specifies the maximum limit of the search results
	 */
	public void searchDescLogicConcepts(String vocabularyName, String searchTerm, int limit);

	/**
	 * Performs a search for a DescLogicConcept
	 * @param vocabularyName
	 * @param searchTerm  - Specifies the search term
	 * @param limit		  - Specifies the maximum limit of search results
	 * @param matchOption - Specifies the match option
	 * 						Example: option values 		<br>
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

	public void searchDescLogicConcepts(String vocabularyName, String searchTerm, int limit, int matchOption, String matchType, int ASDIndex) throws Exception;


	/**
	 * Gets all the concept names that matches the argument pattern of a property.
	 * @param vocabularyName - specifies the namespace
	 * @param propertyName - all returned concepts should have the value of this Property
	 * @param propertyValue - pattern for searching
	 * @param matchLimit - number of records to get
	 */
	public void getConceptWithPropertyMatching(String vocabularyName, String propertyName, String propertyValue, int matchLimit);

	/**
	 * Gets all the concept names from the Silos that matches the  pattern.
	 * @param vocabularyName - specifies the namespace
	 * @param searchTerm - specifies the search string
	 * @param matchLimit - number of records to get
	 */
	public void getConceptWithSiloMatching(String vocabularyName, String searchTerm, int matchLimit);

    /**
     * Gets all the concept names from the Silos that matches the  pattern.
     * @param vocabularyName - specifies the namespace
     * @param searchTerm - specifies the search string
     * @param matchLimit - number of records to get
     * @param siloName - specifies the name of the Silo
     */
    public void getConceptWithSiloMatching(String vocabularyName, String searchTerm, int matchLimit, String siloName);

    /**
	 * Gets Concept name for the given concept code
	 * @param vocabularyName - specifies the vocabulary
	 * @param conceptCode - specifies the concept code
	 */
	public void getDescLogicConceptNameByCode(String vocabularyName, String conceptCode);

	/**
	 * Checks if the first concept is a subconcept of the second concept
	 * @param vocabularyName - specifies the namespace
	 * @param code1 - the name of the first concept
	 * @param code2 - the name of the second concept
	 */
	public void isSubConcept(String vocabularyName, String code1,String code2);
	/**
	 * Checks if the specified concept is retired or not.
	 * @param vocabularyName
	 * @param code - conceptCode
	 */
	public void isRetired(String vocabularyName, String code);
	/**
	 * Gets the descendant concept codes of the specified conceptCode. The search extends to a final baseline date
	 * starting from an initial baseline date. The search is based on a boolean value(flag).
	 * If the value is true, the method only searches for the active concepts at the final baseline date.
	 * If the value is false, the method searches for all descendant concepts, whether active or retired.
	 *
	 * @param vocabularyName
	 * @param code - concept code
	 * @param flag - boolean value to specify the search type
	 * @param iBaseLineDate - initial baseline date
	 * @param fBaseLineDate - final baseline date
	 */

	public  void getDescendants(String vocabularyName, String code, boolean flag, String iBaseLineDate, String fBaseLineDate);
	/**
	 * Gets the values of a property owned by a concept
	 *
	 * @param vocabularyName
	 * @param conceptName
	 * @param propertyName
	 */

	public void getPropertyValues(String vocabularyName,String conceptName, String propertyName);

	/**
	 * Gets the ancestor concept codes of the specified concept. The search extends to a final baseline
	 * date starting from an initial baseline date. The search is based on a boolean value.
	 * If the value is true, the method only searches for the active concepts at the initial baseline date.
	 * If the value is false, the method searches for all ancestor concepts, whether active or retired.
	 *
	 * @param vocabularyName
	 * @param code
	 * @param flag
	 * @param iBaseLineDate - Initial baseline date "MM/DD/YYYY"
	 * @param fBaseLineDate - Final baseline date "MM/DD/YYYY"
	 */
	public void getAncestors(String vocabularyName, String code, boolean flag, String iBaseLineDate, String fBaseLineDate);

	/**
	 * Gets the codes, or the names, of all subconcepts of the specified concept
	 * @param vocabularyName
	 * @param conceptName - code or the specified concept name
	 * @param inputFlag - a Boolean true - if inputFlag is a concept code; false - if it is a concept name
	 * @param outputFlag - a Boolean true - if returned value is a concept code ; false - if it is a concept name.
	 */

	public void getSubConcepts(String vocabularyName, String conceptName, Boolean inputFlag, Boolean outputFlag);

	/**
	 * Gets the codes (or the names) of all superconcepts of the specified concept.
	 * @param vocabularyName
	 * @param conceptname
	 * @param inputFlag - a Boolean true - if inputFlag is a concept code; false - if it is a concept name
	 * @param outputFlag - a Boolean true - if returned value is a concept code ; false - if it is a concept name.
	 */

	public void getSuperConcepts(String vocabularyName, String conceptname, Boolean inputFlag, Boolean outputFlag);

	/**
	 * Gets the roles owned by the specified concept.
	 * @param vocabularyName
	 * @param conceptName
	 */

	public void getRolesByConceptName(String vocabularyName,String conceptName);

	/**
	 * Gets the names and values of properties owned by the specified concept.
	 *
	 * @param vocabularyName
	 * @param conceptName
	 */

	public void getPropertiesByConceptName(String vocabularyName,String conceptName);


    /**
     * Gets vocabulary names from EVS
     *
     */
    public void getVocabularyNames();



    /**
     * Gets the code of the specified concept.
     *
     * @param vocabularyName
     * @param conceptName
     */
	public void getConceptCodeByName(String vocabularyName, String conceptName);

	/**
	 * Gets a DescLogicConcept for the specified conceptName.
	 * @param vocabularyName
	 * @param conceptName
	 *
	 */
	public void getConceptByName(String vocabularyName, String conceptName);

	/**
	 * Gets the host of a vocabulary server
	 *
	 * @param vocabularyName
	 */

	public void getVocabularyHost(String vocabularyName);
	/**
	 * Gets the port of a vocabulary server
	 *
	 * @param vocabularyName
	 */

	public void getVocabularyPort(String vocabularyName);

    /**
     * Returns a list of versions for the given vocabulary in the knowledgebase.
     * @param vocabularyName
     */
    public void getVocabularyVersion(String vocabularyName);
	/**
	 * Gets all EditAction of the specified concept. A EditAction contains a edit date
	 * and an editing action. The method searches for all editing actions and the corresponding
	 * editing dates on the specified concept.
	 *
	 * @param vocabularyName
	 * @param code
	 */
	public void getConceptEditAction(String vocabularyName, String code);

	/**
	 * Gets EditActionDate that match with the given concept code and edit action.
	 *
	 * @param vocabularyName
	 * @param code - concept code
	 * @param action - edit action
	 */

	public void getConceptEditActionDates(String vocabularyName, String code, String action);

	/**
	 * Retrieves all root DescLogicConcept objects
	 * @param vocabularyName
	 * @param allAttributes - true, if all attributes are retrieved; false, if no attribute is retrieved.
	 */

	public void getRootConcepts(String vocabularyName, boolean allAttributes);

	/**
	 * Searches for concepts in the specified MetaThesaurus source. If the concept code is used as the searchTerm, the cui boolean value should be true
	 * @param searchTerm - CUI or search string
	 * @param limit - maximum records
	 * @param source - source abbreviation
	 * @param cui - a boolean value - true, if the searchTerm is CUI, - false, otherwise
	 * @param shortResponse - a boolean value
	 * @param score - a boolean value
	 */

	public void searchMetaThesaurus(String searchTerm, int limit, String source,
									boolean cui, boolean shortResponse, boolean score);

	/**
	 * Search concepts in MetaThesaurus
	 * @param cui - Concept Unique Identifier
	 */

	public void searchMetaThesaurus(String cui);

	/**
	 * Search the specified vocabulary for a given code
	 * @param code - Concepts unique identifier within the given source
	 * @param sourceAbbr - specifies the source abbreviation
	 * @deprecated - The preferred method is searchSourceByCode
	 */

	public void searchByLoincId(String code, String sourceAbbr);

	/**
	 * Search the specified vocabulary for a given code
	 * @param code - Concepts unique identifier within the given source
	 * @param sourceAbbr - specifies the source abbreviation	 *
	 */

	public void searchSourceByCode(String code, String sourceAbbr);
	/**
	 * Gets all semantic types from metaphrase
	 */

	public void getSemanticTypes();

	/**
	 * Gets MetaThesaurusConcepts for the specified source
	 * @param sourceAbbr - specifies the source abbreviation
	 * @depricated - Vocabulary Sources hosts large number of concept information. This method retrieves all the concepts from a Source. If all the concepts were not returned prior a time limit specified on the server this method tends to die.
	 */

	public void getConceptsBySource(String sourceAbbr);



	/**
	 * gets MetaThesaurusConcept name
	 * @param conceptCode - specifies the concept code
	 */
	public void getMetaConceptNameByCode(String conceptCode);


	/**
	 * Get all MetaThesaurus Sources
	 */
    public void getMetaSources();

    /**
     * Gets all the child MetaThesaurusConcepts for a given conncept in the specified source
     * @param conceptCode - specifies the concept code
     * @param sourceAbbr - specifies the source abbreviation
     */
    public void getChildren(String conceptCode, String sourceAbbr);

    /**
     * Gets all the Parent MetaThesaurusConcepts for a given concept from the specified source.
     * The relationship between these concepts are of type "PAR" (Parent)
     * @param conceptCode - specifies the concept code
     * @param sourceAbbr - specifies the source abbreviation
     */
    public void getParent(String conceptCode, String sourceAbbr);

    /**
     * Gets all the broader MetaThesaurus concepts for a given concept
     * The relationship between these concepts are of type "RB" (Related Broader)
     * @param conceptCode - specifies the concept code
     */
    public void getBroaderConcepts(String conceptCode);

    /**
     * Gets all the broader MetaThesaurusConcepts for a given concept from the specified source.
     * The relationship between these concepts are of type "RB" (Related Broader)
     * @param conceptCode - specifies the concept code
     * @param sourceAbbr - specifies the source abbreviation
     */
    public void getBroaderConcepts(String conceptCode, String sourceAbbr);


    /**
     * Gets narrower MetaThesaurus concepts for the specified concept
     * The relationship between these concepts are of type "RN" (Related Narrower)
     * @param conceptCode - specifies the concept code
     */
    public void getNarrowerConcepts(String conceptCode);


    /**
     * Gets narrower MetaThesaurus concepts for the specified concept from a given source
     * The relationship between these concepts are of type "RN" (Related Narrower)
     * @param conceptCode - specifies the concept code
     */
    public void getNarrowerConcepts(String conceptCode, String sourceAbbr);


    /**
     * Gets all related metathesaurus concepts for a given metaconcept
     * @param conceptCode - specifies the concept code
     */
    public void getRelatedConcepts(String conceptCode);


    /**
     * Gets all the related metathesaurus concepts for a given metaconcept from the specified source
     * @param conceptCode - specifies the concept code
     * @param sourceAbbr - specifies the source abbreviation
     */
    public void getRelatedConcepts(String conceptCode, String sourceAbbr);

    /**
     * Gets the related metathesaurus concepts for a given metaconcept from the specified source
     * @param conceptCode - specifies the concept code
     * @param sourceAbbr - specifies the source abbreviation
     * @param relation - Specifies the relation of a second concept to the first concept <br>
     * 	RB - Related Broader, RN - Related Narrower, RO -  Related Other, RL - Related Alike,<br>
     *  PAR - Parent, CHD - Child, SIB - Sibling, AQ - Allowed Qualifier (MeSH only),
     *  QB - Qualified By (MeSH Only)
     */
    public void getRelatedConcepts(String conceptCode, String sourceAbbr, String relation);

    /**
     * Gets the concepts of specified category  for the given metathesaurus concept
     * @param conceptCode - specifies the concept code
     * @param category - one of "Medications", "Procedures", "Laboratory", "Diagnosis"
     */
    public void getConceptsByCategories(String conceptCode, String category);

    /**
	 * Checks if the specified concept has the specified inverse role and role value.
	 * @param vocabularyName - Specifies the namespace
	 * @param roleName - role name
	 * @param roleValue - role value
	 * @param conceptName - concpet name
	 */
    public void containsInverseRole(String vocabularyName,String roleName, String roleValue, String conceptName);

    /**
	 * Checks if the specified concept has the specified role and role value.
	 * @param vocabularyName - Specifies the namespace
	 * @param roleName - role name
	 * @param roleValue - role value
	 * @param conceptName - concept name
	 */
    public void containsRole(String vocabularyName,String roleName, String roleValue, String conceptName);


    /**
	 * Gets all associationTypes from a given namespace
	 * @param vocabularyName - Specifies the namespace
	 */
    public void  getAllAssociationTypes(String vocabularyName);

    /**
	 * Retrievs the name of all concecpt association quailifer types from the specified namespace
	 * @param vocabularyName - Specifies the namespace
	 */
    public void getAllConceptAssociationQualifierTypes(String vocabularyName);

    /**
	 * Retrievs the name of all concecpt association types from the specified namespace
	 * @param vocabularyName - Specifies the namespace
	 */
    public void getAllConceptAssociationTypes(String vocabularyName);

    /**
	 * Retrievs the name of all concecpt property qualifier types from the specified namespace
	 * @param vocabularyName - Specifies the namespace
	 */
    public void getAllConceptPropertyQualifierTypes(String vocabularyName);

    /**
	 * Retrievs the name of all concecpt property types from the specified namespace
	 * @param vocabularyName - Specifies the namespace
	 */
	public void getAllConceptPropertyTypes(String vocabularyName);

	/**
	 * Gets all licenses from the specified namespace
	 * @param vocabularyName - Specifies the namespace
	 * @param condition - Specifies the value for a condition parameter
	 */
	public void getAllLicenses(String vocabularyName,String condition);

	/**
	 * Gets all the property types from a specified namespace
	 * @param vocabularyName - specfifies the namespace
	 */
	public void getAllPropertyTypes(String vocabularyName);

	/**
	 * Gets all the Silos from a specified namespace
	 * @param vocabularyName - specfifies the namespace
	 */
	public void getAllSilos(String vocabularyName);

	/**
	 * Gets all qualifier types from the specified namespace
	 * @param vocabularyName - Specifies the namespace
	 */
	public void getAllQualifierTypes(String vocabularyName);

	/**
	 * Gets all the role names from the specified namespace
	 * @param vocabularyName - Specifies the namespace
	 */
	public void getAllRoleNames(String vocabularyName);


    /**
	 * Gets all the sub concept codes for the specified concept from a given namespace
	 * @param vocabularyName - Specifies the namespace
	 * @param conceptCode - Specifies the concept code
	 */
	public void getAllSubConceptCodes(String vocabularyName, String conceptCode);


    /**
	 * Gets all the sub concept names for the specified concept from a given namespace
	 * @param vocabularyName - Specifies the namespace
	 * @param conceptName - Specifies the concept name
	 */
	public void getAllSubConceptNames(String vocabularyName, String conceptName);


    /**
	 * Gets all synonym types from a given namespace
	 * @param vocabularyName - Specifies the namespace
	 */

	public void getAllSynonymTypes(String vocabularyName);


    /**
	 * Gets all the term-association qualifier types from a given namespace
	 * @param vocabularyName - Specifies the namespace
	 */
	public void getAllTermAssociationQualifierTypes(String vocabularyName);


    /**
	 * Gets all the term-property qualifier types from a given namespace
	 * @param vocabularyName - Specifies the namespace
	 */

	public void getAllTermPropertyQualifierTypes(String vocabularyName);

	 /**
	 * Gets all the term-property types from a given namespace
	 * @param vocabularyName - Specifies the namespace
	 */
	public void getAllTermPropertyTypes(String vocabularyName);

	 /**
	 * Gets a parent DescLogicConcept for a given concept name
	 * @param vocabularyName - Specifies the namespace
	 * @param conceptName - Specifies the concept name
	 * @param inputFlag		 - If the input parameter is a concpeName inputFlag should be false <br>
	 * 						   If the input parameter is a conceptCode this value should be true. <br>
	 */
	public void getParentConcepts(String vocabularyName, String conceptName, boolean inputFlag);

	 /**
	 * Gets a child DescLogicConcept for a given concept name
	 * @param vocabularyName - Specifies the namespace
	 * @param conceptName - Specifies the concept name
	 * @param inputFlag		 - If the input parameter is a concpeName inputFlag should be false <br>
	 * 						   If the input parameter is a conceptCode this value should be true. <br>
	 */
	public void getChildConcepts(String vocabularyName, String conceptName, boolean inputFlag);

	 /**
	 * Returns true if the specified concept has a parent concept
	 * @param vocabularyName - Specifies the namespace
	 * @param conceptName - Specifies the concept name
	 */
	public void hasParents(String vocabularyName, String conceptName);


	 /**
	 * Returns true if the specified concept has a child concept
	 * @param vocabularyName - Specifies the namespace
	 * @param conceptName - Specifies the concept name
	 */
	public void hasChildren(String vocabularyName, String conceptName);

	 /**
	 * Gets the properties of a given concept from the specified namespace
	 * @param vocabularyName - Specifies the namespace
	 * @param conceptName - Specifies the concept name
	 */
	public void fetchDTSProperties(String vocabularyName, String conceptName);


	 /**
	 * Fetchs term association data for a given concept from the specified namespace
	 * @param vocabularyName - Specifies the namespace
	 * @param conceptName - Specifies the concept name
	 */
    public void fetchTermAssociations(String vocabularyName, String conceptName);

    /**
     * Gets the concept name for the specified concept code
     * @param vocabularyName - specifies the namespace
     * @param conceptCode - specifies the concept code
     * @deprecated - The preferred method is getDescLogicConceptNameByCode
     */
    public void getConceptNameByCode(String vocabularyName, String conceptCode);

    /**
     * Gets a DescLogicConcept for a valid conceptName or concept code
     * @param conceptName - Specifies the conceptName or concept code
     * @param inputFlag - If a concept code is entered this value is a true. If a concept name has been entered this value should be false.
     */
    public void getDescLogicConcept(String vocabularyName, String conceptName, boolean inputFlag);
    
    /**
     * Gets List of HistoryRecords for a given concept code  
     * @param vocabularyName
     * @param conceptCode
     */
    public void getHistoryRecords(String vocabularyName, String conceptCode);
    /**
     * Gets List of HistoryRecords
     * @param vocabularyName
     * @param initialDate
     * @param finalDate
     * @param conceptCode
     */
    public void getHistoryRecords(String vocabularyName, String initialDate, String finalDate, String conceptCode);
    /**
     * Gets List of HistoryRecords
     * @param vocabularyName
     * @param initialDate
     * @param finalDate
     */
    public void getHistoryRecords(String vocabularyName, String initialDate, String finalDate);
}
