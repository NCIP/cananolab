package gov.nih.nci.common.util;



import java.util.*;
import java.lang.reflect.*;
import javax.swing.tree.DefaultMutableTreeNode;
import gov.nih.nci.evs.domain.*;
import org.apache.log4j.*;

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
  * PrintUtils consists of methods that prints data to a Standard Output Device.
  * @author caBIO Team
  * @version 1.0
*/


public class PrintUtils {

    private static Logger log= Logger.getLogger(PrintUtils.class.getName());
/**
 	* Prints the objects in a given list along with it's child objects
 	* @param resultList 	Specifies the objects that needs to be printed
 	* return
*/
		public void printTree(List resultList){
			int recordNum = 1;
			int valid = -1;
			List classList;
			for(Iterator rList = resultList.iterator(); rList.hasNext();){

					classList = new ArrayList();
			      	Object result 	= (Object)rList.next();
			      	System.out.println("\n("+recordNum+"). Class name = "+ result.getClass().getName() +"\n");
			      	valid = classList.size();
			      	classList = addClassToList(result, classList);
			      	if(valid!= classList.size()){
						classList = printObject(result, classList);
						classList.remove(classList.size()-1);
						}

			      	recordNum++;
			    }

			String beanName = resultList.get(0).getClass().getName();
			System.out.println("Total number of "+ beanName.substring(beanName.lastIndexOf(".")+1) + " " +resultList.size());

			}


/**
	* Sends and object to a Standard Output Device
	* @param result 	Specifies the object that needs to be printed
	* @param classList	Specifies the path from root to the current object
	* return
*/
		public List printObject(Object result, List classList){

			List methodList 		= new ArrayList();
			List methodForObject 	= new ArrayList();
			List methodForObjects	= new ArrayList();
			Class resultClass 		= result.getClass();
			String className 		= resultClass.getName();
			String attribName 		= null;
			//Field[] fields 			= resultClass.getDeclaredFields();
			//Method[] methods 		= resultClass.getDeclaredMethods();


			Field[] fields 			= getAllFields(resultClass);
			Method[] methods 		= getAllMethods(resultClass);


			int valid = classList.size();



		   for(int f=0;f<fields.length; f++){
			    fields[f].setAccessible(true);
			    String fieldClassName = fields[f].getType().getName();
			    String fieldName = fields[f].getName().substring(0,1).toUpperCase()+fields[f].getName().substring(1);
			    for(int i=0; i< methods.length; i++){
			       String methodName = methods[i].getName();
			             if(methodName.endsWith(fieldName) && methodName.startsWith("get")){

			                  if(fieldClassName.startsWith(resultClass.getPackage().getName())){
			  						methodForObject.add(methods[i]);

			 						}
			 					else if(fieldClassName.endsWith("Collection")){

			 						methodForObjects.add(methods[i]);
			 						String beanName = fieldName.substring(0, fieldName.indexOf("Collection"));
			 						String beanClassName = resultClass.getPackage().getName() + "."+ beanName.substring(0,1).toUpperCase() + beanName.substring(1);

			 						}
			 					else{
			 						methodList.add(methods[i]);
			 						}
			                  break;
			                 }
			             }
			   }

			   String path = (String)classList.get(0);
			   String tab = "\t";
			   for(int a=1; a<classList.size(); a++){

					   path += "."+ (String)classList.get(a);
					   tab += "\t";
				   }

				System.out.println("Path  = "+ path);

			   for(Iterator iList=methodList.iterator(); iList.hasNext();){
			   		Method meth = (Method)iList.next();
			   		attribName = meth.getName().substring(3);
			   		try{
			   			System.out.println(tab+" * "+attribName +" - "+ meth.invoke(result, new Object[] {}));
			   			 }catch(Exception ex){
			   		     log.error("ERROR: " + ex.getMessage());
			   		 }
			  	}

				for(Iterator iList=methodForObject.iterator(); iList.hasNext();){
				         Method meth = (Method)iList.next();
				         attribName = "role value - "+meth.getName().substring(3) +".id ";
				         try{
							Object obj = meth.invoke(result, new Object[]{});

							if(obj == null){
								System.out.println("Empty " + obj.getClass().getName());
							}
							else{
								classList = addClassToList(obj, classList);
								if(valid!= classList.size()){
									classList = printObject(obj, classList);
									classList.remove(classList.size()-1);
								}
							}
				            }catch(Exception ex){
				                log.error("ERROR: " + ex.getMessage());
				            }
		               }


			   for(Iterator iList=methodForObjects.iterator(); iList.hasNext();){
			   		Method meth = (Method)iList.next();
			   		attribName = " - "+meth.getName().substring(3) +".id ";
			   		try{
						java.util.Collection objList = new ArrayList();
			   			objList = (java.util.Collection)meth.invoke(result,new Object[]{});
			   			System.out.println(tab+"*" +attribName +"  "+  objList.size()+ " found");
			   			Object[] objects = objList.toArray();

			   			List newList = new ArrayList();
			   			for(int i=0; i< objects.length; i++){
							classList = addClassToList(objects[i], classList);
							if(valid!= classList.size()){
								classList = printObject(objects[i], classList);
								classList.remove(classList.size()-1);
								}
							}
			         }catch(Exception ex){
			             log.error("ERROR: " + ex.getMessage());
			         }
				}
				return classList;
		}

/**
	* Adds the current class to a list. This list is used to idetify the path from parent to this class
	* @param result 	Specifies the object
	* @param classList	Specifies the path from root to the current object
	* return
*/
	public List addClassToList(Object result, List classList){

		boolean add = true;
		String className = result.getClass().getName();
		String classBean = className.substring(className.lastIndexOf(".")+1);
		if(classList.size()>1){
			String pathItem = null;
				for(int x=0; x<classList.size(); x++){
				pathItem = (String)classList.get(x);
				//System.out.println("BeanName = "+ classBean +" item = "+ item);

					if(classBean.equalsIgnoreCase(pathItem)){
						add = false;
						break;
					}
				}
		}
		if(add){
			classList.add(className.substring(className.lastIndexOf(".")+1));
			}
		return classList;
		}


/**
 	* Prints the objects in a given list
 	* @param resultList 	Specifies the objects that needs to printed
 	* return
*/
	public void printResults(List resultList){


			int recordNum = 1;
	        for(Iterator rList = resultList.iterator(); rList.hasNext();){
	            Object result 	= (Object)rList.next();
	            Class resultClass = result.getClass();
	            String className = resultClass.getName();
	            if(className.startsWith("java.lang") || className.equalsIgnoreCase("java.util.Date")){
	            	for(int i=0;i <resultList.size(); i++){
	            		System.out.println(resultList.get(i));
	            		}
	            	System.exit(0);
	            	}
	            else if(className.endsWith("DefaultMutableTreeNode")){
	            	printDLTree(resultList);
	            	System.exit(0);
	            	}
	            else{

	            String attribName = null;
	            //Field[] fields = resultClass.getDeclaredFields();
	            //Method[] methods = resultClass.getDeclaredMethods();
	            Field[] fields 			= getAllFields(resultClass);
				Method[] methods 		= getAllMethods(resultClass);
	            List methodList = new ArrayList();
	            List methodForObject = new ArrayList();
	            List methodForObjects= new ArrayList();


	         System.out.println("\n("+recordNum+"). Class name = "+ className +"\n");


	            for(int f=0;f<fields.length; f++){
	                fields[f].setAccessible(true);
	                String fieldClassName = fields[f].getType().getName();
	                String fieldName = fields[f].getName().substring(0,1).toUpperCase()+fields[f].getName().substring(1);

	                for(int i=0; i< methods.length; i++){
	                    String methodName = methods[i].getName();
	                    if(methodName.endsWith(fieldName) && methodName.startsWith("get")){

	                    if(fieldClassName.startsWith(resultClass.getPackage().getName())){
							methodForObject.add(methods[i]);
							}
						else if(fieldClassName.endsWith("Collection")){
							methodForObjects.add(methods[i]);
							}
						else{
							methodList.add(methods[i]);
							}
	                    break;
	                    }
	                }

	            }

	         int count=1;
				  	for(Iterator iList=methodList.iterator(); iList.hasNext();){
				  	   Method meth = (Method)iList.next();
				  	   attribName = meth.getName().substring(3);
				       try{
				           System.out.println("--> "+attribName +" - "+ meth.invoke(result, new Object[]{}));
				  	       count++;
				  		}catch(Exception ex){
				  		  log.error("ERROR: " + ex.getMessage());
				  		}

				  	}
/*
			for(Iterator iList=methodForObject.iterator(); iList.hasNext();){
	                Method meth = (Method)iList.next();
	                attribName = "role value - "+meth.getName().substring(3) +".id ";
	                try{
							Object obj = meth.invoke(result, null);
							if(obj == null){
								}
							else{
							Method objMeth = obj.getClass().getMethod("getId",null);
								if(objMeth == null){
									System.out.println("no such method found for " + obj);
									}
								else{
	                        		count++;
								}
							}

	                    }catch(Exception ex){}
	                }


			for(Iterator iList=methodForObjects.iterator(); iList.hasNext();){
	    	     Method meth = (Method)iList.next();
	    	     attribName = "role value - "+meth.getName().substring(3) +".id ";

	    	    try{
					java.util.Collection objList = new ArrayList();
					objList = (java.util.Collection)meth.invoke(result,null);
					System.out.println("Collection Object " +attribName +"  "+  objList.size()+ " found");
					Object[] objects = objList.toArray();

	    	    }catch(Exception ex){}
			}
	*/
			recordNum++;
	            }
	        }
	            System.out.println("\n\n"+resultList.size()+ " \trecords found");

}

