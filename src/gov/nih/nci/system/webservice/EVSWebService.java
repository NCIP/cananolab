
package gov.nih.nci.system.webservice;
import gov.nih.nci.common.net.Request;
import gov.nih.nci.common.net.Response;
import gov.nih.nci.common.util.PrintUtils;
import gov.nih.nci.evs.query.EVSQuery;
import gov.nih.nci.evs.query.EVSQueryImpl;
import gov.nih.nci.system.proxy.*;
import gov.nih.nci.system.servicelocator.ServiceLocator;
import gov.nih.nci.evs.domain.*;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import java.util.*;

import javax.swing.tree.DefaultMutableTreeNode;

import org.apache.log4j.Logger;

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
 * @author Shaziya Muhsin
 */

/**
 * The EVSWebServices class describes methods implemented to query the Enterprise Vocabulary Service. 
 */

public class EVSWebService {
	
    private static Logger log = Logger.getLogger(ServiceLocator.class.getName());
	private String defaultVocabulary = "NCI_Thesaurus";
	private int defaultLimit = 100;
    private Properties evsProperties = new Properties();
    private String evsFileName = "evsBeans.properties";

    /**
     * Constructor that instanciates an EVSWebService instance and loads the EVS properties file.
     * @throws Exception
     */
	public EVSWebService() throws Exception{
        loadProperties(evsFileName);
		}
	
    /**
     * Gets results for the specified EVSQuery
     * @param evsQuery
     * @return
     * @throws Exception
     */
		
        private List query(EVSQuery evsQuery) throws Exception{
		List results = new ArrayList();
		try{
			
			Request request = new Request(evsQuery);
			request.setDomainObjectName(evsQuery.getClass().getName());
			LocalProxy server = new LocalProxy(); 	  	    	
			results = (List)((Response)server.query(request)).getResponse();			
		} 
		catch(Exception ex){
			ex.printStackTrace();
			throw new Exception(ex.getMessage());
			}
		return results;
		}
	

        /**
         * Returns DescLogicConcepts for a given criteria 
         * @param parameterList - specifies the vocabularyName (String) and limit (int)
         * @param dlc specifies the DescLogicConcept
         * @return
         * @throws Exception
         */
	private List getDescLogicConcepts(String parameterList, DescLogicConcept dlc) throws Exception{
        List results = new ArrayList();
        List concepts = new ArrayList();
        String returnClassName = null;
        int limit = 0;
        String vocabularyName = null;
        if(parameterList.indexOf(",")>1){
            StringTokenizer st = new StringTokenizer(parameterList,",");
            returnClassName = st.nextToken();            
            vocabularyName = st.nextToken();
            
            if(st.hasMoreTokens()){
                limit = Integer.valueOf(st.nextToken());
            }
            
            
        }
        else if(parameterList != null){
            returnClassName = parameterList;
        }
        
        if(vocabularyName != null){
            defaultVocabulary = vocabularyName;            
        }
        if(limit >0){
            defaultLimit = limit;
        }
    
       concepts = getDescLogicConcepts(dlc, defaultVocabulary, defaultLimit);
	
        if(concepts.size()>0){
            if(returnClassName.endsWith("MetaThesaurusConcept")){            
                results = convertConcepts(concepts); 
            }
            else if(returnClassName.endsWith("HistoryRecord") || returnClassName.endsWith("History")){
                results = getHistoryRecord(concepts);
            }
            else{
                results = concepts;
            }          
        }
        
        return results;
    }
    
