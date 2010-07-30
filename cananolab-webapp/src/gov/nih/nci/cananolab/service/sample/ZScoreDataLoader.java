package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.characterization.invitro.Cytotoxicity;
import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.dto.common.FindingBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.SampleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.exception.BaseException;
import gov.nih.nci.cananolab.exception.SecurityException;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.sample.impl.CharacterizationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.util.Constants;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExcelParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;

import org.apache.log4j.Logger;

public class ZScoreDataLoader {
	private static Logger logger = Logger.getLogger(ZScoreDataLoader.class);

	public final static Map<String, String> CELL_TYPE_MAP = new HashMap<String, String>();
	static {
		CELL_TYPE_MAP.put("AO", "aortic endothelial");
		CELL_TYPE_MAP.put("SM", "vascular smooth muscle");
		CELL_TYPE_MAP.put("HEP", "HepG2 (liver hepatocytes)");
		CELL_TYPE_MAP.put("MP", "monocyte/macrophage");
	}

	public final static Map<String, String> ASSAY_TYPE_MAP = new HashMap<String, String>();
	static {
		ASSAY_TYPE_MAP.put("APO", "caspase 3 apoptosis");
		ASSAY_TYPE_MAP.put("JC1", "mitochondrial membrane potential");
		ASSAY_TYPE_MAP.put("RES", "cellular metabolism");
		ASSAY_TYPE_MAP.put("CTG", "intracellular ATP content");
	}

	public final static Map<String, String> ASSAY_DESC_MAP = new HashMap<String, String>();
	static {
		ASSAY_DESC_MAP
				.put(
						"APO",
						"Apo-ONE reagent was added in equal volume to the "
								+ "384-well plates; the remainder of the protocol followed the "
								+ "manufacturer's protocol. Fluorescence was read after a "
								+ "1-h incubation (485/530 nm).");

		ASSAY_DESC_MAP
				.put(
						"JC1",
						"JC1 reagent was added to to a final concentration "
								+ "of 2 micro M and incubated for 2 h. Plates were washed 3 x with PBS and "
								+ "read sequentially in the red (530/580 nm) and green (485/530 nm) "
								+ "fluorescence spectrum, and the red:green fluorescence ratio was calculated.");

		ASSAY_DESC_MAP
				.put(
						"RES",
						"C12-resazurin was added to a final concentration of "
								+ "1 microM and incubated for 1 h at 37 C. Plates were washed 3 x with PBS, "
								+ "and read at 530/580 nm.");

		ASSAY_DESC_MAP
				.put(
						"CTG",
						"Plates were washed 3 x with PBS, and 30 microL of "
								+ "PBS added to each well. CellTiter-Glo reagent was first diluted 1:3 "
								+ "with PBS and added in equal volume. The remainder of the protocol "
								+ "followed the manufacturer's protocol.");
	}

	public final static Map<String, String> DATUM_TYPE_MAP = new HashMap<String, String>();
	static {
		DATUM_TYPE_MAP.put("caspase 3 apoptosis", "fluorescence");
		DATUM_TYPE_MAP.put("mitochondrial membrane potential",
				"ratio of red to green fluorescence");
		DATUM_TYPE_MAP.put("cellular metabolism", "fluorescence");
		DATUM_TYPE_MAP.put("intracellular ATP content", "luminescence");
	}

	public final static Map<String, Double> FE_DOSE_MAP = new HashMap<String, Double>();
	static {
		FE_DOSE_MAP.put("0_01", 0.01);
		FE_DOSE_MAP.put("0_03", 0.03);
		FE_DOSE_MAP.put("0_1", 0.1);
		FE_DOSE_MAP.put("0_3", 0.3);
	}

	public final static Map<String, Double> QDOT_DOSE_MAP = new HashMap<String, Double>();
	static {
		QDOT_DOSE_MAP.put("0_01", 3d);
		QDOT_DOSE_MAP.put("0_03", 10d);
		QDOT_DOSE_MAP.put("0_1", 30d);
		QDOT_DOSE_MAP.put("0_3", 100d);
	}

