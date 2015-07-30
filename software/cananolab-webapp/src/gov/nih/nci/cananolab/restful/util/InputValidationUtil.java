package gov.nih.nci.cananolab.restful.util;

import org.apache.commons.validator.EmailValidator;

public class InputValidationUtil {
	
	public static boolean isAlphabetic(String input) {
		String reg = "^[a-zA-Z\\s]*$";
		
		return (input == null) ? false : input.matches(reg);

	}
	
	public static boolean isRelaxedAlphabetic(String input) {
		if(input == null || input == "")
			return true;
		String reg = "^[a-zA-Z\\s\\-\\.\\'\\é\\Ė\\Á\\Ä\\á\\ë\\ú\\ý\\ÿ]*$";
		
		if(input.matches(reg))
			return true;
		return false;

	}
	
	public static boolean isNumeric(String input) {
		String reg = "^[0-9]*$";
		
		return (input == null) ? false : input.matches(reg);

	}
	
	public static boolean isAlphanumeric(String input) {
		String reg = "^[a-zA-Z0-9\\s]*$";
		
		return (input == null) ? false : input.matches(reg);
	}
	
	public static boolean isRelaxedAlphanumeric(String input) {
		String reg = "^[a-zA-Z0-9\\s\\-\\.\\(\\)]*$";
		return (input == null) ? false : input.matches(reg);

	}
	
	public static boolean isLoginNameValid(String input) {
		String reg = "^[a-zA-Z0-9\\s\\_]*$";
		
		return (input == null) ? false : input.matches(reg);
	}
	
	public static boolean isZipValid(String input) {
		String reg = "^(\\d{5}(-\\d{4})?)|([a-zA-Z0-9\\s])$";
		boolean match = input.matches(reg);
		return (input == null) ? false : input.matches(reg);
	}
	
	public static boolean isPhoneValid(String input) {
		String reg = "^\\+?[0-9()\\-\\s]*((ext\\.|extension)\\s[0-9]+)?$";
		return (input == null) ? false : input.matches(reg);
	}
	
	public static boolean isTextFieldWhiteList(String input) {
		if(input == null || input == "")
			return true;
		String reg = "^(?!.*(<script|<%00script|\\%3C\\%73\\%63\\%72\\%69\\%70\\%74|<script|<script|<%00script|\\%uff1cscript\\%uff1e|\\%BC\\%F3\\%E3\\%F2\\%E9\\%F0\\%F4|\\+ADw\\-SCRIPT\\+AD4|\\u003Cscript|javascript\\:|\\%6A\\%61\\%76\\%61\\%73\\%63%\\%72\\%69\\%70\\%74\\%3A|javascript:|javascript:|<iframe|<frame|etc/passwd|/bin/id|\\.ini|;vol\\||id\\||AVAK\\$\\(RETURN_CODE\\)OS|sys\\.dba_user|\\+select\\+|\\+and\\+|WFXSSProbe|WF_XSRF|alert\\(|TEXT/VBSCRIPT|=\"|\\.\\./|\\.\\.\\|\\'|\\\"|background\\:|\\'\\+|\\\"\\+|%\\d+)).*$";
		if(input.matches(reg))
			return true;
		return false;
	}
	
	public static boolean isNumber(String input) {
		String reg = "^[-+]?[0-9]*\\.?[0-9]+$";
		return (input == null) ? false : input.matches(reg);
	}
	
	public static boolean isRelaxedTextFieldWhiteList0(String input) {
		if(input == null || input == "")
			return true;
		String reg = "^[a-zA-Z0-9\\-\\_\\s\\(\\)\\:\\.\\,\\/\\?\\*]*$";
		if(input.matches(reg))
			return true;
		return false;
	}
	
	public static boolean isDoiValid(String input) {
		if(input == null || input == "")
			return true;
		String reg = "^[a-zA-Z0-9\\/\\-\\_\\s\\(\\)\\:\\.]*$";
		if(input.matches(reg))
			return true;
		return false;
	}
	
	public static boolean isUrlValid(String input){
		if(input == null || input == "")
			return true;
		String reg = "((http://|https://|ftp://)([\\S.]+))|((\\\\)(.+)(\\.)(\\w+))";
		if(input.matches(reg))
			return true;
		return false;
	}
	
	public static boolean isEmailValid(String email) {
		EmailValidator emailValidator = EmailValidator.getInstance();
		return (email == null || email.length() == 0) ? true : emailValidator.isValid(email);
	}
	/*


<constant>
	<constant-name>textFieldWhiteList2</constant-name>
	<constant-value>^(?!.*(&lt;script|&lt;%00script|\%3C\%73\%63\%72\%69\%70\%74|&#x3C;&#x73;&#x63;&#x72;&#x69;&#x70;&#x74;|&#60;&#115;&#99;&#114;&#105;&#112;&#116;|&#60;&#37;&#48;&#48;&#115;&#99;&#114;&#105;&#112;&#116;|\%uff1cscript\%uff1e|\%BC\%F3\%E3\%F2\%E9\%F0\%F4|\+ADw\-SCRIPT\+AD4|\\u003Cscript|javascript\:|\%6A\%61\%76\%61\%73\%63%\%72\%69\%70\%74\%3A|&#x6a;&#x61;&#x76;&#x61;&#x73;&#x63;&#x72;&#x69;&#x70;&#x74;&#x3a;|&#106;&#97;&#118;&#97;&#115;&#99;&#114;&#105;&#112;&#116;&#58;|etc/passwd|/bin/id|\.ini|;vol\||id\||AVAK\$\(RETURN_CODE\)OS|sys\.dba_user|\+select\+|\+and\+|WFXSSProbe|TEXT/VBSCRIPT|="|\.\./|\.\.\\|\\'|\\\"|background\:|\'\+|\"\+|%\d+)).*$			
	</constant-value>
</constant>
<constant>
	<constant-name>textFieldWhiteList1</constant-name>
	<constant-value>^(?!.*(\="|\+and\+|\+select\+|sys\.dba\_user|AVAK\$\(RETURN\_CODE\)OS|id\||;vol\||%\d+|\&#60;|\>|\|\.\.\\|\.\.\/|\.ini|javascript\:|\/etc\/passwd|\/bin\/id|\\\'|\\\"|background\:expression)).*$</constant-value>
</constant>
<constant>
	<constant-name>textFieldWhiteList0</constant-name>
	<constant-value>^[a-zA-Z0-9\-\_\s\(\)\:\.\/\?\*]*$</constant-value>
</constant>

<constant>
	<constant-name>doi</constant-name>
	<constant-value>^[a-zA-Z0-9\/\-\_\s\(\)\:\.]*$</constant-value>
</constant>

<constant>
	<constant-name>unit</constant-name>
	<constant-value>^[a-zA-Z0-9\%\/\-\_\s\(\)\^]*$</constant-value>
</constant>
*/
}
