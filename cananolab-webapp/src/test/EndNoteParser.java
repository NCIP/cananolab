/**
 *
 */
package test;

import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.service.publication.EndNoteXMLHandler;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

/**
 * @author tanq
 *
 */
public class EndNoteParser {
	static boolean DEBUG = true;

	public static void main(String[] args) {

		// DEBUG
		// System.setProperty("gov.nih.nci.security.configFile",
		// "C:/project/java/workspace/caNanoLab/build/ApplicationSecurityConfig.xml");
		// String inputFileName =
		// "C:\\temp\\publication\\ShorterAllianceNoStyle.xml";
		// String outputFileName = "C:\\temp\\publication\\AllianceOutput.txt";
		// args = new String[2];
		// args[0] = inputFileName;
		// args[1] = outputFileName;
		// END OF DEBUG

		String inputFileName = null;
		String outputFileName = null;
		PrintStream p = System.out;
		if (args != null && args.length > 2) {
			if (args.length == 3) {
				inputFileName = args[2];
			} else if (args.length == 4) {
				inputFileName = args[2];
				outputFileName = args[3];
				try {
					p = new PrintStream(outputFileName);
				} catch (IOException ex) {
					System.out.println("Output File IO Exception!");
					ex.printStackTrace();
					return;
				}
			} else {
				printHelpPage();
				return;
			}
			System
					.setProperty("gov.nih.nci.security.configFile",
							"C:/caNanolab_1.5/conf/standalone/ApplicationSecurityConfig.xml");
			System.setProperty("java.security.auth.login.config",
					"C:/caNanolab_1.5/conf/standalone/login.config");
		} else {
			printHelpPage();
			return;
		}
		boolean isSuccess = false;
		EndNoteXMLHandler endNotehandler = null;
		try {
			String userLoginName = args[0];
			String userPassword = args[1];
			UserBean user = new UserBean(userLoginName, userPassword);
			endNotehandler = new EndNoteXMLHandler(inputFileName, true);
			isSuccess = endNotehandler.parsePublicationXML(user);
		} catch (Exception ex) {
			isSuccess = false;
			ex.printStackTrace();
		} finally {
			if (p != null && outputFileName != null) {
				p.close();
			}
		}
		System.exit(isSuccess ? 0 : 1);
	}

	private static void printHelpPage() {
		System.out.println("Invalid argument!");
		System.out.println("java EndnoteParser <inputFileName> [outputLog]");
		System.out.println("Optional: outputLog");
	}

	// NOT IN USED
	public static void replaceTokens(String filePath)
			throws java.io.IOException {
		StringBuffer fileData = new StringBuffer(1000);
		BufferedReader reader = new BufferedReader(new FileReader(filePath));
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			fileData.append(buf, 0, numRead);
		}
		reader.close();

		String[] tokens = {
				"<style face=\"normal\" font=\"default\" size=\"100%\">",
				"<style face=\"italic\" font=\"default\" size=\"100%\">",
				"<style face=\"bold\" font=\"default\" size=\"100%\">",
				"<style face=\"italic underline\" font=\"default\" size=\"100%\">",
				"</style>" };
		String fileString = fileData.toString();
		for (String token : tokens) {
			fileString = fileString.replaceAll(token, "");
		}

		FileOutputStream out = new FileOutputStream(filePath);
		PrintStream p = new PrintStream(out);
		p.print(fileString);
	}
}