	public final static String SAMPLE_NAME_PREFIX = "MIT_MGH-SShawPNAS2008-";

	public final static String CONDITION_NAME = "sample concentration";

	public final static String USER_NAME = "SPREAD_SHEET_PARSER_4_STANSHAW_DATA";

	// Sample name map, {NP1 -> MIT_MGH-SShawPNAS2008-01}, etc.
	private Map<String, String> sampleNameMap = new HashMap<String, String>();

	// Assay map, {AO_RES_0_01 -> [AssayCondition]}.
	private Map<String, AssayCondition> assayMap = new HashMap<String, AssayCondition>();

	// Data map, is the Vertical Map, {NP1 -> {AO_RES_0_01, 0.403302864}}.
	private SortedMap<String, SortedMap<String, Double>> dataMatrix;

	public ZScoreDataLoader(
			SortedMap<String, SortedMap<String, Double>> verticalMatrix,
			SortedMap<String, SortedMap<String, Double>> horizontalMatrix) {
		this.dataMatrix = verticalMatrix;
		List<String> particleNames = new ArrayList<String>(verticalMatrix
				.keySet());
		for (String name : particleNames) {
			String sampleName = null;
			if (name.matches("NP\\d")) {
				sampleName = name.replaceAll("NP(\\d)", "NP0$1");
			} else {
				sampleName = name;
			}
			sampleName = sampleName.replaceAll("NP", SAMPLE_NAME_PREFIX).trim();
			sampleNameMap.put(name, sampleName);
		}
		List<String> assayNames = new ArrayList<String>(horizontalMatrix
				.keySet());
		for (String name : assayNames) {
			if (name.matches("([A-Z]+)_([A-Z0-9]+)_(.+)")) {
				String cellType = name.replaceAll("([A-Z]+)_([A-Z0-9]+)_(.+)",
						"$1");
				String assayType = name.replaceAll("([A-Z]+)_([A-Z0-9]+)_(.+)",
						"$2");
				String conditionString = name.replaceAll(
						"([A-Z]+)_([A-Z0-9]+)_(.+)", "$3");

				AssayCondition assayCondition = new AssayCondition(
						CELL_TYPE_MAP.get(cellType), ASSAY_TYPE_MAP
								.get(assayType), FE_DOSE_MAP
								.get(conditionString), "mg/mL", QDOT_DOSE_MAP
								.get(conditionString), "nM", ASSAY_DESC_MAP
								.get(assayType));
				assayMap.put(name, assayCondition);
			}
		}
	}