    /**
     * Gets HistoryRecords for one or more DescLogicConepts specified in the list
     * @param concepts
     * @return
     * @throws Exception
     */
    private List getHistoryRecord(List concepts) throws Exception{
        List results = new ArrayList();
        for(int i=0; i<concepts.size(); i++){
            DescLogicConcept result = (DescLogicConcept)concepts.get(i);
            String code = result.getCode();
            EVSQuery evsQuery = new EVSQueryImpl();
            
            evsQuery.getHistoryRecords(defaultVocabulary, code);           
            List historyList = query(evsQuery);
            if(historyList.size() > 0){
                for(int h=0; h< historyList.size(); h++){
                    results.add(historyList.get(h));
                }                
            }
        }
        return results;
    }
    
    
    /**
     * This method  converts a DescLogicConcept to a matching MetaThesaurusConcept or 
     * a MetaThesaurusConcept to a matching DescLogicConcept based on the conceptList. 
     * @param concepts List of DescLogicConcepts or a List of MetaThesaurusConcepts
     * @return
     * @throws Exception
     */
    private List convertConcepts(List concepts) throws Exception{
        List convertedResults = new ArrayList();
        Set results = new HashSet();
        if(concepts.get(0).getClass().getName().endsWith("DescLogicConcept")){
            for(int i=0; i< concepts.size(); i++){
                DescLogicConcept concept =  (DescLogicConcept)concepts.get(i);
                
                String metaConceptCode =  null;
                Vector properties = concept.getPropertyCollection();
                for(int p=0; p< properties.size(); p++){
                    Property property = (Property) properties.get(p);                    
                    if(property.getName().equals("NCI_META_CUI") ||property.getName().equals("UMLS_CUI")){
                                metaConceptCode =  property.getValue();
                                break;
                    }
                }
                EVSQuery metaSearch = new EVSQueryImpl();
                if(metaConceptCode!=null){                    
                    metaSearch.searchMetaThesaurus(metaConceptCode); 
                    try{
                        List metaConcept = query(metaSearch);
                        if(metaConcept.size()>0){
                            results.add((MetaThesaurusConcept)metaConcept.get(0));
                        }
                    }catch(Exception ex){}
                    
                }
                else{
                    EVSQuery q = new EVSQueryImpl();
                    q.getMetaSources();
                    List nciSource = new ArrayList();
                    List sourceList = new ArrayList();
                    sourceList = query(q);                    
                    
                    
                    for(int s=0; s< sourceList.size(); s++){
                        Source source = (Source) sourceList.get(s);
                        if(source.getAbbreviation().startsWith("NCI")){
                            nciSource.add(source.getAbbreviation());
                        }
                    }
                    for(int x=0; x<nciSource.size(); x++){
                        String code = (String) nciSource.get(x);  
                        
                        metaSearch.searchSourceByCode(concept.getCode(),code);
                        try{
                            List metaConcept = query(metaSearch);
                            if(metaConcept.size()>0){
                                results.add((MetaThesaurusConcept)metaConcept.get(0));
                            }
                        }catch(Exception ex){
                            log.info("Exception: "+ ex.getMessage());
                        }
                        
                       
                    }                    
                  
                }
               
            }
        }
        else if(concepts.get(0).getClass().getName().endsWith("MetaThesaurusConcept")){
            for(int i=0; i< concepts.size(); i++){
                MetaThesaurusConcept mtc = (MetaThesaurusConcept)concepts.get(i);
                String code = mtc.getCui();
                List dlcConcept = new ArrayList();
                List match = new ArrayList();
                if(code != null){
                    EVSQuery evsQuery = new EVSQueryImpl();
                    evsQuery.searchDescLogicConcepts(defaultVocabulary, code, 1, 2, "NCI_META_CUI",1);
                    dlcConcept = query(evsQuery);
                    if(dlcConcept.size()<1){
                        evsQuery = new EVSQueryImpl();
                        evsQuery.searchDescLogicConcepts(defaultVocabulary, code, 1, 2, "UMLS_CUI",1);
                        dlcConcept = query(evsQuery);
                    }                   
                    if(dlcConcept.size()<1){
                        ArrayList atomList =  mtc.getAtomCollection();
                        for(int a=0; a<atomList.size(); a++){
                            Atom atom = (Atom) atomList.get(a);
                            Source source = atom.getSource();
                           
                            if(source.getAbbreviation().startsWith("NCI")){
                                String atomCode = atom.getCode();                           
                                evsQuery = new EVSQueryImpl();
                                evsQuery.getDescLogicConcept(defaultVocabulary, atomCode, true);                                
                                try{
                                    match = query(evsQuery);
                           
                                }catch(Exception ex){
                                    log.error("Invalid concept" + atomCode);
                                }                                
                                if(match.size()>0){
                                    dlcConcept.add(match.get(0));
                                }
                            }
                        }
                    }
                    if(dlcConcept.size()>0){
                        results.add((DescLogicConcept)dlcConcept.get(0));
                     }
                }        
            }
        }
        if(results.size()>0){
            for(Iterator i=results.iterator(); i.hasNext();){
               convertedResults.add(i.next());
            }
        }
        return convertedResults;
    }
    
    

