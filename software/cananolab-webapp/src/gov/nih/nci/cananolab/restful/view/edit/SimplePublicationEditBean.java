package gov.nih.nci.cananolab.restful.view.edit;

import java.util.Date;
import java.util.Map;

public class SimplePublicationEditBean {

	String sampleTitle;
	String title;
	String category;
	String status;
	String pubMedId;
	String digitalObjectId;
	String journalName;
	Date year;
	String volume;
	String startPage;
	String endPage;
	Map<String, String> authors;
	String authorId;
	String firstName;
	String lastName;
	String initial;
	String[] keywordsStr;
	String description;
	String[] researchAreas;
	String uri;
	String fileId;
	String sampleId;
	String[] associatedSampleNames;
	String[] groupAccesses;
	String[] userAccesses;
	String protectedData;
	Boolean isPublic;
	Boolean isOwner;
	String ownerName;
	String createdBy;
	
}
