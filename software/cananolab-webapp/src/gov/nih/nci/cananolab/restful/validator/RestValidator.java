package gov.nih.nci.cananolab.restful.validator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

public class RestValidator {
	
	public static List<String> validate(Object object)  {
		
		String errorString = "";
		
		List<String> errors = new ArrayList<String>(); 
		
		ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
		Validator validator = factory.getValidator();
		
		Set<ConstraintViolation<Object>> constraintViolations = validator.validate( object );
		
		if (!constraintViolations.isEmpty()) {
			for (ConstraintViolation<Object> violation : constraintViolations) {
				errorString  += (errorString.length() < 1) ? violation.getPropertyPath() + " " + violation.getMessage() : " : " + violation.getPropertyPath() + " " + violation.getMessage();
			
				if (errorString.length() > 0)  {
					errors.add(violation.getPropertyPath() + " " + violation.getMessage());
				}
			}
		} 

		return errors;
	}
	
//	public Message exist(MongoTemplate mongoTemplate, MatchModelInterface matchObject, Class<?> classType) {
//		Message existMessage = 	(mongoTemplate.findById(matchObject.getId(), classType) != null) ? 
//								(new MessageBuilder()).status("F").message("Object with id="+matchObject.getId()+" already exist").build() : 
//								(new MessageBuilder()).status("S").build();
//				
//		return existMessage;
//	}
}