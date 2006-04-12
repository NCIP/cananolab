

package gov.nih.nci.calab.domain;
import gov.nih.nci.calab.domain.*;
import gov.nih.nci.system.applicationservice.*;
import java.util.*;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */
 
  /**

   */

public  class Run 
	implements java.io.Serializable 
{
	private static final long serialVersionUID = 1234567890L;

	
	   
	   private java.lang.Long id;
	   public  java.lang.Long getId(){
	      return id;
	   }
	   public void setId( java.lang.Long id){
	      this.id = id;
	   }
	
	   
	   private java.lang.String name;
	   public  java.lang.String getName(){
	      return name;
	   }
	   public void setName( java.lang.String name){
	      this.name = name;
	   }
	
	   
	   private java.lang.String description;
	   public  java.lang.String getDescription(){
	      return description;
	   }
	   public void setDescription( java.lang.String description){
	      this.description = description;
	   }
	
	   
	   private java.lang.String createdBy;
	   public  java.lang.String getCreatedBy(){
	      return createdBy;
	   }
	   public void setCreatedBy( java.lang.String createdBy){
	      this.createdBy = createdBy;
	   }
	
	   
	   private java.util.Date createdDate;
	   public  java.util.Date getCreatedDate(){
	      return createdDate;
	   }
	   public void setCreatedDate( java.util.Date createdDate){
	      this.createdDate = createdDate;
	   }
	
	   
	   private java.util.Date runDate;
	   public  java.util.Date getRunDate(){
	      return runDate;
	   }
	   public void setRunDate( java.util.Date runDate){
	      this.runDate = runDate;
	   }
	
	   
	   private java.lang.String runBy;
	   public  java.lang.String getRunBy(){
	      return runBy;
	   }
	   public void setRunBy( java.lang.String runBy){
	      this.runBy = runBy;
	   }
	

	
	   
	   
	   
	      
			
			
			
			
			private gov.nih.nci.calab.domain.Assay assay;
			public gov.nih.nci.calab.domain.Assay getAssay(){
			
			
			
//			  ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
//			  gov.nih.nci.calab.domain.Run thisIdSet = new gov.nih.nci.calab.domain.Run();
//			  thisIdSet.setId(this.getId());
//			  
//			  try {
//			     java.util.List resultList = applicationService.search("gov.nih.nci.calab.domain.Assay", thisIdSet);				 
//		             if (resultList!=null && resultList.size()>0) {
//		                assay = (gov.nih.nci.calab.domain.Assay)resultList.get(0);
//		             }
//		          
//			  } catch(Exception ex) 
//			  { 
//			      	System.out.println("Run:getAssay throws exception ... ...");
//			   		ex.printStackTrace(); 
//			  }
			  return assay;	
			 
			 		
           }
		   
	      
	               
	   
	   
	   
	   public void setAssay(gov.nih.nci.calab.domain.Assay assay){
		this.assay = assay;
	   }	
	   
	   
	
	   
	   
	   
	      
			private java.util.Collection runSampleContainerCollection = new java.util.HashSet();
			public java.util.Collection getRunSampleContainerCollection(){
//			try{
//			   if(runSampleContainerCollection.size() == 0) {}
//		           } catch(Exception e) {			     
//			      ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
//			      try {
//			      
//			      
//			         
//				 	gov.nih.nci.calab.domain.Run thisIdSet = new gov.nih.nci.calab.domain.Run();
//			         	thisIdSet.setId(this.getId());
//			         	java.util.Collection resultList = applicationService.search("gov.nih.nci.calab.domain.RunSampleContainer", thisIdSet);				 
//				 	runSampleContainerCollection = resultList;  
//				 	return resultList;
//				 
//			      
//			      }catch(Exception ex) 
//			      {
//			      	System.out.println("Run:getRunSampleContainerCollection throws exception ... ...");
//			   		ex.printStackTrace(); 
//			      }
//			   }	
	              return runSampleContainerCollection;
	          }
			   
			   
			   
			   
			   
	      
	               
	   
	   	public void setRunSampleContainerCollection(java.util.Collection runSampleContainerCollection){
	   		this.runSampleContainerCollection = runSampleContainerCollection;
	        }	
	   
	   
	
	   
	   
	   
	      
			private java.util.Collection inputFileCollection = new java.util.HashSet();
			public java.util.Collection getInputFileCollection(){
//			try{
//			   if(inputFileCollection.size() == 0) {}
//		           } catch(Exception e) {			     
//			      ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
//			      try {
//			      
//			      
//			         
//				 	gov.nih.nci.calab.domain.Run thisIdSet = new gov.nih.nci.calab.domain.Run();
//			         	thisIdSet.setId(this.getId());
//			         	java.util.Collection resultList = applicationService.search("gov.nih.nci.calab.domain.InputFile", thisIdSet);				 
//				 	inputFileCollection = resultList;  
//				 	return resultList;
//				 
//			      
//			      }catch(Exception ex) 
//			      {
//			      	System.out.println("Run:getInputFileCollection throws exception ... ...");
//			   		ex.printStackTrace(); 
//			      }
//			   }	
	              return inputFileCollection;
	          }
			   
			   
			   
			   
			   
	      
	               
	   
	   	public void setInputFileCollection(java.util.Collection inputFileCollection){
	   		this.inputFileCollection = inputFileCollection;
	        }	
	   
	   
	
	   
	   
	   
	      
			private java.util.Collection outputFileCollection = new java.util.HashSet();
			public java.util.Collection getOutputFileCollection(){
//			try{
//			   if(outputFileCollection.size() == 0) {}
//		           } catch(Exception e) {			     
//			      ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
//			      try {
//			      
//			      
//			         
//				 	gov.nih.nci.calab.domain.Run thisIdSet = new gov.nih.nci.calab.domain.Run();
//			         	thisIdSet.setId(this.getId());
//			         	java.util.Collection resultList = applicationService.search("gov.nih.nci.calab.domain.OutputFile", thisIdSet);				 
//				 	outputFileCollection = resultList;  
//				 	return resultList;
//				 
//			      
//			      }catch(Exception ex) 
//			      {
//			      	System.out.println("Run:getOutputFileCollection throws exception ... ...");
//			   		ex.printStackTrace(); 
//			      }
//			   }	
	              return outputFileCollection;
	          }
			   
			   
			   
			   
			   
	      
	               
	   
	   	public void setOutputFileCollection(java.util.Collection outputFileCollection){
	   		this.outputFileCollection = outputFileCollection;
	        }	
	   
	   
	

		public boolean equals(Object obj){
			boolean eq = false;
			if(obj instanceof Run) {
				Run c =(Run)obj; 			 
				Long thisId = getId();		
				
					if(thisId != null && thisId.equals(c.getId())) {
					   eq = true;
				    }		
				
			}
			return eq;
		}
		
		public int hashCode(){
			int h = 0;
			
			if(getId() != null) {
				h += getId().hashCode();
			}
			
			return h;
	}
	
	
}