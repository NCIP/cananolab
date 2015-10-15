package gov.nih.nci.cananolab.restful.customsearch;

import gov.nih.nci.cananolab.domain.common.Author;
import gov.nih.nci.cananolab.domain.common.File;
import gov.nih.nci.cananolab.domain.common.Keyword;
import gov.nih.nci.cananolab.domain.common.Protocol;
import gov.nih.nci.cananolab.domain.common.Publication;
import gov.nih.nci.cananolab.domain.particle.Characterization;
import gov.nih.nci.cananolab.domain.particle.Function;
import gov.nih.nci.cananolab.domain.particle.FunctionalizingEntity;
import gov.nih.nci.cananolab.domain.particle.NanomaterialEntity;
import gov.nih.nci.cananolab.domain.particle.Sample;
import gov.nih.nci.cananolab.domain.particle.SampleComposition;
import gov.nih.nci.cananolab.exception.NoAccessException;
import gov.nih.nci.cananolab.restful.customsearch.bean.ProtocolSearchableFieldsBean;
import gov.nih.nci.cananolab.restful.customsearch.bean.PublicationSearchableFieldsBean;
import gov.nih.nci.cananolab.restful.customsearch.bean.SampleSearchableFieldsBean;
import gov.nih.nci.cananolab.service.BaseServiceHelper;
import gov.nih.nci.cananolab.service.protocol.helper.ProtocolServiceHelper;
import gov.nih.nci.cananolab.service.publication.helper.PublicationServiceHelper;
import gov.nih.nci.cananolab.service.sample.helper.SampleServiceHelper;
import gov.nih.nci.cananolab.system.applicationservice.CaNanoLabApplicationService;
import gov.nih.nci.cananolab.util.StringUtils;
import gov.nih.nci.system.client.ApplicationServiceProvider;
import gov.nih.nci.system.query.hibernate.HQLCriteria;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;

public class CustomSearchEngine extends BaseServiceHelper {