	/**
	 * Gets all the fields of a given class
	 * @param resultClass - Specifies the class name
	 * @return - returns all the fields of a class
	 */
	public Field[] getAllFields(Class resultClass){
		List fieldList = new ArrayList();
		try{

		while(resultClass != null && !resultClass.isInterface() && !resultClass.isPrimitive()){
			Field[] fields = resultClass.getDeclaredFields();
			for (int i = 0; i < fields.length; i++) {
						fields[i].setAccessible(true);
						String fieldName = fields[i].getName();
						fieldList.add(fields[i]);
				}


			if(!resultClass.getSuperclass().getName().equalsIgnoreCase("java.lang.Object")){
				resultClass = resultClass.getSuperclass();
				}
			else{
				break;
				}

			}
		}catch(Exception ex){
		    log.error("ERROR: " + ex.getMessage());
			}

		Field[] fields = new Field[fieldList.size()];
		for(int i=0;i<fieldList.size(); i++){
			fields[i]= (Field)fieldList.get(i);
			}
			return fields;
		}

	/**
	 * Gets all the methods of a given class
	 * @param resultClass - Specifies the class name
	 * @return - Returns all the methods
	 */


	public Method[] getAllMethods(Class resultClass){

		List methodList = new ArrayList();
		try{
		while(resultClass != null && !resultClass.isInterface() && !resultClass.isPrimitive()){
			Method[] method = resultClass.getDeclaredMethods();
			for (int i = 0; i < method.length; i++) {
						method[i].setAccessible(true);
						String methodName = method[i].getName();
						methodList.add(method[i]);
				}


			if(!resultClass.getSuperclass().getName().equalsIgnoreCase("java.lang.Object")){
				resultClass = resultClass.getSuperclass();
				}
			else{
				break;
				}
			}
			}catch(Exception ex){
			    log.error("ERROR: " + ex.getMessage());
			}

			Method[] methods = new Method[methodList.size()];
					for(int i=0;i<methodList.size(); i++){
						methods[i]= (Method)methodList.get(i);
					}
		return methods;
		}