    /**
     * Returns DescLogicConcepts for a given criteria
     * @param dlc - DescLogicConcept 
     * @param vocabulary - specifies the vocabularyName
     * @param limit - specifies the search limit
     * @return
     * @throws Exception
     */
      
     
	private List getDescLogicConcepts(DescLogicConcept dlc,String vocabulary, int limit) throws Exception{
	    
		List results = new ArrayList();
		String conceptName = null;
		String conceptCode = null;
		String vocab = null;
		int maxLimit = limit;
		int matchOption = 0;
		String matchType = "";
		String searchValue = null;
		
		if(dlc.getName()!=null && dlc.getName().length()>0){
			conceptName = dlc.getName();
			}
		if(dlc.getCode()!=null && dlc.getCode().length()>0){
			conceptCode = dlc.getCode();
			}
		
		if(vocabulary == null || vocabulary.length() < 1){
			vocab = defaultVocabulary;
			}
		else{
			vocab = vocabulary;
			}
		if(limit == 0){
			maxLimit = defaultLimit;
			}
		
		
		if(dlc.getPropertyCollection().size() > 0){
			
			Property property = (Property) dlc.getPropertyCollection().get(0);
			if(property.getName()!=null && property.getName().length()>0){
				matchOption = 2;
				matchType = property.getName();				
				if(property.getValue()!= null && property.getValue().length()>0){
					searchValue = property.getValue();
					}
				}
			
			}
			
		else if(dlc.getRoleCollection().size()>0){
				Role role = (Role)dlc.getRoleCollection().get(0);
				if(role.getName()!=null && role.getName().length() >0){
					matchOption = 1;
					matchType = role.getName();
					if(role.getValue()!= null || role.getValue().length()>0){
						searchValue = role.getValue();
						}
					}
				}
		
		EVSQuery evsQuery = new EVSQueryImpl();
		
			if(conceptCode!=null){
				evsQuery.getDescLogicConcept(vocab, conceptCode,true);
			}
			else if(conceptName!=null && matchOption == 0){
				evsQuery.searchDescLogicConcepts(vocab,conceptName,maxLimit);				
				} 
			else if(conceptName == null && searchValue != null){
				
				evsQuery.searchDescLogicConcepts(vocab, searchValue, maxLimit, matchOption, matchType,1);
				}						
			else{
				throw new Exception("Please specify search term or concept code");
				}
		
		results = query(evsQuery);
        return results;
		}
	