	SampleServiceHelper sampleHelper;
	ProtocolServiceHelper protocolHelper;
	PublicationServiceHelper publicationHelper;
	CaNanoLabApplicationService appService;
	public List<SampleSearchableFieldsBean> retrieveSamplesForIndexing() {
		List <SampleSearchableFieldsBean> sampleResults = new ArrayList<SampleSearchableFieldsBean>();
		try {
			appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
			List<String> accessibledata = getAccessibleData();	

			sampleHelper = new SampleServiceHelper();

			List<String> publicSamples = sampleHelper.getAllSamples();

			for (int i = 0; i < publicSamples.size(); i++) {
				Long id = Long.valueOf(publicSamples.get(i));

//				if (StringUtils.containsIgnoreCase(getAccessibleData(),
//						publicSamples.get(i))) {

					DetachedCriteria sampleCriteria = DetachedCriteria
							.forClass(Sample.class).add(
									Property.forName("id").eq(id));

					sampleCriteria.setFetchMode("primaryPointOfContact",
							FetchMode.EAGER);
					sampleCriteria.setFetchMode("sampleComposition",
							FetchMode.EAGER);
					sampleCriteria.setFetchMode(
							"sampleComposition.nanomaterialEntityCollection",
							FetchMode.EAGER);
					sampleCriteria
							.setFetchMode(
									"sampleComposition.functionalizingEntityCollection",
									FetchMode.EAGER);
					sampleCriteria
							.setFetchMode(
									"sampleComposition.nanomaterialEntityCollection.composingElementCollection",
									FetchMode.EAGER);
					sampleCriteria
							.setFetchMode(
									"sampleComposition.nanomaterialEntityCollection.composingElementCollection.inherentFunctionCollection",
									FetchMode.EAGER);
					sampleCriteria
							.setFetchMode(
									"sampleComposition.functionalizingEntityCollection.functionCollection",
									FetchMode.EAGER);
					sampleCriteria.setFetchMode("keywordCollection",
							FetchMode.EAGER);
					sampleCriteria.setFetchMode("publicationCollection",
							FetchMode.EAGER);
					sampleCriteria.setFetchMode("characterizationCollection",
							FetchMode.EAGER);

					sampleCriteria
							.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
					Sample sample = null;
					List result = appService.query(sampleCriteria);
					if (!result.isEmpty()) {
						sample = (Sample) result.get(0);
					}

					SampleSearchableFieldsBean sampleFieldsBean = new SampleSearchableFieldsBean();
					sampleFieldsBean.setSampleId(sample.getId().toString());
					sampleFieldsBean.setSampleName(sample.getName());
					sampleFieldsBean.setCreatedDate(sample.getCreatedDate());
					sampleFieldsBean.setSamplePocName(sample
							.getPrimaryPointOfContact().getFirstName());
					Collection<Keyword> keywordCollection = sample
							.getKeywordCollection();
					Iterator<Keyword> keywordItr = keywordCollection.iterator();
					List<String> keywords = new ArrayList<String>();
					while (keywordItr.hasNext()) {
						Keyword keywrd = keywordItr.next();
						keywords.add(keywrd.getName());
					}
					sampleFieldsBean.setSampleKeywords(keywords);
					// sampleFieldsBean.setNanoEntityName(nanoEntityName);
					SampleComposition sampleComp = null;
					if (sample.getSampleComposition() != null) {
						sampleComp = sample.getSampleComposition();
						Collection<NanomaterialEntity> nanoCollection = null;
						if (sampleComp.getNanomaterialEntityCollection() != null) {
							nanoCollection = sampleComp
									.getNanomaterialEntityCollection();
						}
						Iterator<NanomaterialEntity> itr = nanoCollection
								.iterator();
						while (itr.hasNext()) {
							NanomaterialEntity nanoEntity = itr.next();
							sampleFieldsBean.setNanoEntityDesc(nanoEntity
									.getDescription());
							sampleFieldsBean.setNanoEntityName(nanoEntity
									.getClass().getSimpleName());
						}

						Collection<FunctionalizingEntity> funcCollection = null;
						if (sampleComp.getFunctionalizingEntityCollection() != null) {
							funcCollection = sampleComp
									.getFunctionalizingEntityCollection();
						}
						Iterator<FunctionalizingEntity> funcitr = funcCollection
								.iterator();
						while (funcitr.hasNext()) {
							FunctionalizingEntity funcEntity = funcitr.next();
							sampleFieldsBean.setFuncEntityName(funcEntity
									.getClass().getSimpleName());
							if (funcEntity.getFunctionCollection() != null) {
								Collection functionCollection = funcEntity
										.getFunctionCollection();
								Iterator<Function> functionItr = functionCollection
										.iterator();
								while (functionItr.hasNext()) {
									Function function = functionItr.next();
									sampleFieldsBean.setFunction(function
											.getClass().getSimpleName());
								}
							}
						}
					}

					if (sample.getCharacterizationCollection() != null) {
						Collection charCollection = sample
								.getCharacterizationCollection();

						Iterator<Characterization> itr = charCollection
								.iterator();
						while (itr.hasNext()) {
							Characterization charc = itr.next();
							sampleFieldsBean.setCharacterization(charc
									.getClass().getSimpleName());
						}
					}

					sampleResults.add(sampleFieldsBean);
				//}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("sample Results size ==="+sampleResults.size());
		return sampleResults;
	}

	public List<ProtocolSearchableFieldsBean> retrieveProtocolsForIndexing() {
		List<ProtocolSearchableFieldsBean> protocolResults = new ArrayList<ProtocolSearchableFieldsBean>();
		try {
			appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
			protocolHelper = new ProtocolServiceHelper();

			List<String> publicProtocols = protocolHelper.getAllProtocols();
			

			for (int i = 0; i < publicProtocols.size(); i++) {
				Long id = Long.valueOf(publicProtocols.get(i));
//				if (StringUtils.containsIgnoreCase(getAccessibleData(),
//						publicProtocols.get(i))) {

					DetachedCriteria protocolCriteria = DetachedCriteria
							.forClass(Protocol.class).add(
									Property.forName("id").eq(id));

					protocolCriteria.setFetchMode("file", FetchMode.JOIN);
					protocolCriteria.setFetchMode("file.keywordCollection",
							FetchMode.JOIN);
					protocolCriteria
							.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

					Protocol protocol = null;

					List result = appService.query(protocolCriteria);
					if (!result.isEmpty()) {
						protocol = (Protocol) result.get(0);
					}
					ProtocolSearchableFieldsBean protocolFieldsBean = new ProtocolSearchableFieldsBean();					
					protocolFieldsBean.setCreatedDate(protocol.getCreatedDate());
					protocolFieldsBean.setProtocolId(protocol.getId().toString());
					protocolFieldsBean.setProtocolName(protocol.getName());
					File file = protocol.getFile();
					if(file!=null){
						if(file.getTitle()!=null)
							protocolFieldsBean.setProtocolFileName(protocol.getFile()
								.getTitle());
						if(file.getDescription()!=null)
							protocolFieldsBean.setProtocolFileDesc(protocol.getFile().getDescription());
						if(file.getId()!=null)
							protocolFieldsBean.setProtocolFileId(protocol.getFile().getId().toString());
					}
					protocolResults.add(protocolFieldsBean);
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return protocolResults;
	}

	public List<PublicationSearchableFieldsBean> retrievePublicationForIndexing() {
		List<PublicationSearchableFieldsBean> pubResults = new ArrayList<PublicationSearchableFieldsBean>();
		try {
			appService = (CaNanoLabApplicationService) ApplicationServiceProvider
					.getApplicationService();
			publicationHelper = new PublicationServiceHelper();
			List<String> publicPublications = publicationHelper
					.getAllPublications();

			for (int i = 0; i < publicPublications.size(); i++) {
				Long id = Long.valueOf(publicPublications.get(i));
//				if (StringUtils.containsIgnoreCase(getAccessibleData(),
//						publicPublications.get(i))) {
					DetachedCriteria publicationCrit = DetachedCriteria
							.forClass(Publication.class).add(
									Property.forName("id").eq(id));

					publicationCrit.setFetchMode("keywordCollection",
							FetchMode.JOIN);
					publicationCrit.setFetchMode("authorCollection",
							FetchMode.JOIN);

					publicationCrit
							.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);

					Publication pub = null;

					List result = appService.query(publicationCrit);
					if (!result.isEmpty()) {
						pub = (Publication) result.get(0);
					}
					
					//Getting associated sample names
					
					String query = "select sample.name, sample.id from gov.nih.nci.cananolab.domain.particle.Sample as sample join sample.publicationCollection as pub where pub.id='"
							+ id + "'";
					HQLCriteria crit = new HQLCriteria(query);
					CaNanoLabApplicationService appService = (CaNanoLabApplicationService) ApplicationServiceProvider
							.getApplicationService();
					List results = appService.query(crit);
					List<String> sampleNames = new ArrayList<String>();
					for (int j = 0; j < results.size(); j++) {
						Object[] row = (Object[]) results.get(j);
						String sampleName = row[0].toString();
						String sampleId = row[1].toString();
						if (StringUtils.containsIgnoreCase(getAccessibleData(), sampleId)) {
							sampleNames.add(sampleName);
						} else {
							logger.debug("User doesn't have access to sample " + sampleName);
						}
					}

					PublicationSearchableFieldsBean publicationFieldsBean = new PublicationSearchableFieldsBean();
					publicationFieldsBean.setCreatedDate(pub.getCreatedDate());
					publicationFieldsBean.setPublicationId(pub.getId().toString());
					publicationFieldsBean.setPubTitle(pub.getTitle());
					publicationFieldsBean.setPubDesc(pub.getDescription());
					publicationFieldsBean.setSampleName(sampleNames);
					if(pub.getPubMedId()!=null)
						publicationFieldsBean.setPubmedId(pub.getPubMedId().toString());
					publicationFieldsBean.setDoiId(pub.getDigitalObjectId());

					if (pub.getAuthorCollection() != null) {
						Collection authCollection = pub.getAuthorCollection();
						Iterator<Author> authItr = authCollection.iterator();
						while (authItr.hasNext()) {
							Author author = authItr.next();
							List<String> authors = new ArrayList<String>();
							authors.add(author.getFirstName());
							authors.add(author.getLastName());
							publicationFieldsBean.setAuthors(authors);
						}
					}
					if (pub.getKeywordCollection() != null) {
						Collection pubkeywordCollection = pub
								.getKeywordCollection();
						Iterator<Keyword> keyItr = pubkeywordCollection
								.iterator();
						while (keyItr.hasNext()) {
							Keyword pubKeyword = keyItr.next();
							List<String> pubKeywords = new ArrayList<String>();
							pubKeywords.add(pubKeyword.getName());
							publicationFieldsBean.setPubKeywords(pubKeywords);
						}
					}
					pubResults.add(publicationFieldsBean);
				//}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pubResults;
	}
}