	/**
	 * Prints the specified DescLogicConcept on to the standard output device
	 * @param dlc - specifies the DescLogicConcept object
	 */

	public void printDLC(DescLogicConcept dlc, String tab ){
        
		if(tab == null){
			tab = " ";
			}
		System.out.println("\n"+tab + "Concept Code = "+dlc.getCode()+ "\n"+tab+ "Name = "+ dlc.getName() + "\n" + tab+ "Namespace id = "+ dlc.getNamespaceId());

		Vector propVect = dlc.getPropertyCollection();
		System.out.println(tab + "-->Properties = "+ propVect.size());
		for(int x=0; x<propVect.size(); x++){
			Property p =  (Property) propVect.get(x);
			System.out.println(tab + "\tProperty name = "+ p.getName() +tab + "\t -  Value = "+ p.getValue());
			List qList = new ArrayList();
			qList = p.getQualifierCollection();
			//System.out.println("\n\t\tQualifier count = "+ qList.size());
			for(int q=0;q<qList.size(); q++){
			    Qualifier qualifier = (Qualifier)qList.get(q);
			    System.out.println("\t\tProperty Qualifier name = "+ qualifier.getName()+"\tvalue = "+ qualifier.getValue());
			    }
			}

		Vector assVect = dlc.getAssociationCollection();
		System.out.println(tab + "-->Associations = "+ assVect.size());
		for(int x=0; x<assVect.size(); x++){
			Association a =  (Association) assVect.get(x);
			System.out.println(tab + "\tAssociation name = "+ a.getName() +tab + "\t -  Value = "+ a.getValue());
			List qList = new ArrayList();
			qList = a.getQualifierCollection();
			//System.out.println("\n\t\tQualifier count = "+ qList.size());
			for(int q=0;q<qList.size(); q++){
			    Qualifier qualifier = (Qualifier)qList.get(q);
			    System.out.println("\t\tAssociation Qualifier name = "+ qualifier.getName()+"\tvalue = "+ qualifier.getValue());
			    }
			}
        try{
            if(dlc.getClass().getDeclaredMethod("getInverseAssociationCollection", new Class[]{})!=null){
                Vector inverseAssVect = dlc.getInverseAssociationCollection();
                System.out.println(tab + "-->Inverse Associations = "+ inverseAssVect.size());
                for(int x=0; x<assVect.size(); x++){
                    Association a =  (Association) inverseAssVect.get(x);
                    System.out.println(tab + "\tAssociation name = "+ a.getName() +tab + "\t -  Value = "+ a.getValue());
                    List qList = new ArrayList();
                    qList = a.getQualifierCollection();
                    //System.out.println("\n\tQualifier count = "+ qList.size());
                    for(int q=0;q<qList.size(); q++){
                        Qualifier qualifier = (Qualifier)qList.get(q);
                        System.out.println("\t\tInverse Association Qualifier name = "+ qualifier.getName()+"\tvalue = "+ qualifier.getValue());
                        }
                    }

            }

        }catch(Exception e){}
				
		Vector roleVect = dlc.getRoleCollection();
		System.out.println(tab + "-->Roles = "+ roleVect.size());
		for(int r=0; r<roleVect.size(); r++){
			Role role =  (Role) roleVect.get(r);
			System.out.println(tab + "\tRole name = "+ role.getName() + tab + "\t"+ role.getValue());
			}
        EdgeProperties edge = dlc.getEdgeProperties();
        if(edge != null){
            System.out.println("\t\tEdge Properties-> is-a Relationship : " + edge.getIsA()+"\tLinks :"+edge.getLinks() +"\tName:"+edge.getName()+"\tTraverseDown :");
        }
        TreeNode node = dlc.getTreeNode();
        if(node != null){
            System.out.println("\t\tEdge Properties-> is-a Relationship : " + node.getIsA()+"\tLinks :"+node.getLinks() +"\tName:"+node.getName()+"\tTraverseDown :");
        }
        
		System.out.println(tab + "hasParent  = "+dlc.getHasParents());
		System.out.println(tab + "hasChildren = "+ dlc.getHasChildren());
		System.out.println(tab + "isRetired = "+ dlc.getIsRetired());
        
		}

