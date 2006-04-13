

package gov.nih.nci.calab.domain;

/**
 * <!-- LICENSE_TEXT_START -->
 * <!-- LICENSE_TEXT_END -->
 */
 
  /**

   */

public  class Assay 
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
	
	   
	   private java.lang.String createdBy;
	   public  java.lang.String getCreatedBy(){
	      return createdBy;
	   }
	   public void setCreatedBy( java.lang.String createdBy){
	      this.createdBy = createdBy;
	   }
	
	   
	   private java.lang.String description;
	   public  java.lang.String getDescription(){
	      return description;
	   }
	   public void setDescription( java.lang.String description){
	      this.description = description;
	   }
	
	   
	   private java.lang.String assayType;
	   public  java.lang.String getAssayType(){
	      return assayType;
	   }
	   public void setAssayType( java.lang.String assayType){
	      this.assayType = assayType;
	   }
	
	   
	   private java.util.Date createdDate;
	   public  java.util.Date getCreatedDate(){
	      return createdDate;
	   }
	   public void setCreatedDate( java.util.Date createdDate){
	      this.createdDate = createdDate;
	   }
	

	
	   
	   
	   
	      
			
			
			
			
			private gov.nih.nci.calab.domain.Protocol protocol;
			public gov.nih.nci.calab.domain.Protocol getProtocol(){
			
			
			
//			  ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
//			  gov.nih.nci.calab.domain.Assay thisIdSet = new gov.nih.nci.calab.domain.Assay();
//			  thisIdSet.setId(this.getId());
//			  
//			  try {
//			     java.util.List resultList = applicationService.search("gov.nih.nci.calab.domain.Protocol", thisIdSet);				 
//		             if (resultList!=null && resultList.size()>0) {
//		                protocol = (gov.nih.nci.calab.domain.Protocol)resultList.get(0);
//		             }
//		          
//			  } catch(Exception ex) 
//			  { 
//			      	System.out.println("Assay:getProtocol throws exception ... ...");
//			   		ex.printStackTrace(); 
//			  }
			  return protocol;	
			 
			 		
           }
		   
	      
	               
	   
	   
	   
	   public void setProtocol(gov.nih.nci.calab.domain.Protocol protocol){
		this.protocol = protocol;
	   }	
	   
	   
	
	   
	   
	   
	      
			private java.util.Collection runCollection = new java.util.HashSet();
			public java.util.Collection getRunCollection(){
//			try{
//			   if(runCollection.size() == 0) {}
//		           } catch(Exception e) {			     
//			      ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
//			      try {
//			      
//			      
//			         
//				 	gov.nih.nci.calab.domain.Assay thisIdSet = new gov.nih.nci.calab.domain.Assay();
//			         	thisIdSet.setId(this.getId());
//			         	java.util.Collection resultList = applicationService.search("gov.nih.nci.calab.domain.Run", thisIdSet);				 
//				 	runCollection = resultList;  
//				 	return resultList;
//				 
//			      
//			      }catch(Exception ex) 
//			      {
//			      	System.out.println("Assay:getRunCollection throws exception ... ...");
//			   		ex.printStackTrace(); 
//			      }
//			   }	
	              return runCollection;
	          }
			   
			   
			   
			   
			   
	      
	               
	   
	   	public void setRunCollection(java.util.Collection runCollection){
	   		this.runCollection = runCollection;
	        }	
	   
	   
	

		public boolean equals(Object obj){
			boolean eq = false;
			if(obj instanceof Assay) {
				Assay c =(Assay)obj; 			 
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