package gov.nih.nci.cananolab.service.particle.impl;

import gov.nih.nci.cagrid.cananolab.client.CaNanoLabServiceClient;
import gov.nih.nci.cagrid.cqlquery.Attribute;
import gov.nih.nci.cagrid.cqlquery.CQLQuery;
import gov.nih.nci.cagrid.cqlquery.Predicate;
import gov.nih.nci.cagrid.cqlresultset.CQLQueryResults;
import gov.nih.nci.cagrid.data.utilities.CQLQueryResultsIterator;
import gov.nih.nci.cananolab.domain.common.DerivedBioAssayData;
import gov.nih.nci.cananolab.domain.common.DerivedDatum;
import gov.nih.nci.cananolab.domain.common.Instrument;
import gov.nih.nci.cananolab.domain.common.InstrumentConfiguration;
import gov.nih.nci.cananolab.domain.particle.NanoparticleSample;
import gov.nih.nci.cananolab.domain.particle.characterization.Characterization;
import gov.nih.nci.cananolab.dto.common.ProtocolFileBean;
import gov.nih.nci.cananolab.dto.common.UserBean;
import gov.nih.nci.cananolab.dto.particle.ParticleBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryBean;
import gov.nih.nci.cananolab.dto.particle.characterization.CharacterizationSummaryRowBean;
import gov.nih.nci.cananolab.dto.particle.characterization.DerivedBioAssayDataBean;
import gov.nih.nci.cananolab.exception.DuplicateEntriesException;
import gov.nih.nci.cananolab.exception.ParticleCharacterizationException;
import gov.nih.nci.cananolab.exception.ParticleException;
import gov.nih.nci.cananolab.service.common.FileService;
import gov.nih.nci.cananolab.service.common.LookupService;
import gov.nih.nci.cananolab.service.particle.NanoparticleCharacterizationService;
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
 * Service methods involving remote characterizations
 * 
 * @author tanq
 * 
 */
public class NanoparticleCharacterizationServiceRemoteImpl implements NanoparticleCharacterizationService{
	private static Logger logger = Logger
			.getLogger(NanoparticleCharacterizationServiceRemoteImpl.class);
	private CaNanoLabServiceClient gridClient;
	public NanoparticleCharacterizationServiceRemoteImpl(String serviceUrl) 
		throws Exception{
		gridClient = new CaNanoLabServiceClient(serviceUrl);
	}

	public void saveCharacterization(NanoparticleSample particleSample,
			Characterization achar) throws Exception {
		throw new ParticleException("Not implemented for grid service");
	}

	public Characterization findCharacterizationById(String charId)
			throws ParticleCharacterizationException {
		try {
			CQLQuery query = new CQLQuery();
			gov.nih.nci.cagrid.cqlquery.Object target = new gov.nih.nci.cagrid.cqlquery.Object();
			target
					.setName("gov.nih.nci.cananolab.domain.particle.characterization.Characterization");
			Attribute attribute = new Attribute();
			attribute.setName("id");
			attribute.setPredicate(Predicate.EQUAL_TO);
			attribute.setValue(charId);
			target.setAttribute(attribute);
			query.setTarget(target);
			CQLQueryResults results = gridClient.query(query);
			results
					.setTargetClassname("gov.nih.nci.cananolab.domain.particle.characterization.Characterization");
			CQLQueryResultsIterator iter = new CQLQueryResultsIterator(results);
			Characterization achar = null;
			while (iter.hasNext()) {
				java.lang.Object obj = iter.next();
				achar = (Characterization) obj;
				loadCharacterizationAssociations(achar);			
			}
			return achar;			
		} catch (Exception e) {
			logger.error("Problem finding the characterization by id: "
					+ charId, e);
			throw new ParticleCharacterizationException();
		}
	}

	public Boolean checkRedundantViewTitle(NanoparticleSample particleSample,
			Characterization chara) throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException("Not implemented for grid service");		
	}

	public SortedSet<String> findAllCharacterizationSources()
			throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException("Not implemented for grid service");
	}

	public List<Instrument> findAllInstruments()
			throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException("Not implemented for grid service");
	}

	public Instrument findInstrumentBy(String instrumentType,
			String manufacturer) throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException("Not implemented for grid service");
	}

	// for dwr ajax
	public String getInstrumentAbbreviation(String instrumentType)
			throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException("Not implemented for grid service");
	}

	// use in dwr ajax
	public String[] getDerivedDatumValueUnits(String derivedDatumName)
			throws ParticleCharacterizationException {
		throw new ParticleCharacterizationException("Not implemented for grid service");
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
		throw new ParticleCharacterizationException("Not implemented for grid service");
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
		String protocolId = null;
		if (protocolFileBean.getDomainFile() != null
				&& protocolFileBean.getDomainFile().getId() != null) {
			protocolId = protocolFileBean.getDomainFile().getId().toString();
		}
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
	
	/**
	 * Get all the associated data of a Characterization
	 * @param particleSample
	 * @throws Exception
	 */
	private void loadCharacterizationAssociations(Characterization achar)throws Exception{
		String charId = achar.getId().toString();
		//TODO Qina
		
		/*CustomizedApplicationService appService = (CustomizedApplicationService) ApplicationServiceProvider
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
		return achar;*/
	}
}