	public void load(UserBean user) throws BaseException {
		Date currentDate = Calendar.getInstance().getTime();
		SecurityService securityService = new SecurityService(
				Constants.CSM_APP_NAME, user);
		SampleService service = new SampleServiceLocalImpl(securityService);
		CharacterizationService charService = new CharacterizationServiceLocalImpl(
				securityService);
		// iterate each Sample name, load sample & save Cytotoxity char.
		for (String name : sampleNameMap.keySet()) {
			String sampleName = sampleNameMap.get(name);
			SampleBean sampleBean = null;
			try {
				// 1.load sampleBean by sample name.
				sampleBean = service.findSampleByName(sampleName);
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
			// 2.create a Char map for holding all Chars for this sample.
			Map<String, Cytotoxicity> charMap = new HashMap<String, Cytotoxicity>();
			SortedMap<String, Double> data = dataMatrix.get(name);
			int i = 0;
			// 3.iterate data matrix for creating Cytotoxicity by data.
			for (String assayStr : data.keySet()) {
				Cytotoxicity achar = null;
				Finding finding = null;
				AssayCondition ac = assayMap.get(assayStr);
				this.saveLookupValue(ac);
				if (ac != null) {
					String acStr = ac.getCellType() + "||" + ac.getAssayType();
					achar = charMap.get(acStr);
					if (achar != null) {
						for (Finding aFinding : achar.getFindingCollection()) {
							finding = aFinding;
							break;
						}
					} else {
						i = 0;
						achar = new Cytotoxicity();
						achar.setCreatedBy(USER_NAME);
						achar.setCreatedDate(currentDate);
						achar.setCellLine(ac.getCellType());
						achar.setAssayType(ac.getAssayType());// TODO
						achar.setFindingCollection(new HashSet<Finding>());
						achar.setDesignMethodsDescription(ac
								.getAssayDesciption());
						finding = new Finding();
						finding.setCreatedBy(USER_NAME);
						finding.setCreatedDate(currentDate);
						achar.getFindingCollection().add(finding);
						finding.setDatumCollection(new HashSet<Datum>());
						charMap.put(acStr, achar);
					}
					Datum datum = new Datum();
					datum.setCreatedBy(USER_NAME);
					datum.setCreatedDate(DateUtils.addSecondsToCurrentDate(i));
					datum.setValue(data.get(assayStr).floatValue());
					datum.setName(DATUM_TYPE_MAP.get(ac.getAssayType()));// TODO
					datum.setValueType("Z-score");
					Condition condition = new Condition();
					condition.setCreatedBy(USER_NAME);
					condition.setCreatedDate(currentDate);
					condition.setName(CONDITION_NAME);
					// NP49, NP50, NP51 are QDots
					if (sampleName.matches(SAMPLE_NAME_PREFIX + "49")
							|| sampleName.matches(SAMPLE_NAME_PREFIX + "50")
							|| sampleName.matches(SAMPLE_NAME_PREFIX + "51")) {
						condition.setValue(ac.getConditionValue2().toString());
						condition.setValueUnit(ac.getConditionUnit2());// TODO
					} else {
						condition.setValue(ac.getConditionValue().toString());
						condition.setValueUnit(ac.getConditionUnit());// TODO
					}
					datum.setConditionCollection(new HashSet<Condition>());
					datum.getConditionCollection().add(condition);
					finding.getDatumCollection().add(datum);
					i++;
				}
			} // end of loop - iterate data matrix.

			// 4.saving Finding and then save Characterization.
			i = 0;
			for (String charKey : charMap.keySet()) {
				Cytotoxicity achar = charMap.get(charKey);
				CharacterizationBean charBean = new CharacterizationBean(achar);
				try {
					List<FindingBean> findings = charBean.getFindings();
					for (FindingBean finding : findings) {
						charService.saveFinding(finding);
					}
					charService.saveCharacterization(sampleBean, charBean);
					if (logger.isDebugEnabled()) {
						logger.debug("charBean saved for charKey: " + charKey);
					}
					i++;
				} catch (Exception e) {
					logger.error("Error saving charBean for charKey: "
							+ charKey, e);
					continue;
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("charBean# saved: " + i + ", for sample:" + name);
			}
		} // end of loop - iterate sample name map.
		if (logger.isDebugEnabled()) {
			logger.debug("Loading completed.");
		}
	}

	protected void saveLookupValue(AssayCondition ac) throws BaseException {
		Set<String> valueSet = null;
		// 1.find & save assay type.
		String assayType = ac.getAssayType();
		valueSet = LookupService.getDefaultAndOtherLookupTypes("cytotoxicity",
				"assayType", "otherAssayType");
		if (valueSet != null && !valueSet.contains(assayType)) {
			LookupService.saveOtherType("cytotoxicity", "otherAssayType",
					assayType);
			if (logger.isDebugEnabled()) {
				logger.debug("Lookup saved: cytotoxicity, otherAssaytype, "
						+ assayType);
			}
		}
		// 2.find & save datum name.
		String datumName = DATUM_TYPE_MAP.get(assayType);
		valueSet = LookupService.getDefaultAndOtherLookupTypes(assayType,
				"datumName", "otherDatumName");
		if (valueSet != null && !valueSet.contains(datumName)) {
			LookupService.saveOtherType(assayType, "otherDatumName", datumName);
			if (logger.isDebugEnabled()) {
				logger.debug("Lookup saved: " + assayType
						+ ", otherDatumName, " + datumName);
			}
		}
		// 3.find & save condition unit1.
		String conditionUnit = ac.getConditionUnit();
		valueSet = LookupService.getDefaultAndOtherLookupTypes(CONDITION_NAME,
				"unit", "otherUnit");
		if (valueSet != null && !valueSet.contains(conditionUnit)) {
			LookupService.saveOtherType(CONDITION_NAME, "otherUnit",
					conditionUnit);
			if (logger.isDebugEnabled()) {
				logger.debug("Lookup saved: " + CONDITION_NAME
						+ ", otherUnit, " + conditionUnit);
			}
		}
		// 4.find & save condition unit2.
		conditionUnit = ac.getConditionUnit2();
		valueSet = LookupService.getDefaultAndOtherLookupTypes(CONDITION_NAME,
				"unit", "otherUnit");
		if (valueSet != null && !valueSet.contains(conditionUnit)) {
			LookupService.saveOtherType(CONDITION_NAME, "otherUnit",
					conditionUnit);
			if (logger.isDebugEnabled()) {
				logger.debug("Lookup saved: " + CONDITION_NAME
						+ ", otherUnit, " + conditionUnit);
			}
		}
	}

	public static void main(String[] args) {
		if (args != null && args.length == 3) {
			String loginName = args[0];
			String password = args[1];
			String inputFileName = args[2];
			try {
				UserBean user = new UserBean(loginName, password);
				ExcelParser parser = new ExcelParser();
				SortedMap<String, SortedMap<String, Double>> verticalMatrix = parser
						.verticalParse(inputFileName);
				SortedMap<String, SortedMap<String, Double>> horizontalMatrix = parser
						.horizontalParse(inputFileName);
				ZScoreDataLoader loader = new ZScoreDataLoader(verticalMatrix,
						horizontalMatrix);
				loader.load(user);
			} catch (SecurityException e) {
				System.out.println("Can't authenticate the login name.");
				e.printStackTrace();
				System.exit(2);
			} catch (IOException e) {
				System.out.println("Input file not found.");
				e.printStackTrace();
				System.exit(3);
			} catch (BaseException e) {
				System.out.println("Error saving data.");
				e.printStackTrace();
				System.exit(4);
			}
		} else {
			System.out.println("Invalid argument!");
			System.out
					.println("java ZScoreDataLoader <loginName> <password> <inputFileName>");
		}
		System.exit(0);
	}
}

class AssayCondition {
	private String cellType;
	private String assayType;
	private Double conditionValue;
	private String conditionUnit;
	private Double conditionValue2;
	private String conditionUnit2;
	private String assayDesciption;

	public AssayCondition(String cellType, String assayType,
			Double conditionValue, String conditionUnit,
			Double conditionValue2, String conditionUnit2,
			String assayDesciption) {
		super();
		this.cellType = cellType;
		this.assayType = assayType;
		this.conditionValue = conditionValue;
		this.conditionUnit = conditionUnit;
		this.conditionValue2 = conditionValue2;
		this.conditionUnit2 = conditionUnit2;
		this.assayDesciption = assayDesciption;
	}

	public String getCellType() {
		return cellType;
	}

	public void setCellType(String cellType) {
		this.cellType = cellType;
	}

	public String getAssayType() {
		return assayType;
	}

	public void setAssayType(String assayType) {
		this.assayType = assayType;
	}

	public Double getConditionValue() {
		return conditionValue;
	}

	public void setConditionValue(Double conditionValue) {
		this.conditionValue = conditionValue;
	}

	public String getConditionUnit() {
		return conditionUnit;
	}

	public void setConditionUnit(String conditionUnit) {
		this.conditionUnit = conditionUnit;
	}

	public Double getConditionValue2() {
		return conditionValue2;
	}

	public void setConditionValue2(Double conditionValue2) {
		this.conditionValue2 = conditionValue2;
	}

	public String getConditionUnit2() {
		return conditionUnit2;
	}

	public void setConditionUnit2(String conditionUnit2) {
		this.conditionUnit2 = conditionUnit2;
	}

	public String getAssayDesciption() {
		return assayDesciption;
	}

	public void setAssayDesciption(String assayDesciption) {
		this.assayDesciption = assayDesciption;
	}
}