	/**
	 * Prints the specified DescLogicConcept on to the standard output device
	 * @param dlc - specifies the DescLogicConcept object
	 */

			public void printDLC(DescLogicConcept dlc ){
				printDLC(dlc, null);
				}



	/**
	 * Prints the DescLogicConcepts tree on the Standard Output Device
	 * @param evsResults - Results list
	 */

			public void printDLTree(List evsResults){
				     printDLTree(evsResults, 0);
				}


	/**
	 * Prints the DescLogicConcepts tree on the Standard Output Device
	 * @param evsResults - Specifies the results list that holds the DescLogicConcept tree
	 * @parm levels - Specifies the maximum number of levels to be printed
	*/
			public void printDLTree(List evsResults, int levels){
				int max = levels;
				DefaultMutableTreeNode root = (DefaultMutableTreeNode)evsResults.get(0);
				int count = 1;
				for(Enumeration e = root.preorderEnumeration(); e.hasMoreElements();){
					DefaultMutableTreeNode node = (DefaultMutableTreeNode)e.nextElement();

					String tab = "\t";
					for(int t=2; t<=node.getLevel(); t++){
						tab += "\t";
						}
					if(node.getLevel()<= levels || max == 0){
                        if(node.isRoot()){
                            System.out.println("Node is root >" + node.getLevel()) ;                            
                        }
                        else{System.out.println("LEVEL >>>>> " + node.getLevel());}
						DescLogicConcept dlc = (DescLogicConcept)node.getUserObject();
						printDLC(dlc, tab);
						System.out.println(tab + "Child Nodes = "+ node.getChildCount());
						System.out.println(tab + "Level = "+ node.getLevel());
						System.out.println(tab + "Node count = " + count);
						count++;
						}


					}

				System.out.println("Root has "+ root.getChildCount()+" child nodes");
				System.out.println("Root depth = "+ root.getDepth());
			}

