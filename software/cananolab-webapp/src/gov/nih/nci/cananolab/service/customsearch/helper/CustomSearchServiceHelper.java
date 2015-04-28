package gov.nih.nci.cananolab.service.customsearch.helper;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import gov.nih.nci.cananolab.restful.customsearch.bean.CustomSearchBean;
import gov.nih.nci.cananolab.service.BaseServiceHelper;
import gov.nih.nci.cananolab.service.security.SecurityService;
import gov.nih.nci.cananolab.service.security.UserBean;

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

	public CustomSearchServiceHelper() {
		super();
	}

	public CustomSearchServiceHelper(UserBean user) {
		super(user);
	}

	public CustomSearchServiceHelper(SecurityService securityService) {
		super(securityService);
	}

	public List<CustomSearchBean> customSearchByKeywordByProtocol(String keyword) {
		List<CustomSearchBean> results = null;
		try {
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
	            searchBean.setCreatedDate(doc.get("createdDate"));
	            results.add(searchBean);
	            }
		  
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		return results;
	}

	public List<CustomSearchBean> customSearchByKeywordBySample(String keyword) {
		List<CustomSearchBean> results = null;
		try {
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
	            results.add(searchBean);
	            }
		  
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		return results;
	}

	public List<CustomSearchBean> customSearchByKeywordByPub(String keyword) {
		List<CustomSearchBean> results = null;
		try {
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
	            searchBean.setCreatedDate(doc.get("createdDate"));
	            results.add(searchBean);
	            }
		  
		    
		}catch(Exception e){
			e.printStackTrace();
		}
		return results;
	}
}
