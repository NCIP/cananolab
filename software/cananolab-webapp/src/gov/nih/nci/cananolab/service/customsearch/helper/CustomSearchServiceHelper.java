package gov.nih.nci.cananolab.service.customsearch.helper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import gov.nih.nci.cananolab.restful.customsearch.bean.CustomSearchBean;
import gov.nih.nci.cananolab.security.CananoUserDetails;
import gov.nih.nci.cananolab.security.utils.SpringSecurityUtil;
import gov.nih.nci.cananolab.service.protocol.ProtocolService;
import gov.nih.nci.cananolab.service.publication.PublicationService;
import gov.nih.nci.cananolab.service.sample.SampleService;
import gov.nih.nci.cananolab.util.StringUtils;

@Component("customSearchServiceHelper")
public class CustomSearchServiceHelper
{
	private static Logger logger = Logger.getLogger(CustomSearchServiceHelper.class);
	
	@Autowired
	private SampleService sampleService;
	
	@Autowired
	private ProtocolService protocolService;
	
	@Autowired
	private PublicationService publicationService;

	public List<CustomSearchBean> customSearchByKeywordByProtocol(HttpServletRequest httpRequest, String keyword) {
		List<CustomSearchBean> results = null;
		CananoUserDetails user = SpringSecurityUtil.getPrincipal();
		FSDirectory fsDirectory = null;
		DirectoryReader directoryReader = null;
		try {
			results = new ArrayList<CustomSearchBean>();
			fsDirectory = FSDirectory.open(new File("indexDir"));
			directoryReader = DirectoryReader.open(fsDirectory);
			  
		    IndexSearcher searcher = new IndexSearcher(directoryReader);
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
	            //searchBean.setCreatedDate(doc.get("createdDate"));	
	            String date = this.parseDate(doc.get("createdDate"));
	            searchBean.setCreatedDate(date);
	            searchBean.setFileTitle(doc.get("protocolFileName"));
	            if (user != null){
	            	if(user.isCurator()){
	            		searchBean.setEditable(true);
	            	}
	            	else{
	            		List<String> protoIds = protocolService.findProtocolIdsByOwner(user.getUsername());
	            		if((searchBean.getId()!=null)&&(StringUtils.containsIgnoreCase(protoIds, searchBean.getId()))){
	            			searchBean.setEditable(true);
	            		}else{
	            			searchBean.setEditable(false);
	            		}
	            	}	            	
	            }
	            results.add(searchBean);
	        }
		  
		    
		}catch(Exception e){
			logger.error("Error in customSearchByKeywordByProtocol printing stack trace", e);
		}
		finally {
			if (fsDirectory != null)
				fsDirectory.close();
			try {
				if (directoryReader != null)
					directoryReader.close();
			}
			catch (IOException e) {
				logger.error("Error in customSearchByKeywordByProtocol when trying to close DirectoryReader", e);
			}
		}
		return results;
	}

	private String parseDate(String date) {
		String month = date.substring(4, 7);
		String day = date.substring(8, 10);
		String year = date.substring(24);
		String createdDate = "";
		if(month.equalsIgnoreCase("jan"))
			month = "1";
		if(month.equalsIgnoreCase("feb"))
			month = "2";
		if(month.equalsIgnoreCase("mar"))
			month = "3";
		if(month.equalsIgnoreCase("apr"))
			month = "4";
		if(month.equalsIgnoreCase("may"))
			month = "5";
		if(month.equalsIgnoreCase("jun"))
			month = "6";
		if(month.equalsIgnoreCase("jul"))
			month = "7";
		if(month.equalsIgnoreCase("aug"))
			month = "8";
		if(month.equalsIgnoreCase("sep"))
			month = "9";
		if(month.equalsIgnoreCase("oct"))
			month = "10";
		if(month.equalsIgnoreCase("nov"))
			month = "11";
		if(month.equalsIgnoreCase("dec"))
			month = "12";
		createdDate = month+"/"+day+"/"+year;
		return createdDate;
	}

	public List<CustomSearchBean> customSearchByKeywordBySample(HttpServletRequest httpRequest, String keyword) {
		List<CustomSearchBean> results = null;
		CananoUserDetails user = SpringSecurityUtil.getPrincipal();
		FSDirectory fsDirectory = null;
		DirectoryReader directoryReader = null;

		try {
			results = new ArrayList<CustomSearchBean>();	
			  
			fsDirectory = FSDirectory.open(new File("indexDir"));
			directoryReader = DirectoryReader.open(fsDirectory);
			  
		    IndexSearcher searcher = new IndexSearcher(directoryReader);
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
	            String date = this.parseDate(doc.get("createdDate"));
	            searchBean.setCreatedDate(date);
	            if(user!=null){
	            	if(user.isCurator()){
	            		searchBean.setEditable(true);
	            	}
	            	else{
	            		List<String> sampleIds = sampleService.findSampleIdsByOwner(user.getUsername());
	            		if((searchBean.getId()!=null)&&(StringUtils.containsIgnoreCase(sampleIds, searchBean.getId()))){
	    	            	searchBean.setEditable(true);
	    	            }else{
	    	            	searchBean.setEditable(false);
	    	            }
	            	}	            	
	            }
	            results.add(searchBean);
	            }
		  
		    
		}catch(Exception e){
			logger.error("Error in customSearchByKeywordBySample printing stack trace", e);
			e.printStackTrace();
		}
		finally {
			if (fsDirectory != null)
				fsDirectory.close();
			try {
				if (directoryReader != null)
					directoryReader.close();
			}
			catch (IOException e) {
				logger.error("Error in customSearchByKeywordBySample when trying to close DirectoryReader", e);
			}
		}		
		return results;
	}

	public List<CustomSearchBean> customSearchByKeywordByPub(HttpServletRequest httpRequest, String keyword) {
		List<CustomSearchBean> results = null;
		CananoUserDetails user = SpringSecurityUtil.getPrincipal();
		FSDirectory fsDirectory = null;
		DirectoryReader directoryReader = null;
		
		try {
			
			results = new ArrayList<CustomSearchBean>();	
			
			fsDirectory = FSDirectory.open(new File("indexDir"));
			directoryReader = DirectoryReader.open(fsDirectory);
			  
		    IndexSearcher searcher = new IndexSearcher(directoryReader);
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
	            String date = this.parseDate(doc.get("createdDate"));
	            searchBean.setCreatedDate(date);
	            if(user!=null){
	            	if(user.isCurator()){
	            		searchBean.setEditable(true);
	            	}
	            	else{
	            		List<String> publicationIds = publicationService.findPublicationIdsByOwner(user.getUsername());	
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
			logger.error("Error in customSearchByKeywordByPub printing stack trace", e);
			e.printStackTrace();
		}
		finally {
			if (fsDirectory != null)
				fsDirectory.close();
			try {
				if (directoryReader != null)
					directoryReader.close();
			}
			catch (IOException e) {
				logger.error("Error in customSearchByKeywordByPub when trying to close DirectoryReader", e);
			}
		}		
		return results;
	}
	
}
