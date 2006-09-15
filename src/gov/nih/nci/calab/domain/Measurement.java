/**
 * 
 */
package gov.nih.nci.calab.domain;

/**
 * @author zengje
 *
 */
public class Measurement {

	private static final long serialVersionUID = 1234567890L;

	/**
	 * 
	 */
	public Measurement() {
		super();
		// TODO Auto-generated constructor stub
	}
	 
	public Measurement(String value, String unit) {
		this.setValue(value);
		this.setUnitOfMeasurement(unit);
	}
	
	   private java.lang.Long id;
	   public  java.lang.Long getId(){
	      return id;
	   }
	   public void setId( java.lang.Long id){
	      this.id = id;
	   }
	
	   
	   private java.lang.String value;
	   public  java.lang.String getValue(){
	      return value;
	   }
	   public void setValue( java.lang.String value){
	      this.value = value;
	   }
	
	   
	   private java.lang.String unitOfMeasurement;
	   public  java.lang.String getUnitOfMeasurement(){
	      return unitOfMeasurement;
	   }
	   public void setUnitOfMeasurement( java.lang.String unitOfMeasurement){
	      this.unitOfMeasurement = unitOfMeasurement;
	   }

		public boolean equals(Object obj){
			boolean eq = false;
			if(obj instanceof AssayType) {
				AssayType c =(AssayType)obj; 			 
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