    /**
     * Returns a List of MetaThesaurusConcepts for a given criteria
     * @param metaConcept- MetaThesaurusConcept
     * @return
     * @throws Exception
     */
	private List getMetaThesaurusConcepts(MetaThesaurusConcept metaConcept) throws Exception{
		String conceptName = null;
		String conceptCode = null;
		List sourceList = new ArrayList();
		String source = null;
		List atomList = new ArrayList();
		try{
				conceptName = metaConcept.getName();
				conceptCode = metaConcept.getCui();
		
				if( metaConcept.getSourceCollection()!= null){
				    sourceList = metaConcept.getSourceCollection();
					if(sourceList.size()>0){
					    source = ((Source) sourceList.get(0)).getAbbreviation();
						if(source.length() == 0){
							source = "*";
							}
					    }
					else{
					    source = "*";
						}
					}
				else{
				    source = "*";
				    }
		}catch(Exception e){			
			throw new Exception("getMetaThesaurus method throws Exception ="+ e.getMessage());
			}
		
		int limit = 100;
		EVSQuery evsQuery = new EVSQueryImpl();
		List results = new ArrayList();
		
		try{
			if(conceptCode!=null && source == null){
				evsQuery.searchMetaThesaurus(conceptCode);				
				}
			else if(conceptCode != null && source != null){
				evsQuery.searchMetaThesaurus(conceptCode,limit,source,true,false,true);			
				}
			else if(conceptName != null){				
				evsQuery.searchMetaThesaurus(conceptName, limit, source, false,false,true);
				}
			else if(conceptName == null && conceptCode == null && source != null){
			    //log.info("Perform - Source specific search");
			    
			    if(metaConcept.getAtomCollection()!=null){
			        atomList = metaConcept.getAtomCollection();
		
			        if(atomList.size()>0){
			            Atom atom = (Atom)atomList.get(0);
		
			            if(atom.getCode().length()>0){
			                evsQuery.searchSourceByCode(atom.getCode(), source);
			                }
			            }
			        }
			}else{
				throw new Exception ("You need to specify either a concept name, code or an Atom code along with the source abbreviation");
				}
			results = query(evsQuery);
			}catch(Exception e){
				throw new Exception(e.getMessage());
				}
           return results;
		
		}
	
    
    /**
     * Returns a list of DescLogicConcepts or MetaThesaurusConcepts beased on a given search criteria
     * @param parameterList - Optional parameter that specifies a vocabularyName (String) and limit(int)
     * @param wsObject - DescLogicConcept or a MetaThesaurusConcept instance from gov.nih.nci.evs.domain.ws package. 
     * @param startIndex - Starting record number of the result set
     * @param recordCounter - Total number of records to be returned
     * @return
     * @throws Exception
     */
      public List evsSearch(String parameterList, Object wsObject, int startIndex, int recordCounter) throws Exception{
           Object searchObject = null;                      
                if(wsObject.getClass().getName().indexOf(".ws.")>0){
                    searchObject = this.convertWStoEVS(wsObject);   
                    if(wsObject.getClass().getName().endsWith("DescLogicConcept")){                         
                        if(getVocabularyName(wsObject)!=null){
                            defaultVocabulary = getVocabularyName(wsObject);
                        }
                    }
                }
                else{
                    searchObject = wsObject;
                }
                String limit = String.valueOf(defaultLimit);
                if(recordCounter > 0 ){
                    int total = startIndex + recordCounter;
                    limit = String.valueOf(total);                    
                }
                
                if(parameterList.indexOf(",")>0){
                   
                    String[] params = parameterList.split(",");
                    
                    if(params.length == 2){
                        parameterList += ","+ limit;
                    }                    
                }
                else{
                    parameterList += "," + defaultVocabulary +","+ limit;
                }
               
            List results = new ArrayList();
            List evsResults = evsSearch(parameterList, searchObject);
            List finalResults = new ArrayList();
            if(wsObject.getClass().getName().indexOf(".ws.")>0){
                for(int i=0; i< evsResults.size(); i++){
                    Object wsResult = convertEVStoWS(evsResults.get(i));
                    if(wsResult.getClass().getName().endsWith("DescLogicConcept")){
                        setVocabularyName(wsResult);
                    }
                    results.add(wsResult);
                }
            }
            else{
                results = evsResults;
            }
            
                if(recordCounter == 0){
                    recordCounter = results.size();
                }
                
               for(int i= startIndex; i< (startIndex + recordCounter) && i< results.size(); i++){
                   finalResults.add(results.get(i));
               } 
            
            return finalResults;
        }
        
      
      /**
       * Returna list of EVS domain objects based on a given criteria
       * @param parameterList - specifies the vocabulayName and limit
       * @param searchObject - an instance of a DescLogicConcept or an instance of a MetaThesaurusConcept
       * @return
       * @throws Exception
       */
	public List evsSearch(String parameterList, Object searchObject) throws Exception{
        
		List results = new ArrayList();
		String className = searchObject.getClass().getName();
        String returnClassName = null;
       
        if(parameterList != null && parameterList.indexOf(",")<1){
            returnClassName = parameterList;
        }
        else{
            returnClassName = parameterList.substring(0,parameterList.indexOf(","));
        }
       
		
		try{
			if(className.endsWith("DescLogicConcept") ){		
				results = getDescLogicConcepts(parameterList,(DescLogicConcept)searchObject);
                }
			else if(className.endsWith("MetaThesaurusConcept")){
				List metaConcepts = getMetaThesaurusConcepts((MetaThesaurusConcept) searchObject);
                if(returnClassName != null && returnClassName.endsWith("DescLogicConcept")){
                    results = convertConcepts(metaConcepts);
                }
                else{
                    results = metaConcepts;
                }
				}
			else{
				    throw new Exception ("Invalid search class " + className);
				}
			
		}catch(Exception e){
			    throw new Exception (e.getMessage());
			}
		
		return results;
		}
	
