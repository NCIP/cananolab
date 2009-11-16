package gov.nih.nci.cananolab.service.sample;

import gov.nih.nci.cananolab.domain.characterization.invitro.Cytotoxicity;
import gov.nih.nci.cananolab.domain.common.Condition;
import gov.nih.nci.cananolab.domain.common.Datum;
import gov.nih.nci.cananolab.domain.common.Finding;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.util.DateUtils;
import gov.nih.nci.cananolab.util.ExcelParser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
		// TODO verify with Michal
		ASSAY_TYPE_MAP.put("APO", "caspase 3 apoptosis");
		ASSAY_TYPE_MAP.put("JC1", "mitochondrial membrane potential");
		ASSAY_TYPE_MAP.put("RES", "intracellular metabolism indicator");
		ASSAY_TYPE_MAP.put("CTG", "cell viability");
	}

	public final static Map<String, String> DATUM_TYPE_MAP = new HashMap<String, String>();
	static {
		// TODO verify with Michal
		DATUM_TYPE_MAP.put("caspase 3 apoptosis", "% control");
		DATUM_TYPE_MAP.put("mitochondrial membrane potential",
				"ratio of red to green fluorescence");
		DATUM_TYPE_MAP.put("intracellular metabolism indicator", "question");
		DATUM_TYPE_MAP.put("cell viability", "% cell viability");
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

	private Map<String, String> sampleNameMap = new HashMap<String, String>();
	private Map<String, AssayCondition> assayMap = new HashMap<String, AssayCondition>();
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
			if (name.matches("([A-Z]+)_([A-Z]+)_(.+)")) {
				String cellType = name.replaceAll("([A-Z]+)_([A-Z]+)_(.+)",
						"$1");
				String assayType = name.replaceAll("([A-Z]+)_([A-Z]+)_(.+)",
						"$2");
				String conditionString = name.replaceAll(
						"([A-Z]+)_([A-Z]+)_(.+)", "$3");

				AssayCondition assayCondition = new AssayCondition(
						CELL_TYPE_MAP.get(cellType), ASSAY_TYPE_MAP
								.get(assayType), FE_DOSE_MAP
								.get(conditionString), "mg/ml", QDOT_DOSE_MAP
								.get(conditionString), "nM");
				assayMap.put(name, assayCondition);
			}
		}
	}

	public void load(UserBean user) {
		// SampleService service = new SampleServiceLocalImpl();
		for (String name : sampleNameMap.keySet()) {
			String sampleName = sampleNameMap.get(name);
			// try {
			// SampleBean sampleBean = service.findSampleByName(sampleName,
			// user);
			// } catch (Exception e) {
			// logger.error(e);
			// }
			Map<String, Cytotoxicity> charMap = new HashMap<String, Cytotoxicity>();
			SortedMap<String, Double> data = dataMatrix.get(name);
			int i=0;
			for (String assayStr : data.keySet()) {
				AssayCondition ac = assayMap.get(assayStr);
				Double dataValue = data.get(assayStr);

				Cytotoxicity achar = null;
				Finding finding = null;
				if (ac != null) {
					if (charMap
							.get(ac.getCellType() + "||" + ac.getAssayType()) != null) {
						achar = charMap.get(ac.getCellType() + "||"
								+ ac.getAssayType());
						for (Finding aFinding : achar.getFindingCollection()) {
							finding = aFinding;
							break;
						}

					} else {
						i=0;
						achar = new Cytotoxicity();
						achar.setCellLine(ac.getCellType());
						achar.setAssayType(ac.getAssayType());
						achar.setFindingCollection(new HashSet<Finding>());
						finding = new Finding();
						achar.getFindingCollection().add(finding);
						finding.setDatumCollection(new HashSet<Datum>());
						charMap.put(
								ac.getCellType() + "||" + ac.getAssayType(),
								achar);
					}
					Datum datum = new Datum();
					datum.setValue(dataValue.floatValue());
					datum.setName(DATUM_TYPE_MAP.get(ac.getAssayType()));
					datum.setValueType("Z-score");
					datum.setCreatedBy("AUTO_PARSER");
					datum.setCreatedDate(DateUtils.addSecondsToCurrentDate(i));
					Condition condition = new Condition();
					condition.setName("sample concentration");
					//NP49, NP50, NP51 are QDots
					if (sampleName.matches(SAMPLE_NAME_PREFIX + "49")
							|| sampleName.matches(SAMPLE_NAME_PREFIX + "50")
							|| sampleName.matches(SAMPLE_NAME_PREFIX + "51")) {
						condition.setValue(ac.getConditionValue2().toString());
						condition.setValueUnit(ac.getConditionUnit2());
					} else {
						condition.setValue(ac.getConditionValue().toString());
						condition.setValueUnit(ac.getConditionUnit());
					}
					condition.setCreatedBy("AUTO_PARSER");
					condition.setCreatedDate(new Date());
					datum.setConditionCollection(new HashSet<Condition>());
					datum.getConditionCollection().add(condition);
					finding.getDatumCollection().add(datum);
					i++;
				}
				// TODO saving characterization and finding
			}
		}
	}

	public static void main(String[] args) {
		if (args != null && args.length == 3) {
			String loginName = args[0];
			String password = args[1];
			String inputFileName = args[2];
			ZScoreDataLoader loader = null;
			try {
				ExcelParser parser = new ExcelParser(inputFileName);
				SortedMap<String, SortedMap<String, Double>> verticalMatrix = parser
						.verticalParse();
				SortedMap<String, SortedMap<String, Double>> horizontalMatrix = parser
						.horizontalParse();
				loader = new ZScoreDataLoader(verticalMatrix, horizontalMatrix);
			} catch (IOException e) {
				System.out.println("Input file not found.");
				e.printStackTrace();
				System.exit(0);
			}
			// try {
			// LoginService loginService = new LoginService(
			// Constants.CSM_APP_NAME);
			// UserBean user = loginService.login(loginName, password);
			// if (user.isCurator()) {
			loader.load(null);
			// } else {
			// System.out
			// .println("You need to be the curator to be able to execute this function");
			// System.exit(0);
			// }
			// } catch (SecurityException e) {
			// System.out.println("Can't authenticate the login name.");
			// e.printStackTrace();
			// System.exit(0);
			// }
		} else {
			System.out.println("Invalid argument!");
			System.out
					.println("java ZScoreDataLoader <loginName> <password> <inputFileName>");
		}
		System.exit(1);
	}
}

class AssayCondition {
	private String cellType;
	private String assayType;
	private Double conditionValue;
	private String conditionUnit;
	private Double conditionValue2;
	private String conditionUnit2;

	public AssayCondition(String cellType, String assayType,
			Double conditionValue, String conditionUnit,
			Double conditionValue2, String conditionUnit2) {
		super();
		this.cellType = cellType;
		this.assayType = assayType;
		this.conditionValue = conditionValue;
		this.conditionUnit = conditionUnit;
		this.conditionValue2 = conditionValue2;
		this.conditionUnit2 = conditionUnit2;
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
}
