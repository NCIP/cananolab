package gov.nih.nci.cananolab.restful.util;

public class InputValidationUtil {
	
	public static boolean isAlphabetic(String input) {
		String reg = "^[a-zA-Z\\s]*$";
		
		return (input == null) ? false : input.matches(reg);

	}
	
	public static boolean isRelaxedAlphabeticc(String input) {
		String reg = "^[a-zA-Z\\s\\-\\.\\']*$";
		
		return (input == null) ? false : input.matches(reg);

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
		return (input == null) ? false : input.matches(reg);
	}
	
	public static boolean isPhoneValid(String input) {
		String reg = "^\\+?[0-9()\\-\\s]*((ext\\.|extension)\\s[0-9]+)?$";
		return (input == null) ? false : input.matches(reg);
	}
	
//	public static boolean isTextFieldWhiteList(String input) {
//		String reg = "^(?!.*(&lt;script|&lt;%00script|\\%3C\\%73\\%63\\%72\\%69\\%70\\%74|&#x3C;&#x73;&#x63;&#x72;&#x69;&#x70;&#x74;|&#60;&#115;&#99;&#114;&#105;&#112;&#116;|&#60;&#37;&#48;&#48;&#115;&#99;&#114;&#105;&#112;&#116;|\\%uff1cscript\\%uff1e|\\%BC\\%F3\\%E3\\%F2\\%E9\\%F0\\%F4|\\+ADw\\-SCRIPT\\+AD4|\\u003Cscript|javascript\\:|\\%6A\\%61\\%76\\%61\\%73\\%63%\\%72\\%69\\%70\\%74\\%3A|&#x6a;&#x61;&#x76;&#x61;&#x73;&#x63;&#x72;&#x69;&#x70;&#x74;&#x3a;|&#106;&#97;&#118;&#97;&#115;&#99;&#114;&#105;&#112;&#116;&#58;|&lt;iframe|&lt;frame|etc/passwd|/bin/id|\\.ini|;vol\\||id\||AVAK\$\\(RETURN_CODE\)OS|sys\\.dba_user|\\+select\+|\\+and\\+|WFXSSProbe|WF_XSRF|alert\\(|TEXT/VBSCRIPT|=\"|\\.\\./|\\.\.\\|\\'|\\\"|background\:|\\'\+|\\\"\\+|%\\d+)).*$";
//		return (input == null) ? false : input.matches(reg);
//	}
	
	public static boolean isNumber(String input) {
		String reg = "^[-+]?[0-9]*\\.?[0-9]+$";
		return (input == null) ? false : input.matches(reg);
	}
	
	/*

<constant>
	<constant-name>textFieldWhiteList</constant-name>
	<constant-value>^(?!.*(&lt;script|&lt;%00script|\%3C\%73\%63\%72\%69\%70\%74|&#x3C;&#x73;&#x63;&#x72;&#x69;&#x70;&#x74;|&#60;&#115;&#99;&#114;&#105;&#112;&#116;|&#60;&#37;&#48;&#48;&#115;&#99;&#114;&#105;&#112;&#116;|\%uff1cscript\%uff1e|\%BC\%F3\%E3\%F2\%E9\%F0\%F4|\+ADw\-SCRIPT\+AD4|\\u003Cscript|javascript\:|\%6A\%61\%76\%61\%73\%63%\%72\%69\%70\%74\%3A|&#x6a;&#x61;&#x76;&#x61;&#x73;&#x63;&#x72;&#x69;&#x70;&#x74;&#x3a;|&#106;&#97;&#118;&#97;&#115;&#99;&#114;&#105;&#112;&#116;&#58;|&lt;iframe|&lt;frame|etc/passwd|/bin/id|\.ini|;vol\||id\||AVAK\$\(RETURN_CODE\)OS|sys\.dba_user|\+select\+|\+and\+|WFXSSProbe|WF_XSRF|alert\(|TEXT/VBSCRIPT|="|\.\./|\.\.\\|\\'|\\\"|background\:|\'\+|\"\+|%\d+)).*$			
	</constant-value>
</constant>
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
	<constant-name>relaxedTextFieldWhiteList0</constant-name>
	<constant-value>^[a-zA-Z0-9\-\_\s\(\)\:\.\,\/\?\*]*$</constant-value>
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
