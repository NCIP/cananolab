package gov.nih.nci.cananolab.service;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import gov.nih.nci.cananolab.restful.indexer.IndexBuilder;

public class IndexWriter implements Job{

	private static Logger logger = Logger.getLogger(IndexWriter.class
			.getName());
	public void execute(JobExecutionContext context)
			throws JobExecutionException {	
			startIndexing();		
	}
	public void startIndexing(){
		try {
			// build a lucene index
		        IndexBuilder indexBuilder = new IndexBuilder();
		        indexBuilder.buildIndexes();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
