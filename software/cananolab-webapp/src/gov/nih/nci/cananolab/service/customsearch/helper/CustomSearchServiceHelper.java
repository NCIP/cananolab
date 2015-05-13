package gov.nih.nci.cananolab.service.customsearch.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import gov.nih.nci.cananolab.restful.customsearch.bean.CustomSearchBean;
import gov.nih.nci.cananolab.service.BaseServiceHelper;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.protocol.impl.ProtocolServiceLocalImpl;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.publication.impl.PublicationServiceLocalImpl;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.service.sample.impl.SampleServiceLocalImpl;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;
import gov.nih.nci.cananolab.util.StringUtils;

import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.store.FSDirectory;

public class CustomSearchServiceHelper extends BaseServiceHelper {
	private static Logger logger = Logger
			.getLogger(CustomSearchServiceHelper.class);
	SecurityService securityService = getSecurityService();

	public CustomSearchServiceHelper() {
		super();
	}

	public CustomSearchServiceHelper(UserBean user) {
		super(user);
	}

	public CustomSearchServiceHelper(SecurityService securityService) {
		super(securityService);
	}

	public List<CustomSearchBean> customSearchByKeywordByProtocol(HttpServletRequest httpRequest, String keyword) {
		List<CustomSearchBean> results = null;
		UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
		try {
			ProtocolService protocolService = getProtocolServiceInSession(httpRequest, securityService);
			
			results = new ArrayList<CustomSearchBean>();	
			  
		    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("indexDir"))));
		    QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		    Query query = parser.parse(keyword);        
		    TopDocs topDocs = searcher.search(query, 1000);
		    
	        ScoreDoc[] hits = topDocs.scoreDocs;
	        for (int i = 0; i < hits.length; i++) {
	            Document doc = searcher.doc(hits[i].doc);
	            CustomSearchBean searchBean = new CustomSearchBean();
	            searchBean.setId(doc.get("protocolId"));
	            searchBean.setName(doc.get("protocolName"));
	            searchBean.setType("protocol");
	            searchBean.setDescription(doc.get("protocolFileDesc"));
	            searchBean.setFileId(doc.get("protocolFileId"));
	            searchBean.setCreatedDate(doc.get("createdDate"));	
	            searchBean.setFileTitle(doc.get("protocolFileName"));
	            if(user!=null){
	            	if(user.isCurator()){
	            		searchBean.setEditable(true);
	            	}
	            	else{
	            		List<String> protoIds = protocolService.findProtocolIdsByOwner(user.getLoginName());
	            		if((searchBean.getId()!=null)&&(StringUtils.containsIgnoreCase(protoIds,
	    						searchBean.getId()))){
	    	            	searchBean.setEditable(true);
	    	            }else{
	    	            	searchBean.setEditable(false);
	    	            }
	            	}	            	
	            }
	            results.add(searchBean);
	            }
		  
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		return results;
	}

	public List<CustomSearchBean> customSearchByKeywordBySample(HttpServletRequest httpRequest, String keyword) {
		List<CustomSearchBean> results = null;
		UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
		try {
			SampleService sampleService = this.getSampleServiceInSession(httpRequest, securityService);			
			results = new ArrayList<CustomSearchBean>();	
			  
		    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("indexDir"))));
		    QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		    Query query = parser.parse(keyword);        
		    TopDocs topDocs = searcher.search(query, 100);
		    
	        ScoreDoc[] hits = topDocs.scoreDocs;
	        for (int i = 0; i < hits.length; i++) {
	            Document doc = searcher.doc(hits[i].doc);
	            CustomSearchBean searchBean = new CustomSearchBean();
	            searchBean.setId(doc.get("sampleId"));
	            searchBean.setName(doc.get("sampleName"));
	            searchBean.setType("sample");
	            searchBean.setDescription(doc.get("nanoEntityDesc"));
	            searchBean.setCreatedDate(doc.get("createdDate"));
	            if(user!=null){
	            	if(user.isCurator()){
	            		searchBean.setEditable(true);
	            	}
	            	else{
	            		List<String> sampleIds = sampleService.findSampleIdsByOwner(user.getLoginName());
	            		if((searchBean.getId()!=null)&&(StringUtils.containsIgnoreCase(sampleIds,
	    						searchBean.getId()))){
	    	            	searchBean.setEditable(true);
	    	            }else{
	    	            	searchBean.setEditable(false);
	    	            }
	            	}	            	
	            }
	            results.add(searchBean);
	            }
		  
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		return results;
	}

	public List<CustomSearchBean> customSearchByKeywordByPub(HttpServletRequest httpRequest, String keyword) {
		List<CustomSearchBean> results = null;
		UserBean user = (UserBean) (httpRequest.getSession().getAttribute("user"));
		
		try {
			
			PublicationService publicationService = this.getPublicationServiceInSession(httpRequest, securityService);				
			results = new ArrayList<CustomSearchBean>();	
			  
		    IndexSearcher searcher = new IndexSearcher(DirectoryReader.open(FSDirectory.open(new File("indexDir"))));
		    QueryParser parser = new QueryParser("content", new StandardAnalyzer());
		    Query query = parser.parse(keyword);        
		    TopDocs topDocs = searcher.search(query, 100);
		    
	        ScoreDoc[] hits = topDocs.scoreDocs;
	        for (int i = 0; i < hits.length; i++) {
	            Document doc = searcher.doc(hits[i].doc);
	            CustomSearchBean searchBean = new CustomSearchBean();
	            searchBean.setId(doc.get("publicationId"));
	            searchBean.setName(doc.get("pubTitle"));
	            searchBean.setType("publication");
	            searchBean.setDescription(doc.get("pubDesc"));
	            searchBean.setPubmedId(doc.get("pubmedId"));
	            searchBean.setCreatedDate(doc.get("createdDate"));
	            if(user!=null){
	            	if(user.isCurator()){
	            		searchBean.setEditable(true);
	            	}
	            	else{
	            		List<String> publicationIds = publicationService.findPublicationIdsByOwner(user.getLoginName());	
	            		if((searchBean.getId()!=null)&&(StringUtils.containsIgnoreCase(publicationIds,
	    						searchBean.getId()))){
	    	            	searchBean.setEditable(true);
	    	            }else{
	    	            	searchBean.setEditable(false);
	    	            }
	            	}	            	
	            }
	            results.add(searchBean);
	            }
		  
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		return results;
	}
	
	private SampleService getSampleServiceInSession(HttpServletRequest request, SecurityService securityService) {

		SampleService sampleService = (SampleService)request.getSession().getAttribute("sampleService");
		if (sampleService == null) {
			sampleService = new SampleServiceLocalImpl(securityService);
			request.getSession().setAttribute("sampleService", sampleService);
		}
		return sampleService;

	}

	private PublicationService getPublicationServiceInSession(HttpServletRequest request, SecurityService securityService)
			throws Exception {
		PublicationService publicationService = (PublicationService)request.getSession().getAttribute("publicationService");

		if (publicationService == null) {
			publicationService = new PublicationServiceLocalImpl(securityService);
			request.getSession().setAttribute("publicationService", publicationService);
		}
		return publicationService;
	}

	private ProtocolService getProtocolServiceInSession(HttpServletRequest request, SecurityService securityService)
			throws Exception {

		ProtocolService protocolService = (ProtocolService)request.getSession().getAttribute("protocolService");
		if (protocolService == null) {
			protocolService =	new ProtocolServiceLocalImpl(securityService);
			request.getSession().setAttribute("protocolService", protocolService);
		}
		return protocolService;
	}
}