	/**
     * Converts an instance of a class defined in a gov.nih.nci.evs.domain package to gov.nih.nci.evs.domain.ws package instance. 
     * @param evsObject - an instance of a DescLogicConcept or MetaThesaurusConcept defined in the gov.nih.nci.evs.domain package 
     * @return
     * @throws Exception
	 */
    public Object convertEVStoWS(Object evsObject)throws Exception{
        
        Object wsObject = null;
        String wsPackageName = null;
        String wsBeanName = evsObject.getClass().getName();
        if(wsBeanName.indexOf(".")>0){
            wsPackageName = wsBeanName.substring(0, wsBeanName.lastIndexOf("."));
            wsBeanName = wsBeanName.substring(wsBeanName.lastIndexOf(".")+1);
            
        }
        String wsClassName = null;
        if(wsPackageName != null){
             wsClassName = wsPackageName +".ws."+ wsBeanName;
        } 
       
        if(wsClassName != null){
            wsObject = getPopulatedWSObject(evsObject, Class.forName(wsClassName).newInstance());
        }  
        
               
        return wsObject;
    }
    
    /**
     * Populates wsObject with values specified in the evsObject
     * @param evsObject - an instance of a class defined in the gov.nih.nci.evs.domain package 
     * @param wsObject - an instance of a class defined in the gov.nih.nci.evs.domain.ws package
     * @return
     * @throws Exception
     */
    private Object getPopulatedWSObject(Object evsObject, Object wsObject) throws Exception{       
           Field[] fields = evsObject.getClass().getDeclaredFields();
           Field[] wsFields = wsObject.getClass().getDeclaredFields();
           
               for(int f=0; f< wsFields.length; f++){
                   Field wsField = wsFields[f]; 
                   if(wsField.getName().equalsIgnoreCase("vocabularyName")|| wsField.getName().equalsIgnoreCase("serialVersionUID")){
                       continue;
                   }
                   try{
                       Field evsField = getFieldByName(fields, wsField.getName());
                       wsField.setAccessible(true);
                       evsField.setAccessible(true);
                       Object evsFieldValue = null;
                      
                       if(evsField.get(evsObject)!=null){
                           Object value = null;
                           Object wsValue = null;
                           ArrayList wsList = new ArrayList();
                          
                           if(wsField.getType().getName().endsWith("ArrayList")){
                              
                               for(Iterator it = ((Collection)evsField.get(evsObject)).iterator(); it.hasNext();){
                                   value = it.next();
                                   if(value != null){                                       
                                       String evsValueClass = value.getClass().getName();
                                       if(evsValueClass.indexOf(".evs.")>0){
                                           String wsValueClass = evsValueClass.substring(0,evsValueClass.lastIndexOf(".")) + ".ws."+ evsValueClass.substring(evsValueClass.lastIndexOf(".")+1);
                                           wsValue = getPopulatedWSObject(value,Class.forName(wsValueClass).newInstance());   
                                           wsList.add(wsValue);
                                       }
                                       else{
                                           wsList.add(value);
                                       }
                                   }
                                   
                                  }
                              
                               if(wsList.size()>0){
                                   wsField.set(wsObject, wsList);
                               }
                           } else if(wsField.getType().getName().indexOf(".evs.")>0){
                               evsFieldValue = evsField.get(evsObject);
                               if(evsFieldValue != null ){
                                   String evsValueClass = evsFieldValue.getClass().getName();
                                   String wsValueClass = evsValueClass.substring(0,evsValueClass.lastIndexOf(".")) + ".ws."+ evsValueClass.substring(evsValueClass.lastIndexOf(".")+1);                                   
                                   wsValue = this.getPopulatedWSObject(evsFieldValue, Class.forName(wsValueClass).newInstance());                            
                                   if(wsValue != null){                                       
                                       wsField.set(wsObject, wsValue);}
                                   }                   
                               }
                           else{
                               evsFieldValue = evsField.get(evsObject);
                               if(evsFieldValue != null ){
                                   wsField.set(wsObject, evsFieldValue);
                               }                          
                               
                           }
                               
                           }
                                                 
                   }catch(Exception ex){                       
                   }
                 } 
               
               
          return wsObject;
        
    }
    
