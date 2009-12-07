package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.characterization.invitro.Cytotoxicity;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.LoginService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExcelParser;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.apache.log4j.Logger;

public class KellyDataLoader {
	private static Logger logger = Logger.getLogger(KellyDataLoader.class);

	public final static String SAMPLE_NAME_PREFIX = "MIT_MGH-KKellyIB2009-";
	public final static String DATUM_NAME = "sample concentration (mg/mL)";
	private String userName = "AUTO_PARSER";
	
	// Data map, is the Vertical Map, {NP1 -> {AO_RES_0_01, 0.403302864}}.
	private SortedMap<String, SortedMap<String, SortedMap<String, Double>>> dataMatrix;

	public KellyDataLoader(
			SortedMap<String, SortedMap<String, SortedMap<String, Double>>> kellyMatrix) {
		this.dataMatrix = new TreeMap<String, SortedMap<String, SortedMap<String, Double>>>(); 
		
		//1.generate a sorted key map, make 261-9-1 => 261-09-01.
		SortedMap<String, String> sortedKeyMap = new TreeMap<String, String>();
		for (String key : kellyMatrix.keySet()) {
			String sortedKey = key.replaceAll("-(\\d)-", "-0$1-");
			sortedKey = sortedKey.replaceAll("-(\\d)$", "-0$1");
			sortedKeyMap.put(sortedKey, key);
		}
		
		//2.replace sorted key with sample name prefix, "MIT...01" to "MIT...39".
		int count = 1;
		for (String key : sortedKeyMap.keySet()) {
			StringBuilder sampleName = new StringBuilder(SAMPLE_NAME_PREFIX);
			if (count < 10) {
				sampleName.append('0');
			}
			sampleName.append(count++);
			SortedMap<String, SortedMap<String, Double>> assayMap = 
				kellyMatrix.get(sortedKeyMap.get(key));
			this.dataMatrix.put(sampleName.toString(), assayMap);
			if (logger.isDebugEnabled()) {
				logger.debug(sortedKeyMap.get(key) + " ==> " + sampleName);
			}
		}
	}

	public void load(UserBean user) {
		this.userName = user.getLoginName();
		SampleService service = new SampleServiceLocalImpl();
		CharacterizationService charService = new CharacterizationServiceLocalImpl();
		
		//iterate dataMatrix map, load sample & save Cytotoxity char.
		for (String sampleName : this.dataMatrix.keySet()) {
			SampleBean sampleBean = null;
			try {
				//1.load sampleBean by sample name.
				sampleBean = service.findSampleByName(sampleName, user);
			} catch (Exception e) {
				logger.error("Error loading Sample for: " + sampleName, e);
				continue;
			}
			if (sampleBean == null) {
				if (logger.isDebugEnabled()) {
					logger.debug("Sample not found for: " + sampleName);
				}
				continue;
			}
			//2.iterate assayMap, create Cytotoxicity, Finding & Datum.
			SortedMap<String, SortedMap<String, Double>> assayMap = 
				this.dataMatrix.get(sampleName);
			for (String assayName : assayMap.keySet()) {
				Date currentDate = Calendar.getInstance().getTime();
				
				//2a.create 1 Cytotoxicity for each CellLine.
				Cytotoxicity achar = new Cytotoxicity();
				achar.setCreatedBy(this.userName);
				achar.setCreatedDate(currentDate);
				achar.setCellLine(assayName);
				//achar.setAssayType("???"); //TODO: AssayType unknown.
				achar.setFindingCollection(new HashSet<Finding>());
				
				//2b.create 1 Finding for each CellLine.
				Finding finding = new Finding();
				finding.setCreatedBy(this.userName);
				finding.setCreatedDate(currentDate);
				achar.getFindingCollection().add(finding);
				finding.setDatumCollection(new HashSet<Datum>());
				SortedMap<String, Double> datumMap = assayMap.get(assayName);
				
				//2c.iterate datumMap & add datum to finding.
				int i = 0;
				for (String valueType : datumMap.keySet()) {
					Datum datum = new Datum();
					datum.setCreatedBy(this.userName);
					datum.setCreatedDate(DateUtils.addSecondsToCurrentDate(i++));
					datum.setName(DATUM_NAME);
					datum.setValue(datumMap.get(valueType).floatValue());
					datum.setValueType(valueType);
					finding.getDatumCollection().add(datum);
				}
				
				//3.new CharBean, save Finding and the Cytotoxicity bean.
				CharacterizationBean charBean = new CharacterizationBean(achar);
				try {
					List<FindingBean> findings = charBean.getFindings();
					for (FindingBean findingBean : findings) {
						charService.saveFinding(findingBean, user);
					}
					charService.saveCharacterization(sampleBean, charBean, user);
					if (logger.isDebugEnabled()) {
						logger.debug("charBean saved: " + sampleName + ", " + assayName);
					}
				} catch (Exception e) {
					logger.error("Error saving charBean: " + sampleName + ", " + assayName, e);
					continue;
				}
			}
		}
	}

	public static void main(String[] args) {
		if (args != null && args.length == 3) {
			String loginName = args[0];
			String password = args[1];
			String inputFileName = args[2];
			KellyDataLoader loader = null;
			try {
				ExcelParser parser = new ExcelParser();
				SortedMap<String, SortedMap<String, SortedMap<String, Double>>> 
					dataMatrix = parser.twoWayParse(inputFileName);
				loader = new KellyDataLoader(dataMatrix);
			} catch (IOException e) {
				System.out.println("Input file not found.");
				e.printStackTrace();
				System.exit(1);
			}
			try {
				LoginService loginService = new LoginService(Constants.CSM_APP_NAME);
				UserBean user = loginService.login(loginName, password);
				if (user.isCurator()) {
					loader.load(user);
				} else {
					System.out.println(
						"You need to be the curator to be able to execute this function");
					System.exit(1);
				}
			} catch (SecurityException e) {
				System.out.println("Can't authenticate the login name.");
				e.printStackTrace();
				System.exit(1);
			}
		} else {
			System.out.println("Invalid argument!");
			System.out.println(
				"java KellyDataLoader <loginName> <password> <inputFileName>");
		}
		System.exit(0);
	}
}
