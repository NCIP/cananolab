package gov.nih.nci.cananolab.util;

import gov.nih.nci.system.client.ApplicationServiceProvider;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.LexGrid.LexBIG.DataModel.Collections.ResolvedConceptReferenceList;
import org.LexGrid.LexBIG.DataModel.Collections.SortOptionList;
import org.LexGrid.LexBIG.DataModel.Core.CodingSchemeVersionOrTag;
import org.LexGrid.LexBIG.DataModel.Core.ResolvedConceptReference;
import org.LexGrid.LexBIG.Exceptions.LBException;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet;
import org.LexGrid.LexBIG.LexBIGService.LexBIGService;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.ActiveOption;
import org.LexGrid.LexBIG.LexBIGService.CodedNodeSet.SearchDesignationOption;
import org.LexGrid.LexBIG.Utility.Constructors;
import org.LexGrid.LexBIG.Utility.LBConstants.MatchAlgorithms;
import org.apache.log4j.Logger;

/**
 *
 * @author pansu
 *
 */
public class LexBIGServiceUtils {
	private static Logger logger = Logger.getLogger(LexBIGServiceUtils.class);
	private static String evsService = "EvsServiceInfo";
	private static String serviceUrl = "http://lexevsapi51.nci.nih.gov/lexevsapi51";
	public static final String NCI_THESAURUS_SCHEME = "NCI_Thesaurus";
	public static final String NPO_SCHEME="NPO";
	public static final String EXACT_MATCH_ALGORITHM = "exact match";
	public static final String CONTAINS_ALGORITHM = "contains";
	public static final String STARTS_WITH_ALGORITHM = "starts with";

	public LexBIGServiceUtils() {
	}

	public static LexBIGService createLexBIGService() {
		LexBIGService service = null;
		try {
			service = (LexBIGService) ApplicationServiceProvider
					.getApplicationServiceFromUrl(serviceUrl, evsService);
		} catch (FileNotFoundException e) {
			logger
					.debug("Caught exception finding file for properties for EVS: "
							+ e);
			e.printStackTrace();
		} catch (IOException e) {
			logger
					.debug("Caught exception finding file for properties for EVS: "
							+ e);
			e.printStackTrace();
		} catch (Exception e) {
			logger
					.debug("Caught exception finding file for properties for EVS: "
							+ e);
			e.printStackTrace();
		}
		return service;
	}

	public static List<String> getSynonyms(String vocabularyScheme,
			String searchTerm, String matchAlgorithm) throws LBException {
		LexBIGService service = LexBIGServiceUtils.createLexBIGService();
		List<String> matchedSynonyms = new ArrayList<String>();
		String algorithm = null;
		if (matchAlgorithm.equals(LexBIGServiceUtils.CONTAINS_ALGORITHM)) {
			algorithm = MatchAlgorithms.contains.toString();
		} else if (matchAlgorithm
				.equals(LexBIGServiceUtils.STARTS_WITH_ALGORITHM)) {
			algorithm = MatchAlgorithms.startsWith.toString();
		} else {
			algorithm = MatchAlgorithms.exactMatch.toString();
		}
		if (vocabularyScheme != null) {
			CodingSchemeVersionOrTag csvt = new CodingSchemeVersionOrTag();
			CodedNodeSet codedNodeSet = service.getCodingSchemeConcepts(
					vocabularyScheme, csvt);
			codedNodeSet = codedNodeSet.restrictToStatus(
					ActiveOption.ACTIVE_ONLY, null);
			codedNodeSet = codedNodeSet.restrictToMatchingDesignations(
					searchTerm, SearchDesignationOption.ALL,
					algorithm, null);

			// Sort by search engine recommendation & code ...
			SortOptionList sortCriteria = Constructors
					.createSortOptionList(new String[] { "matchToQuery", "code" });
			ResolvedConceptReferenceList matches = codedNodeSet.resolveToList(
					sortCriteria, null, null, 500);

			if (matches.getResolvedConceptReferenceCount() > 0) {
				for (Enumeration refs = matches
						.enumerateResolvedConceptReference(); refs
						.hasMoreElements();) {
					ResolvedConceptReference ref = (ResolvedConceptReference) refs
							.nextElement();
					matchedSynonyms
							.add(ref.getEntityDescription().getContent());
				}
			}
		} else {
			logger.error("Please provide a valid vocabulary scheme");
		}
		return matchedSynonyms;
	}

	public static String getServiceUrL() {
		return serviceUrl;
	}
}