    /**
     * Converts an object defined in the gov.nih.nci.evs.domain.ws package to an object in the gov.nih.nci.evs.domain package
     * @param wsObject an instance of a class defined in the gov.nih.nci.evs.domain.ws package
     * @return
     * @throws Exception
     */
    private Object convertWStoEVS(Object wsObject)throws Exception{
        
        Object evsObject = null;
        String evsBeanName = wsObject.getClass().getName();
        if(evsBeanName.indexOf(".")>0){
            evsBeanName = evsBeanName.substring(evsBeanName.lastIndexOf(".")+1);
        }
        String evsClassName = null;
        if(evsProperties == null){
            loadProperties(evsFileName);
        }
        if(evsProperties != null){
             evsClassName = this.locateClass(evsBeanName);
        } 
        
        if(evsClassName != null){
            evsObject = getPopulatedEVSObject(wsObject);
        }
        
        return evsObject;
    }
    
    /**
     * Populates and return an instance of a class defined in the  gov.nih.evs.domain package with the values specified in the wsObject
     * @param wsObject - an instance of a class defined in the gov.nih.nci.evs.domain.ws package
     * @return
     * @throws Exception
     */
    private Object getPopulatedEVSObject(Object wsObject) throws Exception{
           
            String evsClassName = null;
            String wsBeanName = wsObject.getClass().getName();
            if(wsBeanName.indexOf(".")>0){
                wsBeanName = wsBeanName.substring(wsBeanName.lastIndexOf(".")+1);                    
            }
            if(evsProperties != null){
                evsClassName = this.locateClass(wsBeanName);
           }
            Object evsObject = Class.forName(evsClassName).newInstance();
           
           if(evsClassName != null){
               Field[] fields = evsObject.getClass().getDeclaredFields();
               Field[] wsFields = wsObject.getClass().getDeclaredFields();
               
               
               for(int f=0; f< wsFields.length; f++){
                   Field wsField = wsFields[f]; 
                   if(wsField.getName().equalsIgnoreCase("serialVersionUID") ||wsField.getName().equalsIgnoreCase("vocabularyName")){
                       continue;
                   }
                   Field evsField = getFieldByName(fields, wsField.getName());
                   wsField.setAccessible(true);
                   evsField.setAccessible(true);
                   Object evsFieldValue = null;
                  
                   if(wsField.get(wsObject)!=null){                       
                       evsFieldValue = wsField.get(wsObject); 
                                           
                       
                       if(wsField.getType().getName().endsWith("ArrayList")){   
                           
                           Vector evsVector = new Vector();
                           HashSet evsHashSet = new HashSet();                       
                           ArrayList evsList = new ArrayList();
                           ArrayList wsList = (ArrayList)wsField.get(wsObject);                           
                               Object value = null;
                               if(wsList.size()>0){   
                                  
                                   for(int l=0; l< wsList.size(); l++){
                                       value = getPopulatedEVSObject(wsList.get(l));     
                                       if(evsField.getType().getName().endsWith("Vector")){
                                           evsVector.add(value);
                                       }
                                       else if(evsField.getType().getName().endsWith("HashSet")){
                                           evsHashSet.add(value);
                                            }
                                       else if(evsField.getType().getName().endsWith("ArrayList")){
                                           evsList.add(value);
                                       }
                                    }
                                 
                                   if(evsField.getType().getName().endsWith("Vector")){
                                       evsField.set(evsObject,evsVector );
                                   }
                                   else if(evsField.getType().getName().endsWith("HashSet")){
                                       evsField.set(evsObject, evsHashSet);
                                   }
                                   else if(evsField.getType().getName().endsWith("ArrayList")){
                                       evsField.set(evsObject, evsList);
                                   }
                                }
                        }                    
                       else{
                           if(wsField.getType().getName().indexOf(".evs.")>0){                        
                               evsFieldValue = this.getPopulatedEVSObject(wsField.get(wsObject));                           
                           }
                           else{                               
                               evsFieldValue = wsField.get(wsObject);                           
                           }
                           if(evsFieldValue != null ){
                              
                               evsField.set(evsObject, evsFieldValue);
                           }
                       }                           
                      }    
                    }
                   }
             
        return evsObject;
    }
    
