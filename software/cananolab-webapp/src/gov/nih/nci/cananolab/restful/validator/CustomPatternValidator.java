// $Id: PatternValidator.java 17620 2009-10-04 19:19:28Z hardy.ferentschik $
/*
* JBoss, Home of Professional Open Source
* Copyright 2009, Red Hat, Inc. and/or its affiliates, and individual contributors
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
* http://www.apache.org/licenses/LICENSE-2.0
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,  
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/
package gov.nih.nci.cananolab.restful.validator;

import gov.nih.nci.cananolab.restful.util.PropertyUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.PatternSyntaxException;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Pattern;

/**
 * This is based on hibernate's PatternValidator. 
 * <br><br>
 * What's changed: 1. empty string is accepted as valid <br>
 * 					2. regexpName is accepted to resolve a valid regexp from a saved hashmap <br>
 *
 * 
 * 
 * @author YangS8
 */
public class CustomPatternValidator implements ConstraintValidator<PatternMatchIfNotNullNotEmpty, String> {
	
	private Map<String, String> regexpMap = new HashMap<String, String>();

	private java.util.regex.Pattern pattern;
	
	private String messageSource;
	private String messageKey;

	public void initialize(PatternMatchIfNotNullNotEmpty parameters) {
		Pattern.Flag flags[] = parameters.flags();
		int intFlag = 0;
		for ( Pattern.Flag flag : flags ) {
			intFlag = intFlag | flag.getValue();
		}
		
		String regexp = resolveRegexp(parameters);	
		messageSource = parameters.messageSource();
		messageKey = parameters.messageKey();

		try {
			pattern = java.util.regex.Pattern.compile(regexp, intFlag );
		}
		catch ( PatternSyntaxException e ) {
			throw new IllegalArgumentException( "Invalid regular expression.", e );
		}
	}

	public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
		if ( value == null || value.length() == 0) {
			return true;
		}
		
		Matcher m = pattern.matcher( value );
		boolean matched = m.matches();
		if (!matched) {
			setCustomizeMessageIfNeeded(constraintValidatorContext);
		}
		return matched;
	}
	
	protected void populateRegexpNameValues() {
		this.regexpMap.put("zip", "^(\\d{5}(-\\d{4})?)|([a-zA-Z0-9\\s])$");
		this.regexpMap.put("alphabetic", "^[a-zA-Z\\s]*$");
		
		this.regexpMap.put("relaxedAlphabetic", "^[a-zA-Z\\s\\-\\.\\']*$");
		this.regexpMap.put("numeric", "^[0-9]*$");
		this.regexpMap.put("number", "^[-+]?[0-9]*\\.?[0-9]+$");
		this.regexpMap.put("alphanumeric", "^[a-zA-Z0-9\\s]*$");
		this.regexpMap.put("relaxedAlphanumeric", "^[a-zA-Z0-9\\s\\-\\.\\(\\)]*$");
		this.regexpMap.put("loginName", "^[a-zA-Z0-9\\s\\_]*$");
		this.regexpMap.put("phone", "^\\+?[0-9()\\-\\s]*((ext\\.|extension)\\s[0-9]+)?$");
		this.regexpMap.put("textFieldWhiteList", "^(?!.*(<script|<%00script|\\%3C\\%73\\%63\\%72\\%69\\%70\\%74|<script|<script|<%00script|\\%uff1cscript\\%uff1e|\\%BC\\%F3\\%E3\\%F2\\%E9\\%F0\\%F4|\\+ADw\\-SCRIPT\\+AD4|\\u003Cscript|javascript\\:|\\%6A\\%61\\%76\\%61\\%73\\%63%\\%72\\%69\\%70\\%74\\%3A|javascript:|javascript:|<iframe|<frame|etc/passwd|/bin/id|\\.ini|;vol\\||id\\||AVAK\\$\\(RETURN_CODE\\)OS|sys\\.dba_user|\\+select\\+|\\+and\\+|WFXSSProbe|WF_XSRF|alert\\(|TEXT/VBSCRIPT|=\"|\\.\\./|\\.\\.\\|\\'|\\\"|background\\:|\\'\\+|\\\"\\+|%\\d+)).*$");
		
		this.regexpMap.put("relaxedTextFieldWhiteList0", "^[a-zA-Z0-9\\-\\_\\s\\(\\)\\:\\.\\,\\/\\?\\*]*$");
		this.regexpMap.put("doi", "^[a-zA-Z0-9\\/\\-\\_\\s\\(\\)\\:\\.]*$");
	}
	
	/**
	 * Resolve the valid regexp from regexpName then regexp parameters
	 * 
	 * @param parameters
	 * @return
	 * @throws IllegalArgumentException
	 */
	protected String resolveRegexp(PatternMatchIfNotNullNotEmpty parameters) 
			throws IllegalArgumentException {
		
		if (regexpMap.size() == 0)
			this.populateRegexpNameValues();
		
		String regexp = regexpMap.get(parameters.regexpName());
		if (regexp == null || regexp.length() == 0)
			regexp = parameters.regexp();
		
		if (regexp == null || regexp.length() == 0) 
			throw new IllegalArgumentException( "Unable to resolve a valid regexp.");
		
		return regexp;
	}
	
	protected void setCustomizeMessageIfNeeded(ConstraintValidatorContext constraintValidatorContext) 
			throws IllegalArgumentException {
		
		if (messageSource != null && messageSource.length() != 0
				&& messageKey != null && messageKey.length() != 0) {
			String message = PropertyUtil.getProperty(messageSource, messageKey);
			constraintValidatorContext.disableDefaultConstraintViolation();
			constraintValidatorContext.buildConstraintViolationWithTemplate(message).addConstraintViolation();
		} 
			
	}
}
