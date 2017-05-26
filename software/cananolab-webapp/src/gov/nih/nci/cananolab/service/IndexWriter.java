package gov.nih.nci.cananolab.service;

import org.apache.log4j.Logger;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import gov.nih.nci.cananolab.restful.customsearch.CustomSearchEngine;
import gov.nih.nci.cananolab.restful.indexer.IndexBuilder;

public class IndexWriter extends QuartzJobBean
{

	private static Logger logger = Logger.getLogger(IndexWriter.class.getName());

	private CustomSearchEngine customSearchEngine;

	public void executeInternal(JobExecutionContext context) throws JobExecutionException
	{	
		try {
			// build a lucene index
			IndexBuilder indexBuilder = new IndexBuilder();
			indexBuilder.buildIndexes(customSearchEngine);
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	public void setCustomSearchEngine(CustomSearchEngine customSearchEngine) {
		this.customSearchEngine = customSearchEngine;
	}

}
