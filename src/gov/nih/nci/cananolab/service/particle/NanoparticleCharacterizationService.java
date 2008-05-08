package gov.nih.nci.cananolab.service.particle;

import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.InstrumentConfiguration;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryRowBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.exception.CaNanoLabSecurityException;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.system.applicationservice.CustomizedApplicationService;
import gov.nih.nci.cananolab.util.CaNanoLabComparators;
import gov.nih.nci.cananolab.util.CaNanoLabConstants;
import gov.nih.nci.cananolab.util.ExportUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.hibernate.FetchMode;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

/**
 * Service methods involving characterizations
 * 
 * @author pansu
 * 
 */
public class NanoparticleCharacterizationService {
	private static Logger logger = Logger
			.getLogger(NanoparticleCharacterizationService.class);

	public NanoparticleCharacterizationService() {
	}

	public void saveCharacterization(NanoparticleSample particleSample,
			Characterization achar) throws Exception {

		CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
				.getApplicationService();

		if (achar.getId() != null) {
			try {
				Characterization dbChar = (Characterization) appService.load(
						Characterization.class, achar.getId());
			} catch (Exception e) {
				String err = "Object doesn't exist in the database anymore.  Please log in again.";
				logger.error(err);
				throw new ParticleCharacterizationException(err, e);
			}
		}

		if (particleSample.getCharacterizationCollection() != null) {
			particleSample.getCharacterizationCollection().clear();
		} else {
			particleSample
					.setCharacterizationCollection(new HashSet<Characterization>());
		}
		achar.setNanoparticleSample(particleSample);
		particleSample.getCharacterizationCollection().add(achar);

		// load existing instrument
		if (achar.getInstrumentConfiguration() != null
				&& achar.getInstrumentConfiguration().getInstrument() != null) {
			Instrument dbInstrument = findInstrumentBy(achar
					.getInstrumentConfiguration().getInstrument().getType(),
					achar.getInstrumentConfiguration().getInstrument()
							.getManufacturer());
			if (dbInstrument != null) {
				achar.getInstrumentConfiguration().setInstrument(dbInstrument);
			}
		}
		appService.saveOrUpdate(achar);
	}

	public Characterization findCharacterizationById(String charId)
			throws ParticleCharacterizationException,
			CaNanoLabSecurityException {
		Characterization achar = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();

			DetachedCriteria crit = DetachedCriteria.forClass(
					Characterization.class).add(
					Property.forName("id").eq(new Long(charId)));
			crit.createAlias("derivedBioAssayDataCollection", "bioassay",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("bioassay.labFile", "file",
					CriteriaSpecification.LEFT_JOIN);
			crit.setFetchMode("protocolFile", FetchMode.JOIN);
			crit.setFetchMode("derivedBioAssayDataCollection", FetchMode.JOIN);
			crit.setFetchMode("instrumentConfiguration", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			if (!result.isEmpty()) {
				achar = (Characterization) result.get(0);
			}
			return achar;
		} catch (Exception e) {
			logger.error("Problem finding the characterization by id: "
					+ charId, e);
			throw new ParticleCharacterizationException();
		}
	}

	public SortedSet<String> findAllCharacterizationSources()
			throws ParticleCharacterizationException {
		SortedSet<String> sources = new TreeSet<String>();

		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select distinct achar.source from gov.nih.nci.cananolab.domain.particle.characterization.Characterization achar where achar.source is not null");
			List results = appService.query(crit);
			for (Object obj : results) {
				String source = (String) obj;
				sources.add(source);
			}
		} catch (Exception e) {
			logger
					.error("Problem to retrieve all Characterization Sources.",
							e);
			throw new ParticleCharacterizationException(
					"Problem to retrieve all Characterization Sources ");
		}
		sources.addAll(Arrays
				.asList(CaNanoLabConstants.DEFAULT_CHARACTERIZATION_SOURCES));

		return sources;
	}