    /**
     * Returns a Field from a list that has a field name equal to the specified fieldName
     * @param fields - List of Fields
     * @param fieldName - field name
     * @return
     * @throws Exception
     */
    private Field getFieldByName(Field[] fields, String fieldName) throws Exception{
        Field field = null;
        for(int i=0; i<fields.length;i++){
            fields[i].setAccessible(true);
            if(fields[i].getName().equalsIgnoreCase(fieldName)){
                field = fields[i];
            }
        }
        return field;
    }
    
    /**
     * Loads properties from the given file
     * @param file1
     * @throws Exception
     */
    private void loadProperties(String file1) throws Exception{
       
        if(evsProperties.size() <1){
            if(file1 != null){
                try{
                    evsProperties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(file1));                
                }
                catch(Exception ex){
                    log.error("Cannot locate EVS properties file");
                } 
            }
              
        }
    }
    

    /**
     * Locates and returns a fully qualified class name from a property file for a givn bean name 
     * @param beanName - class name
     * @return
     * @throws Exception
     */
    private String locateClass(String beanName) throws Exception{
        String className = null;
        boolean found = false;
        if(evsProperties == null){
            loadProperties(evsFileName);
        }
        if(evsProperties != null){
            for(Iterator i= evsProperties.keySet().iterator(); i.hasNext();){
                String key = (String)i.next();              
                 if(beanName.lastIndexOf(".")>1){
                    if(key.equals(className)){
                        className = key;
                        break;
                    }
                }
                else{
                    if(key.substring(key.lastIndexOf(".")+1).equals(beanName)){
                        className = key;
                        break;
                    }
                }            
            }
            }
        
        return className;  
    }
    
    /**
     * Returns the vocabularyName defined in the wsObject
     * @param wsObject - an instance of a class defined in the gov.nih.nci.evs.domain.ws package
     * @return
     * @throws Exception
     */
    private String getVocabularyName(Object wsObject) throws Exception{
       
        String vocabularyName = null;
        Field field = null;
        if(this.getFieldByName(wsObject.getClass().getDeclaredFields(), "vocabularyName")!=null){
            field = this.getFieldByName(wsObject.getClass().getDeclaredFields(), "vocabularyName");
            if(field.get(wsObject)!=null){
                vocabularyName = (String)field.get(wsObject);
            }
        }
        return vocabularyName;
        
    }
    /**
     * Populates the vocabulayName attribute in a wsObject
     * @param wsObject - an instance of a DescLogicConcept defined in the gov.nih.nci.evs.domain.ws package
     * @throws Exception
     */
    private void setVocabularyName(Object wsObject) throws Exception{
        if(wsObject.getClass().getName().endsWith("DescLogicConcept")){
            if(this.getFieldByName(wsObject.getClass().getDeclaredFields(), "vocabularyName")!=null){
                Field field = this.getFieldByName(wsObject.getClass().getDeclaredFields(), "vocabularyName");
                field.set(wsObject, defaultVocabulary);
            }
        }
    }

	
}
