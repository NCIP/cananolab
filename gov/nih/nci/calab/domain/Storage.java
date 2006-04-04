

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

public  class Storage 
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
	
	   
	   private java.lang.String location;
	   public  java.lang.String getLocation(){
	      return location;
	   }
	   public void setLocation( java.lang.String location){
	      this.location = location;
	   }
	
	   
	   private java.lang.String type;
	   public  java.lang.String getType(){
	      return type;
	   }
	   public void setType( java.lang.String type){
	      this.type = type;
	   }
	

	
	   
	   
	   
	      
			
			
			private gov.nih.nci.calab.domain.SampleContainer sampleProperty;
			public gov.nih.nci.calab.domain.SampleContainer getSampleProperty(){
			
              ApplicationService applicationService = ApplicationServiceProvider.getApplicationService();
			  gov.nih.nci.calab.domain.Storage thisIdSet = new gov.nih.nci.calab.domain.Storage();
			  thisIdSet.setId(this.getId());
			  try {
			  java.util.List resultList = applicationService.search("gov.nih.nci.calab.domain.SampleContainer", thisIdSet);				 
			 
			  if (resultList!=null && resultList.size()>0) {
			     sampleProperty = (gov.nih.nci.calab.domain.SampleContainer)resultList.get(0);
			     }
			  } catch(Exception ex) 
			  { 
			      	System.out.println("Storage:getSampleProperty throws exception ... ...");
			   		ex.printStackTrace(); 
			  }
			  return sampleProperty;			
			 		
              }
                        
	      
	               
	   
	   
	   
	   public void setSampleProperty(gov.nih.nci.calab.domain.SampleContainer sampleProperty){
		this.sampleProperty = sampleProperty;
	   }	
	   
	   
	

		public boolean equals(Object obj){
			boolean eq = false;
			if(obj instanceof Storage) {
				Storage c =(Storage)obj; 			 
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