	public List<Instrument> findAllInstruments()
			throws ParticleCharacterizationException {
		List<Instrument> instruments = new ArrayList<Instrument>();

		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class)
					.add(Restrictions.isNotNull("type"));
			List results = appService.query(crit);
			for (Object obj : results) {
				Instrument instrument = (Instrument) obj;
				instruments.add(instrument);
			}
		} catch (Exception e) {
			String err = "Problem to retrieve all instruments.";
			logger.error(err, e);
			throw new ParticleCharacterizationException(err);
		}
		return instruments;
	}

	public Instrument findInstrumentBy(String instrumentType,
			String manufacturer) throws ParticleCharacterizationException {
		Instrument instrument = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Instrument.class)
					.add(Property.forName("type").eq(instrumentType)).add(
							Property.forName("manufacturer").eq(manufacturer));
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
			List results = appService.query(crit);
			for (Object obj : results) {
				instrument = (Instrument) obj;

			}
			return instrument;
		} catch (Exception e) {
			String err = "Problem to retrieve instrument.";
			logger.error(err, e);
			throw new ParticleCharacterizationException(err);
		}
	}

	// for dwr ajax
	public String getInstrumentAbbreviation(String instrumentType)
			throws ParticleCharacterizationException {
		String instrumentAbbreviation = null;
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			HQLCriteria crit = new HQLCriteria(
					"select distinct instrument.abbreviation from gov.nih.nci.cananolab.domain.common.Instrument instrument where instrument.type='"
							+ instrumentType
							+ "' and instrument.abbreviation!=null");
			List results = appService.query(crit);
			for (Object obj : results) {
				instrumentAbbreviation = (String) obj;
			}
		} catch (Exception e) {
			String err = "Problem to retrieve instrument abbreviation.";
			logger.error(err, e);
			throw new ParticleCharacterizationException(err);
		}
		return instrumentAbbreviation;
	}

	public String[] getDerivedDatumValueUnits(String derivedDatumName)
			throws ParticleCharacterizationException {
		try {
			SortedSet<String> units = LookupService.findLookupValues(
					derivedDatumName, "unit");
			return units.toArray(new String[0]);
		} catch (Exception e) {
			String err = "Error getting value unit for " + derivedDatumName;
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}

	public SortedSet<Characterization> findParticleCharacterizationsByClass(
			String particleName, String className)
			throws ParticleCharacterizationException {
		SortedSet<Characterization> charas = new TreeSet<Characterization>(
				new CaNanoLabComparators.CharacterizationDateComparator());
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			DetachedCriteria crit = DetachedCriteria.forClass(Class
					.forName(className));
			crit.createAlias("nanoparticleSample", "sample",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("derivedBioAssayDataCollection", "bioassay",
					CriteriaSpecification.LEFT_JOIN);
			crit.createAlias("bioassay.labFile", "file",
					CriteriaSpecification.LEFT_JOIN);
			crit.add(Restrictions.eq("sample.name", particleName));
			crit.setFetchMode("protocolFile", FetchMode.JOIN);
			crit.setFetchMode("derivedBioAssayDataCollection", FetchMode.JOIN);
			crit.setFetchMode("instrumentConfiguration", FetchMode.JOIN);
			crit
					.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

			List result = appService.query(crit);
			for (Object obj : result) {
				charas.add((Characterization) obj);
			}
			return charas;
		} catch (Exception e) {
			String err = "Error getting " + particleName
					+ " characterizations of type " + className;
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}

	public CharacterizationSummaryBean getParticleCharacterizationSummaryByClass(
			String particleName, String className, UserBean user)
			throws ParticleCharacterizationException {
		CharacterizationSummaryBean charSummary = new CharacterizationSummaryBean(
				className);
		try {
			SortedSet<Characterization> charas = findParticleCharacterizationsByClass(
					particleName, className);
			if (charas.isEmpty()) {
				return null;
			}
			FileService fileService = new FileService();
			for (Characterization chara : charas) {
				CharacterizationBean charBean = new CharacterizationBean(chara);
				charSummary.getCharBeans().add(charBean);
				if (charBean.getDerivedBioAssayDataList() != null
						&& !charBean.getDerivedBioAssayDataList().isEmpty()) {
					for (DerivedBioAssayDataBean derivedBioAssayDataBean : charBean
							.getDerivedBioAssayDataList()) {
						fileService.retrieveVisibility(derivedBioAssayDataBean
								.getLabFileBean(), user);
						Map<String, String> datumMap = new HashMap<String, String>();
						for (DerivedDatum data : derivedBioAssayDataBean
								.getDatumList()) {
							String datumLabel = data.getName();
							if (data.getValueUnit() != null
									&& data.getValueUnit().length() > 0) {
								datumLabel += "(" + data.getValueUnit() + ")";
							}
							datumMap
									.put(datumLabel, data.getValue().toString());
						}
						CharacterizationSummaryRowBean charSummaryRow = new CharacterizationSummaryRowBean();
						charSummaryRow.setCharBean(charBean);
						charSummaryRow.setDatumMap(datumMap);
						charSummaryRow
								.setDerivedBioAssayDataBean(derivedBioAssayDataBean);
						charSummary.getSummaryRows().add(charSummaryRow);
						if (datumMap != null && !datumMap.isEmpty()) {
							charSummary.getColumnLabels().addAll(
									datumMap.keySet());
						}
					}
				} else {
					CharacterizationSummaryRowBean charSummaryRow = new CharacterizationSummaryRowBean();
					charSummaryRow.setCharBean(charBean);
					charSummary.getSummaryRows().add(charSummaryRow);
				}
			}
			return charSummary;
		} catch (Exception e) {
			String err = "Error getting " + particleName
					+ " characterization summary of type " + className;
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}

	// set lab file visibility of a characterization
	public void retrieveVisiblity(CharacterizationBean charBean, UserBean user)
			throws ParticleCharacterizationException {
		try {
			FileService fileService = new FileService();
			for (DerivedBioAssayDataBean bioAssayData : charBean
					.getDerivedBioAssayDataList()) {
				fileService.retrieveVisibility(bioAssayData.getLabFileBean(),
						user);
			}
		} catch (Exception e) {
			String err = "Error setting visiblity for characterization "
					+ charBean.getViewTitle();
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}

	public void exportDetail(CharacterizationBean achar, OutputStream out)
			throws ParticleCharacterizationException {
		try {
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("detailSheet");
			HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
			short startRow = 0;
			setDetailSheet(achar, wb, sheet, patriarch, startRow);
			wb.write(out);
			if (out != null) {
				out.flush();
				out.close();
			}
		} catch (Exception e) {
			String err = "Error exporting detail view for "
					+ achar.getViewTitle();
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}

	}

	private short setDetailSheet(CharacterizationBean achar, HSSFWorkbook wb,
			HSSFSheet sheet, HSSFPatriarch patriarch, short rowCount) {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		HSSFRow row = null;
		HSSFCell cell = null;
		// description row
		String description = achar.getDescription();
		if (description != null) {
			row = sheet.createRow(rowCount++);
			short cellCount = 0;
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString("Description"));

			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(description));
		}

		// protocol row
		ProtocolFileBean protocolFileBean = achar.getProtocolFileBean();
		String protocolId = protocolFileBean.getDomainFile().getId().toString();
		if (protocolId != null) {
			row = sheet.createRow(rowCount++);
			short cellCount = 0;
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString("Protocol"));

			if (protocolFileBean.isHidden()) {
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString("Private protocol"));
			} else {
				StringBuffer buf = new StringBuffer();
				buf.append(protocolFileBean.getDisplayName());
				if (protocolFileBean.getDomainFile().getUri() != null) {
					buf.append(" ");
					buf.append(protocolFileBean.getDomainFile().getUri());
				}
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(buf.toString()));
			}
		}

		// instrument
		InstrumentConfiguration instrumentConfiguration = achar
				.getInstrumentConfiguration();
		Instrument instrument = instrumentConfiguration.getInstrument();
		if (instrumentConfiguration != null && instrument.getType() != null) {
			short cellCount = 0;
			row = sheet.createRow(rowCount++);
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString("Instrument"));

			StringBuffer ibuf = new StringBuffer();
			ibuf.append(instrument.getType());
			ibuf.append("-");
			ibuf.append(instrument.getManufacturer());
			if (instrument.getAbbreviation() != null
					&& instrument.getAbbreviation().length() > 0) {
				ibuf.append(" (" + instrument.getAbbreviation() + ")");
			}
			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(ibuf.toString()));

			if (instrumentConfiguration.getDescription() != null) {
				row.createCell(cellCount).setCellValue(
						new HSSFRichTextString(instrumentConfiguration
								.getDescription()));
			}
		}

		List<DerivedBioAssayDataBean> derivedBioAssayDataList = achar
				.getDerivedBioAssayDataList();
		int fileIndex = 1;

		for (DerivedBioAssayDataBean derivedBioAssayData : derivedBioAssayDataList) {
			String dDescription = derivedBioAssayData.getLabFileBean()
					.getDomainFile().getDescription();
			short cellCount = 0;
			if (dDescription != null) {
				row = sheet.createRow(rowCount++);
				cell = row.createCell(cellCount++);
				cell.setCellStyle(headerStyle);
				cell
						.setCellValue(new HSSFRichTextString(
								"Characterization File #" + fileIndex
										+ " Description"));
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(dDescription));

			}

			if (derivedBioAssayData != null
					&& derivedBioAssayData.getLabFileBean().getDomainFile()
							.getUri() != null) {
				row = sheet.createRow(rowCount++);
				cellCount = 0;
				cell = row.createCell(cellCount++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(new HSSFRichTextString(
						"Characterization File #" + fileIndex));
				/*
				 * if(derivedBioAssayData.getType() != null) {
				 * row.createCell(cellCount++).setCellValue(new
				 * HSSFRichTextString(derivedBioAssayData.getType())); }
				 */
				if (derivedBioAssayData.getLabFileBean().isHidden()) {
					row.createCell(cellCount++).setCellValue(
							new HSSFRichTextString("Private file"));
				} else {
					if (derivedBioAssayData.getLabFileBean().isImage()) {
						row.createCell(cellCount).setCellValue(
								new HSSFRichTextString(derivedBioAssayData
										.getLabFileBean().getDomainFile()
										.getTitle()));
						try {
							String filePath = derivedBioAssayData
									.getLabFileBean().getFullPath();
							HSSFClientAnchor anchor;
							short topLeftCell = cellCount;
							short bottomRightCell = (short) (cellCount + 7);
							int topLeftRow = rowCount + 1;
							int bottomRightRow = rowCount + 22;
							anchor = new HSSFClientAnchor(0, 0, 0, 255,
									topLeftCell, topLeftRow, bottomRightCell,
									bottomRightRow);
							anchor.setAnchorType(2);
							patriarch.createPicture(anchor, ExportUtils
									.loadPicture(filePath, wb));
							cellCount = bottomRightCell;
							rowCount = (short) (bottomRightRow + 3);
						} catch (IOException ioe) {
							logger
									.error(
											"Input/output problem to export detail view image data ",
											ioe);
						}

					} else { // question? download the whole file to a cell
						// if not image?
						row.createCell(cellCount++).setCellValue(
								new HSSFRichTextString(derivedBioAssayData
										.getLabFileBean().getDomainFile()
										.getTitle()));
					}

				}
			}

			List<DerivedDatum> datumList = derivedBioAssayData.getDatumList();
			if (datumList != null && !datumList.isEmpty()) {
				cellCount = 0;
				row = sheet.createRow(rowCount);
				rowCount++;
				cell = row.createCell(cellCount++);
				cell.setCellStyle(headerStyle);
				cell.setCellValue(new HSSFRichTextString(
						"Characterization Derived Data #" + fileIndex));

				row = sheet.createRow(rowCount++);
				row = sheet.createRow(rowCount++);

				short ccount = 0;
				for (DerivedDatum datum : datumList) {
					if (datum.getValueUnit() != null
							&& datum.getValueUnit().trim().length() > 0) {
						cell = row.createCell(ccount++);
						cell.setCellStyle(headerStyle);
						cell.setCellValue(new HSSFRichTextString(datum
								.getName()
								+ " (" + datum.getValueUnit() + ")"));
					} else {
						cell = row.createCell(ccount++);
						cell.setCellStyle(headerStyle);
						cell.setCellValue(new HSSFRichTextString(datum
								.getName()));
					}
				}

				row = sheet.createRow(rowCount++);
				ccount = 0;
				for (DerivedDatum datum : datumList) {
					row.createCell(ccount++)
							.setCellValue(
									new HSSFRichTextString(datum.getValue()
											.toString()));
				}
				rowCount++;
			}
			fileIndex++;
		}
		return rowCount;
	}

	public void exportFullSummary(CharacterizationSummaryBean summaryBean,
			OutputStream out) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("summarySheet");
		short startRow = 0;
		short rowCount = setSummarySheet(summaryBean, wb, sheet, startRow);
		HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
		rowCount += 2;
		for (CharacterizationBean cbean : summaryBean.getCharBeans()) {
			rowCount = setDetailSheet(cbean, wb, sheet, patriarch, rowCount);
			rowCount += 2;
		}
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	public void exportSummary(CharacterizationSummaryBean summaryBean,
			OutputStream out) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("summarySheet");
		short startRow = 0;
		setSummarySheet(summaryBean, wb, sheet, startRow);
		wb.write(out);
		if (out != null) {
			out.flush();
			out.close();
		}
	}

	private short setSummarySheet(CharacterizationSummaryBean summaryBean,
			HSSFWorkbook wb, HSSFSheet sheet, short rowCount) {
		HSSFFont headerFont = wb.createFont();
		headerFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		HSSFCellStyle headerStyle = wb.createCellStyle();
		headerStyle.setFont(headerFont);

		HSSFCellStyle newLineStyle = wb.createCellStyle();
		// Word Wrap MUST be turned on
		newLineStyle.setWrapText(true);

		short cellCount = 0;
		HSSFRow row = null;
		HSSFCell cell = null;

		// summary header
		row = sheet.createRow(rowCount);
		rowCount++;

		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("View Title"));

		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Description"));

		for (String label : summaryBean.getColumnLabels()) {
			cell = row.createCell(cellCount++);
			cell.setCellStyle(headerStyle);
			cell.setCellValue(new HSSFRichTextString(label));
		}
		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Characterization File"));

		cell = row.createCell(cellCount++);
		cell.setCellStyle(headerStyle);
		cell.setCellValue(new HSSFRichTextString("Instrument Info"));

		// data
		for (CharacterizationSummaryRowBean sbean : summaryBean
				.getSummaryRows()) {
			row = sheet.createRow(rowCount);
			rowCount++;
			cellCount = 0;

			// view title
			CharacterizationBean achar = (CharacterizationBean) sbean
					.getCharBean();
			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(achar.getViewTitle()));

			// description
			String description = achar.getDescription();
			if (description == null)
				description = "";
			row.createCell(cellCount++).setCellValue(
					new HSSFRichTextString(description));

			Map datumMap = sbean.getDatumMap();
			for (String label : summaryBean.getColumnLabels()) {
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString((String) datumMap.get(label)));
			}

			DerivedBioAssayDataBean charFile = sbean
					.getDerivedBioAssayDataBean();
			if (charFile != null) {
				StringBuffer sbuf = new StringBuffer();
				if (charFile.getLabFileBean().getDomainFile().getType() != null
						&& charFile.getLabFileBean().getDomainFile().getType()
								.length() > 0) {
					sbuf.append(charFile.getLabFileBean().getDomainFile()
							.getType()
							+ " ");
				}
				if (charFile.getLabFileBean().getDomainFile().getUri() != null) {
					if (charFile.getLabFileBean().isHidden()) {
						sbuf.append("Private file");
					} else if (charFile.getLabFileBean().getDomainFile()
							.getTitle() != null) {
						sbuf.append(charFile.getLabFileBean().getDomainFile()
								.getTitle());
					}
				}
				row.createCell(cellCount++).setCellValue(
						new HSSFRichTextString(sbuf.toString()));
			} else {
				row.createCell(cellCount++); // empty cell
			}

			// instrument
			InstrumentConfiguration instrumentConfiguration = achar
					.getInstrumentConfiguration();
			Instrument instrument = instrumentConfiguration.getInstrument();
			if (instrumentConfiguration != null && instrument.getType() != null) {
				StringBuffer sb = new StringBuffer();
				sb.append(instrument.getType());
				sb.append("-");
				sb.append(instrument.getManufacturer());

				if (instrument.getAbbreviation() != null
						&& instrument.getAbbreviation().length() > 0)
					sb.append(" (" + instrument.getAbbreviation() + ")");

				if (instrumentConfiguration.getDescription() != null)
					sb.append("  " + instrumentConfiguration.getDescription());

				cell = row.createCell(cellCount++);
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				cell.setCellValue(new HSSFRichTextString(sb.toString()));
				// cell.setCellStyle(newLineStyle);
			}

		} // for sbean
		return rowCount;
	}

	public void deleteCharacterization(Characterization chara)
			throws ParticleCharacterizationException {
		try {
			CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
					.getApplicationService();
			appService.delete(chara);
		} catch (Exception e) {
			String err = "Error deleting characterization "
					+ chara.getIdentificationName();
			logger.error(err, e);
			throw new ParticleCharacterizationException(err, e);
		}
	}
}