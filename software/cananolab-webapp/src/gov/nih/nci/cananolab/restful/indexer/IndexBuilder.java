package gov.nih.nci.cananolab.restful.indexer;

import gov.nih.nci.cananolab.restful.customsearch.CustomSearchEngine;
import gov.nih.nci.cananolab.restful.customsearch.bean.ProtocolSearchableFieldsBean;
import gov.nih.nci.cananolab.restful.customsearch.bean.PublicationSearchableFieldsBean;
import gov.nih.nci.cananolab.restful.customsearch.bean.SampleSearchableFieldsBean;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.document.DateTools;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.LongField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.StringField;

public class IndexBuilder {
private IndexWriter indexWriter = null;
    
    public IndexWriter getIndexWriter(boolean create) throws IOException {
        if (indexWriter == null) {
            Directory indexDir = FSDirectory.open(new File("indexDir"));
            IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_0, new StandardAnalyzer());
            indexWriter = new IndexWriter(indexDir, config);
            indexWriter.deleteAll();
        }
        return indexWriter;
   }    
    
    public void closeIndexWriter() throws IOException {
        if (indexWriter != null) {
            indexWriter.close();
        }
   }
    public void indexSampleSearchableFieldsBean(SampleSearchableFieldsBean sampleFieldsBean) throws IOException {

        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();
        doc.add(new StringField("sampleId", sampleFieldsBean.getSampleId(), Field.Store.YES));
        doc.add(new StringField("sampleName", sampleFieldsBean.getSampleName(), Field.Store.YES));
        if(sampleFieldsBean.getSamplePocName()!=null)
        	doc.add(new StringField("samplePocName", sampleFieldsBean.getSamplePocName(), Field.Store.YES));
        if(sampleFieldsBean.getNanoEntityName()!=null)
        	doc.add(new StringField("nanoEntityName", sampleFieldsBean.getNanoEntityName(), Field.Store.YES));
        if(sampleFieldsBean.getNanoEntityDesc()!=null)
        	doc.add(new StringField("nanoEntityDesc", sampleFieldsBean.getNanoEntityDesc(), Field.Store.YES));
        if(sampleFieldsBean.getFuncEntityName()!=null)
        	doc.add(new StringField("funcEntityName", sampleFieldsBean.getFuncEntityName(), Field.Store.YES));
        if(sampleFieldsBean.getFunction()!=null)
        	doc.add(new StringField("function", sampleFieldsBean.getFunction(), Field.Store.YES));
        if(sampleFieldsBean.getCharacterization()!=null)
        	doc.add(new StringField("characterization", sampleFieldsBean.getCharacterization(), Field.Store.YES));
        if(sampleFieldsBean.getCreatedDate()!=null)
        	doc.add(new Field("createdDate", DateTools.dateToString(sampleFieldsBean.getCreatedDate(), DateTools.Resolution.MINUTE), Field.Store.YES, Field.Index.NOT_ANALYZED));
        if(sampleFieldsBean.getSampleKeywords().size()>0)
        	for(int i = 0; i < sampleFieldsBean.getSampleKeywords().size();i++){
        		doc.add(new StringField("sampleKeywords", sampleFieldsBean.getSampleKeywords().get(i), Field.Store.YES));
        	}
        String fullSearchableText = sampleFieldsBean.getSampleName() + " " + sampleFieldsBean.getSamplePocName() + " " + sampleFieldsBean.getNanoEntityName() + " " + sampleFieldsBean.getNanoEntityDesc() + " " + sampleFieldsBean.getFuncEntityName() + " " + sampleFieldsBean.getFunction() + " " + sampleFieldsBean.getCharacterization();
        String keywords = "";
        for( int i = 0; i < sampleFieldsBean.getSampleKeywords().size(); i++){
        	String keyword = sampleFieldsBean.getSampleKeywords().get(i);
        	keywords = keywords + " " + keyword;
        }
        fullSearchableText = fullSearchableText + " " + keywords;
        doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
        writer.addDocument(doc);
        
    }   
   
    public void indexProtocolSearchableFieldsBean(ProtocolSearchableFieldsBean protocolFieldsBean) throws IOException {

        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();
        doc.add(new StringField("protocolId", protocolFieldsBean.getProtocolId(), Field.Store.YES));
        doc.add(new StringField("protocolFileDesc", protocolFieldsBean.getProtocolFileDesc(), Field.Store.YES));
        doc.add(new StringField("protocolName", protocolFieldsBean.getProtocolName(), Field.Store.YES));
        doc.add(new StringField("protocolFileName", protocolFieldsBean.getProtocolFileName(), Field.Store.YES));
        if(protocolFieldsBean.getCreatedDate()!=null)
        	doc.add(new Field("createdDate", DateTools.dateToString(protocolFieldsBean.getCreatedDate(), DateTools.Resolution.MINUTE), Field.Store.YES, Field.Index.NOT_ANALYZED));
        String fullSearchableText = protocolFieldsBean.getProtocolName() + " " + protocolFieldsBean.getProtocolFileName();

        doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
        writer.addDocument(doc);
        
    } 
    
    public void indexPublicationSearchableFieldsBean(PublicationSearchableFieldsBean pubFieldsBean) throws IOException {

        IndexWriter writer = getIndexWriter(false);
        Document doc = new Document();
        doc.add(new StringField("publicationId", pubFieldsBean.getPublicationId(), Field.Store.YES));
        if(pubFieldsBean.getPubTitle()!=null)
        	doc.add(new StringField("pubTitle", pubFieldsBean.getPubTitle(), Field.Store.YES));
        if(pubFieldsBean.getPubmedId()!=null)
        	doc.add(new StringField("pubmedId", pubFieldsBean.getPubmedId(), Field.Store.YES));
        if(pubFieldsBean.getDoiId()!=null)
        	doc.add(new StringField("doiId", pubFieldsBean.getDoiId(), Field.Store.YES));
        if(pubFieldsBean.getPubDesc()!=null)
        	doc.add(new StringField("pubDesc", pubFieldsBean.getPubDesc(), Field.Store.YES));
        if(pubFieldsBean.getCreatedDate()!=null)
        	doc.add(new Field("createdDate", DateTools.dateToString(pubFieldsBean.getCreatedDate(), DateTools.Resolution.MINUTE), Field.Store.YES, Field.Index.NOT_ANALYZED));
        String keywords = "";
        String fullSearchableText = pubFieldsBean.getSampleName() + " " + pubFieldsBean.getPubDesc() + " " + pubFieldsBean.getPubTitle() + " " + pubFieldsBean.getPubmedId() + " " + pubFieldsBean.getDoiId();
        if(pubFieldsBean.getPubKeywords()!=null){
	        for(int i = 0; i < pubFieldsBean.getPubKeywords().size(); i++){
	        	String keyword = pubFieldsBean.getPubKeywords().get(i);
	        	keywords = keywords + " " + keyword;
	        }
    	}
        fullSearchableText = fullSearchableText + " " + keywords;
        String authors = "";
        if(pubFieldsBean.getAuthors()!=null){
	        for ( int j = 0; j < pubFieldsBean.getAuthors().size(); j++){
	        	String author = pubFieldsBean.getAuthors().get(j);
	        	authors = authors + " " + author;
	        }
        }
        fullSearchableText = fullSearchableText + " " + authors;
        String sampleNames = "";
        if(pubFieldsBean.getSampleName()!=null){
        	for(int k = 0; k < pubFieldsBean.getSampleName().size(); k++){
        		String sampleName = pubFieldsBean.getSampleName().get(k);
        		sampleNames = sampleNames + " " + sampleName;
        	}
        }
        fullSearchableText = fullSearchableText + " " + sampleNames;

        doc.add(new TextField("content", fullSearchableText, Field.Store.NO));
        writer.addDocument(doc);
        
    }   
    
    public void buildIndexes() throws IOException {
        //
        // Erase existing index
        //
        getIndexWriter(true);
        
        CustomSearchEngine searchEngine = new CustomSearchEngine();
        
        List<ProtocolSearchableFieldsBean> protocols = searchEngine.retrieveProtocolsForIndexing();
        for(ProtocolSearchableFieldsBean protocolBean : protocols){
        	indexProtocolSearchableFieldsBean(protocolBean);
        }
        List<PublicationSearchableFieldsBean> publications = searchEngine.retrievePublicationForIndexing();
        for(PublicationSearchableFieldsBean publicationBean : publications){
        	indexPublicationSearchableFieldsBean(publicationBean);
        }
        List<SampleSearchableFieldsBean> samples = searchEngine.retrieveSamplesForIndexing();
        for(SampleSearchableFieldsBean sampleBean : samples){
        	indexSampleSearchableFieldsBean(sampleBean);
        }
        
        //
        // Don't forget to close the index writer when done
        //
        closeIndexWriter();
   }    
}
