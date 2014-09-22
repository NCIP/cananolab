package gov.nih.nci.cananolab.restful.util;

import java.util.List;

import org.apache.log4j.Logger;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.codehaus.jackson.map.annotate.JsonFilter;
import org.codehaus.jackson.map.ser.FilterProvider;
import org.codehaus.jackson.map.ser.impl.SimpleBeanPropertyFilter;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;


public class ViewFilterUtil {
	private static Logger logger = Logger.getLogger(ViewFilterUtil.class);
	
	/**
	 * Get an inclusive jackson object writer for writing a set of named fields of an object
	 * 
	 * @param includeFieldNames fields that should be serialized	
	 * @param filterName filter name. This needs to match the @JsonFilter("filterName") annotation for a bean
	 * 
	 * @return a Jackson ObjectWriter
	 * @throws Exception
	 */
	public static ObjectWriter getInclusiveObjectWriter(String[] includeFieldNames, String filterName) 
	throws Exception {
		
		if (includeFieldNames == null)
			throw new Exception("includeFieldNames can't be null");
		
		if (filterName == null)
			throw new Exception("filterName can't be null");
		
		ObjectMapper mapper = new ObjectMapper();  
	    FilterProvider filters = new SimpleFilterProvider()  
	      .addFilter(filterName,   
	          SimpleBeanPropertyFilter.filterOutAllExcept(includeFieldNames));  
	    return mapper.writer(filters);  
		
	}
	
	/**
	 * Get an exclusive jackson object writer for serializing an object without a set of named fields
	 * 
	 * @param excludeFieldNames fields that should be excluded for serialization
	 * @param filterName filter name. This needs to match the @JsonFilter("filterName") annotation for a bean
	 * 
	 * @return a Jackson ObjectWriter
	 * @throws Exception
	 */
	public static ObjectWriter getExclusivObjectWriter(String[] excludeFieldNames, String filterName) 
			throws Exception {
		if (excludeFieldNames == null)
			throw new Exception("includeFieldNames can't be null");
		
		if (filterName == null)
			throw new Exception("filterName can't be null");
		
		ObjectMapper mapper = new ObjectMapper();  
	    FilterProvider filters = new SimpleFilterProvider()  
	      .addFilter(filterName,   
	          SimpleBeanPropertyFilter.serializeAllExcept(excludeFieldNames));  
	    return mapper.writer(filters);  
		
	}
	
	/**
	 * Get the json string for sample search result. This includes only a subset of the fields in 
	 * SimpleSampleBean
	 * 
	 * @param results search result list. This could contain only error messages.
	 * 
	 * @return
	 */
	public static String getSampleSearchResultJSon(List results) {
		String[] needFieldNames = {"sampleId", "sampleName", "pointOfContact", "composition", "functions", "characterizations", 
				"dataAvailability", "createdDate"};  
		try {
		    ObjectWriter writer = ViewFilterUtil.getInclusiveObjectWriter(needFieldNames, "searchResultFilter");
		    return writer.writeValueAsString(results);
		} catch (Exception e) {
			logger.error("getSampleSearchResultJSon error: " + e.getMessage());
		}
		
		return "Error getting sample search result in json";
	}

}