			/**
			 * Prints the contents of the given result list on a standard output device
			 * @param evsResults - Specifies the results that need to be printed
			 */
			public void printEVSResults(List evsResults){
                
                try{
                    if(evsResults.get(0).getClass().getName().endsWith("Boolean")){
                        Boolean value = (Boolean)evsResults.get(0);
                        if(value == null){
                            System.out.println("Not specified");
                            }
                        else{
                            System.out.println("has value  = "+ value.toString());
                            }
                        }
                    else if(evsResults.get(0).getClass().getName().endsWith("DefaultMutableTreeNode")){
                        printDLTree(evsResults);
                        }
                    else if(evsResults.get(0).getClass().getName().endsWith("DescLogicConcept")){
                        for(int i=0; i<evsResults.size(); i++){
                            DescLogicConcept dlc = (DescLogicConcept) evsResults.get(i);
                            printDLC(dlc);
                            }
                        }
                    else if(evsResults.get(0).getClass().getName().endsWith("Vector")){
                        Vector resultsVec = (Vector)evsResults.get(0);
                        for(int res=0; res<resultsVec.size(); res++ ){
                            System.out.println("\t"+resultsVec.get(res));
                            }
                        }
                    else if(evsResults.get(0).getClass().getName().endsWith("MetaThesaurusConcept")){
                        for(int m=0; m<evsResults.size(); m++){
                            MetaThesaurusConcept mtc = (MetaThesaurusConcept)evsResults.get(m);
                            System.out.println("\nConcept code: "+mtc.getCui() +"\n\t"+mtc.getName());
                            List sList =  mtc.getSourceCollection();
                            System.out.println("\tSource-->" + sList.size());
                            for(int y=0; y<sList.size(); y++){
                                Source s = (Source)sList.get(y);
                                System.out.println("\t - "+s.getAbbreviation());
                                }
                            List semanticList = mtc.getSemanticTypeCollection();
                            System.out.println("\tSemanticType---> count ="+ semanticList.size());
                            for(int z=0; z<semanticList.size(); z++){
                                SemanticType sType = (SemanticType) semanticList.get(z);
                                System.out.println("\t- Id: "+sType.getId()+"\n\t- Name : "+sType.getName());
                                }
                            List atomList = mtc.getAtomCollection();
                            System.out.println("\tAtoms -----> count = "+ atomList.size());
                            for(int i=0;i<atomList.size(); i++){
                                Atom atom = (Atom)atomList.get(i);
                                System.out.println("\t -Code: "+ atom.getCode()+" -Name: "+ atom.getName() +" -LUI: "+ atom.getLui()+" -Source: "+ atom.getSource().getAbbreviation());
                                }
                            List synList = mtc.getSynonymCollection();
                            System.out.println("\tSynonyms -----> count = "+ synList.size());
                            for(int i=0; i< synList.size(); i++){
                                System.out.println("\t - "+ (String) synList.get(i));
                                }
                        }
                    }
                    else if(evsResults.get(0).getClass().getName().endsWith("Source")){
                            for(int m=0; m< evsResults.size(); m++){
                            Source source = (Source) evsResults.get(m);
                            System.out.println(m+"\tSource ---> "+ source.getAbbreviation());
                            }
                        }
                    else if(evsResults.get(0).getClass().getName().endsWith("Role")){
                        System.out.println("\tRoles ---> "+evsResults.size());
                        for(int i=0; i< evsResults.size(); i++){
                            Role role = (Role)evsResults.get(i);
                            System.out.println("\t\t"+role.getName() +"\t"+role.getValue());
                            }

                        }
                    else if(evsResults.get(0).getClass().getName().endsWith("Property")){
                        System.out.println("\tProperty ---> " + evsResults.size());
                        for(int i=0; i< evsResults.size(); i++){
                            Property p = (Property) evsResults.get(i);
                            System.out.println("\t\t"+ p.getName()+"\t"+p.getValue());
                            }
                        }
                    else if(evsResults.get(0).getClass().getName().endsWith("Silo")){
                        System.out.println("\tSILO ---> " + evsResults.size());
                        for(int i=0; i< evsResults.size(); i++){
                            Silo s = (Silo) evsResults.get(i);
                            System.out.println("\t\t"+ s.getName()+"\t"+s.getId());
                            }
                        }
                    else if(evsResults.get(0).getClass().getName().endsWith("String")){
                        for(int i=0; i< evsResults.size(); i++){
                            String s = (String) evsResults.get(i);
                            System.out.println("\t\t"+ s);
                            }
                        }
                    else if(evsResults.get(0).getClass().getName().endsWith("HistoryRecord")){
                        System.out.println("\tHISTORY RECORDS ---> "+ evsResults.size());
                        for(int i=0; i< evsResults.size(); i++){
                            HistoryRecord hr = (HistoryRecord) evsResults.get(i);
                            System.out.println("\t\t"+ hr.getDescLogicConceptCode());
                            Vector h =  hr.getHistoryCollection();
                            for(int x=0;x<h.size(); x++){
                                History hist = (History) h.get(x);
                                System.out.println("\t\t\t"+hist.getEditAction() + " : " + hist.getEditActionDate() +"  ref.code : " + hist.getReferenceCode());
                                
                            }
                            }
                    } else if(evsResults.get(0).getClass().getName().endsWith("History")){
                        System.out.println();
                        
                    }
                    else{
                        System.out.println("Class not defined");
                        //printResults(evsResults);
                        }

                }catch(Exception ex){}
                
							}
                            
}
