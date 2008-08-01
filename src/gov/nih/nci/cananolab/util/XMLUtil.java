/**
 * Utilility class to handle populate form fields by parsing XML file
 * It includes pubmedID population 
 * modify caMod.PopulatePubMedUtil
 * @author tanq
 */
package gov.nih.nci.cananolab.util;
import gov.nih.nci.cananolab.domain.common.Publication;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * this class is to retrieve record from pubMed if a correct pubmedid is provided.
 * populate the record into the database after retrieval
 */

public class XMLUtil implements Runnable{
	private static final String NOT_FOUND = "<ERROR>Empty id list - nothing todo</ERROR>";
	private static final String PUBMED_URL = "http://www.ncbi.nlm.nih.gov/entrez/utils/pmfetch.fcgi?db=PubMed&report=abstract&mode=xml&id=";
	private static final String LINE_BR = "";
    private static XMLUtil onlyInstance = null;
    private static Thread  insertRecord = null;
    private static Long pubMed_ID = null;
    private static Publication pub = null;
    public static synchronized XMLUtil getInstance() {
        if (onlyInstance == null)
            onlyInstance = new XMLUtil();
        
        return onlyInstance;
    }
    public XMLUtil(){}
    
    public static void populatePumMedRecord(Long pubMedID, Publication publication){
        // Instantiate the file transfer thread and run it.
        insertRecord = new Thread( new XMLUtil() ); // run a separate thread
        pubMed_ID = pubMedID;
        //System.out.println( "<PopulatePubMed.java run> PubMed_ID=" + pubMed_ID + ", " + pubMedID );
        pub = publication;
        insertRecord.run();
    }
    public void run(){
        //System.out.println("<PopulatePubMed.java run> Entering..." );
        String pubMedAbstractRecord = getPubmedAbstract(pubMed_ID);
        if (pubMedAbstractRecord!=null &&
        		pubMedAbstractRecord.indexOf(NOT_FOUND)==-1) {        	
        	
	        System.out.println("=============================" );
	        System.out.println(pubMedAbstractRecord);
	        System.out.println("=============================" );
//        String pubMedAbstract = getAbstractString(pubMedAbstractRecord);
//        String[] pageStringArray = getPubMedPages(pubMedAbstract);
//        String  pageStart = null;
//        String pageEnd = null;
//        if(pageStringArray.length>1&& pageStringArray[0] !=null && pageStringArray[1] !=null){
//            pageStart = pageStringArray[0];
//            pageEnd = pageStringArray[1];
//        }
//        
//        String pubTitle = getPublicationTitle(pubMedAbstract);       
//        String pubVolume = getPublicationVolume(pubMedAbstract);      
//        String pubYear = getPublicationYear(pubMedAbstract);        
//        String journal = getJournal(pubMedAbstract);
        
        //System.out.println("<PopulatePubMed.java> getPublicationTitle="+pubTitle);
        //System.out.println("<PopulatePubMed.java> pubVolume="+pubVolume);
        //System.out.println("<PopulatePubMed.java> pubYear="+pubYear);
        //System.out.println("<PopulatePubMed.java> journal="+journal);
        
//        try{
//            pub.setTitle(pubTitle);
//            pub.setJournalName(journal);
//            
//            if(pageEnd != null && pageEnd.indexOf(".")!=-1){
//                //System.out.println("<PopulatePubMed.java> 44444");
//                pageEnd = pageEnd.substring(0,pageEnd.indexOf("."));
//            }
//            
//            if(pageStart != null && pageEnd != null){
//                //System.out.println("<PopulatePubMed.java> pageStart="+pageStart);
//                //System.out.println("<PopulatePubMed.java> pageEnd="+pageEnd);
//                StringBuffer pageNumber = new StringBuffer();
//                char[] pageStartChar = pageStart.toCharArray();
//                for(int i=0; i<pageStartChar.length;i++){
//                    //System.out.println("<PopulatePubMed.java> pageStartChar:"+pageStartChar[i]);
//                    char pageChar = pageStartChar[i];
//                    
//                    //Character pageChar = new Character(pageStartChar[i]);
//                    //System.out.println("<PopulatePubMed.java> pageChar is :"+pageChar);
//                    if(Character.isDigit(pageChar)){
//                        pageNumber.append(pageChar);
//                    }
//                    
//                }
//                pub.setStartPage(new Long(pageNumber.toString()));
//                if(pageEnd.length() >= pageStart.length()){
//                    StringBuffer endPageNumbers = new StringBuffer();
//                    char[] pageEndChars = pageEnd.toCharArray();
//                    for(int p=0; p<pageEndChars.length;p++){
//                        //System.out.println("<PopulatePubMed.java> pageEndChars:"+pageEndChars[p]);
//                        char endPageChars = pageEndChars[p];
//                        //System.out.println("<PopulatePubMed.java> endPageChars is :"+endPageChars);
//                        if(Character.isDigit(endPageChars)){
//                            endPageNumbers.append(endPageChars);
//                        }
//                        
//                    }
//                    pub.setEndPage(new Long(endPageNumbers.toString()));
//                    
//                } else {
//                    int diff = pageStart.length() - pageEnd.length();
//                    pageEnd = pageStart.substring(0,diff)+pageEnd;
//                    StringBuffer endPageNumber = new StringBuffer();
//                    char[] pageEndChar = pageEnd.toCharArray();
//                    for(int k=0; k<pageEndChar.length;k++){
//                        //System.out.println("<PopulatePubMed.java> pageEndChar:"+pageEndChar[k]);
//                        char endPageChar = pageEndChar[k];
//                        
//                        
//                        //System.out.println("<PopulatePubMed.java> endPageChar is :"+endPageChar);
//                        if(Character.isDigit(endPageChar)){
//                            endPageNumber.append(endPageChar);
//                        }                        
//                    }
//                    
//                    pub.setEndPage(new Long(endPageNumber.toString()));
//
//                }
//            } else{
//                pub.setStartPage(null);
//                pub.setEndPage(null);
//            }
//            if(pubVolume  != null){
//                
//                if(pubVolume.indexOf("(")!=-1){
//                    
//                    pubVolume = pubVolume.substring(1,pubVolume.indexOf("("));
//                    pub.setVolume(pubVolume);
//                } else{
//                    
//                    pub.setVolume(pubVolume);
//                }
//            } else {
//                pub.setVolume(null);
//            }
//            
//            pub.setYear( Integer.valueOf( pubYear.trim() ) );
//          //  pub.updateByKey();
//        } catch(Exception e){
//            System.out.println("<PopulatePubMed.java> Error in PopulatePubMed"+e);
//        }
        }
    }
    
    public static String getPubmedAbstract(Long pubMedID){	
    	// to retrieve record from pub med if pubmedID exits    	
    	System.out.println("<PopulatePubMed.java getPubmedAbstract> pubMedID=" + pubMedID );
        StringBuffer buf = new StringBuffer();
        String line = null;
        InputStream inputStream = null;       
      
        if(  pubMedID != null ){
            try{
                URL url = new URL(PUBMED_URL+pubMedID);
                inputStream = url.openStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                line = reader.readLine();
                buf.append(line);
                buf.append(LINE_BR);
                while(line != null){
                    line = reader.readLine();
                    if(line != null){
                    	 buf.append(line);
                         buf.append(LINE_BR);
                    }//end while                 
                }// end try
                reader.close();
                inputStream.close();
            } catch (IOException exception) {
                System.out.println(exception);
            }                        
        }// end if           
        return buf.toString();
    }
    
    public static boolean 	get_AbstractString(String pubmedRecord){
        System.out.println("<PopulatePubMed.java get_AbstractString> Entering...");
        String pubmedAbstract = null;
        
        boolean flag = false;
        
        try {
        if(pubmedRecord != null){
            if(pubmedRecord.indexOf("1: ")!=-1){
                int i = pubmedRecord.indexOf("1: ");
                pubmedAbstract = pubmedRecord.trim().substring(i+2);                
                StringTokenizer token = new StringTokenizer(pubmedAbstract);
                while(token.hasMoreTokens()){
                    String nextToken = token.nextToken();
                    if(nextToken.equals("Error")){
                        flag = true;
                        break;
                    }
                }
            }
        }
        } catch (Exception e ) { System.out.println( "Exception e=" + e);}
        return flag;
    }
    
    private static  String getAbstractString(String pubmedRecord){// retrieve the pubmed abstract
        String pubmedAbstract = null;
        try {
        if(pubmedRecord != null){
            if( pubmedRecord.indexOf("1: ")!=-1){
                int i = pubmedRecord.indexOf("1: ");
                pubmedAbstract = pubmedRecord.trim().substring(i+2);
                System.out.println( "<PopulatePubMed.java getAbstractString> \n<PopulatePubMed.java getAbstractString> **** pubmedAbstract *****\n" + pubmedAbstract + "\n<PopulatePubMed.java getAbstractString>  **** pubmedAbstract *****\n");
            }
        }
        } catch (Exception e ) { System.out.println( "Exception e=" + e);}
        return pubmedAbstract;
    }
    
    private static String[] getPubMedPages(String pubMedAbstract){ // to get start colume and end colume for pub record
        System.out.println("<PopulatePubMed.java getPubMedPages> Entering..." );
        String[] pageArray = new String[2];
        
        try {
        if(pubMedAbstract != null){
            if(pubMedAbstract.indexOf(":") !=-1 && pubMedAbstract.indexOf("[epub ahead of print].")==-1){
                
                int startPage = pubMedAbstract.indexOf(":");
                String pageString = pubMedAbstract.trim().substring(startPage).trim();
                StringTokenizer token = new StringTokenizer(pageString);
                String firstToken = null;
                while (token.hasMoreTokens()) {
                    firstToken = token.nextToken();
                    
                    break;
                }
                if(firstToken.indexOf(";")!=-1){
                    firstToken = firstToken.substring(0,firstToken.indexOf(";"));
                }
                
                if(firstToken.indexOf("-") !=-1){
                    int pageSeparator = firstToken.indexOf("-");
                    int pageLength = firstToken.length();
                    pageArray[0] = pageString.substring(0,pageSeparator);
                    pageArray[1] = pageString.substring((pageSeparator+1),pageLength);
                    
                } else{
                    if(firstToken.indexOf(".")!=-1){
                        pageArray[0] = firstToken.substring(0,firstToken.indexOf("."));
                        pageArray[1] = firstToken.substring(0,firstToken.indexOf("."));
                    } else{
                        pageArray[0] = firstToken;
                        pageArray[1] = firstToken;
                    }
                }                                
            }           
        }
        } catch (Exception e ) { System.out.println( "Exception e=" + e);}
        return pageArray;
    }
    private static  String getPublicationTitle(String pubmedAbstract){// to get the title of the record
       
    	System.out.println("<PopulatePubMed.java getPublicationTitle> Entering..." );
        System.out.println("pubmedAbstract=" + pubmedAbstract );
        String pubMedAbstract = pubmedAbstract;
        
        String endPage = null;
        String startPage = null;
        String[] pageArray = null;
        String titleCol = null;
        
        try {
        	
        if(pubmedAbstract != null){
            //System.out.println("<PopulatePubMed.java> %%%%%%%%%%%%%%%pubmedAbstract:"+pubmedAbstract);
            if(pubmedAbstract.indexOf("Comment in:")==-1){
                pageArray = getPubMedPages(pubmedAbstract);
                if(pageArray != null && pageArray[0] != null &&pageArray[1] != null ){
                    startPage = pageArray[0];
                    endPage = pageArray[1];
                    if(pubmedAbstract.indexOf("Epub")!=-1){
                        pubmedAbstract = pubmedAbstract.substring(pubmedAbstract.indexOf("Epub"));
                        pubmedAbstract = pubmedAbstract.substring(pubmedAbstract.indexOf(".")+1);
                        titleCol = pubmedAbstract.substring(0,pubmedAbstract.indexOf(".")).trim();
                    } else if(pubmedAbstract.indexOf("Response to Comment on ")!=-1){
                        String pubmedAbstract2 = "Response to Comment on ";
                        if(pubmedAbstract.indexOf("&quot;") !=-1){
                            String pubmedAbstract3 = pubmedAbstract.substring(pubmedAbstract.indexOf("&quot;")+6);
                            if(pubmedAbstract3.indexOf("&quot;")!=-1){
                                int pubmedAbstract3Index = pubmedAbstract3.indexOf("&quot;");
                                pubmedAbstract3 =  pubmedAbstract3.substring(0,pubmedAbstract3Index);
                                //System.out.println("<PopulatePubMed.java> &########pubmedAbstract3:"+pubmedAbstract3);
                                titleCol = pubmedAbstract2 +": " +pubmedAbstract3;
                                //System.out.println("<PopulatePubMed.java> titleCol is (((((:"+titleCol);
                            }
                        }
                        
                    } else{
                        int pageIndex = pubmedAbstract.indexOf(startPage+"-"+endPage);
                        int pageIndexlength  = startPage.length() + endPage.length() + 1;
                        titleCol  = pubmedAbstract.substring(pageIndex + pageIndexlength);
                        titleCol = titleCol.substring(0,titleCol.indexOf(".")).trim();
                    }
                } else{
                    pubmedAbstract = pubmedAbstract.substring(pubmedAbstract.indexOf(".")+1);
                    
                    if(pubmedAbstract.indexOf("[Epub ahead of print].")!=-1){
                        int bracetIndex = pubmedAbstract.indexOf("[Epub ahead of print].");
                        int ePubLength = "[Epub ahead of print].".length();
                        pubmedAbstract = pubmedAbstract.substring(bracetIndex+ePubLength+1);
                        titleCol = pubmedAbstract.substring(0,pubmedAbstract.indexOf(".")).trim();
                    }
                }
                if(titleCol.indexOf("; discussion ")!=-1){
                    titleCol = titleCol.substring(titleCol.indexOf("-"),titleCol.length()).trim();
                    titleCol = titleCol.substring(titleCol.indexOf(" "), titleCol.length()).trim();
                } 
                
            } else {
                String  titleCol1 = pubmedAbstract.substring((pubmedAbstract.indexOf(".")+1));
                int titleCol2Int = titleCol1.indexOf(".");
                String titleCol2 = titleCol1.substring(titleCol2Int+1).trim();
                titleCol= titleCol2.substring(0,titleCol2.indexOf("."));
            }
        }
        } catch (Exception e) { 
        	System.out.println( "Exception=" + e );
        }
        
        titleCol = titleCol.replaceAll("\n", "");       
        System.out.println("<PopulatePubMed.java getPublicationTitle> *title=" + titleCol );
       
        //Re-Written title retrieval
        //Gforge Bug #12064 - schroedln 4/15/08
        pubMedAbstract = pubMedAbstract.replaceAll("\n", "NEWLINE" );
					    
	    String patternStr = "NEWLINENEWLINE";
	    String[] fields = pubMedAbstract.split(patternStr);
	    	    
	    System.out.println( "Title: " + fields[1].replaceAll("NEWLINE", " " ) );
        
	    titleCol = fields[1].replaceAll("NEWLINE", " " );	    
	    //end of re-written title retrieval
	    
        return titleCol;
    }

    // Returns a version of the input where all line terminators
    // are replaced with a space.
    public static String removeLineTerminators(String inputStr) {
        String patternStr = "(?m)$^|[\\r\\n]+\\z";
        String replaceStr = " ";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(inputStr);
        return matcher.replaceAll(replaceStr);
    }

    private String getPublicationVolume(String pubmedAbstract){// volume of the publication
        System.out.println("<PopulatePubMed.java getPublicationVolume> Entering..." );
        String volumeString = null;
        
        try {
        if(pubmedAbstract != null ){
            int j = pubmedAbstract.indexOf(";");
            if(pubmedAbstract.indexOf(":") !=-1 && pubmedAbstract.indexOf("[epub ahead of print]")==-1){
                int k = pubmedAbstract.indexOf(":");
                volumeString = pubmedAbstract.substring(j,k);
            }
        }
        } catch (Exception e ) { System.out.println( "Exception e=" + e);}
        return volumeString;
    }
    
    private static String getPublicationYear(String pubmedAbstract){ // published year
        System.out.println("<PopulatePubMed.java getPublicationYear> Entering..." );
        String yearString  = null;
        String year = null;
        int j=0;
        int k=0;
        try {
        if(pubmedAbstract != null){            
            if(pubmedAbstract.indexOf("[epub ahead of print]")==-1){                
                j = pubmedAbstract.indexOf(".");
                k = pubmedAbstract.indexOf(";");
                yearString = pubmedAbstract.substring(j+1, k).trim();
                System.out.println("<PopulatePubMed.java>  yearString in section1:"+yearString);
            } else{                
                j = pubmedAbstract.indexOf(";");
                yearString = pubmedAbstract.substring(0, j);                
            }                       
            StringTokenizer token = new StringTokenizer(yearString);            
            while(token.hasMoreTokens()) {
                boolean statusYear = false;
                year = token.nextToken();
                byte[] bytes = year.getBytes();
                int t = 0;
                if(bytes.length == 4){
                    ByteArrayInputStream byteInput = new ByteArrayInputStream(bytes);
                    int readByte = byteInput.read();
                    while(readByte <= 57 && readByte >= 48){
                        readByte = byteInput.read();
                        t++;
                        statusYear = true;
                    }
                }
                if( statusYear){                    
                    return year;
                }
            }
        }
        } catch (Exception e ) { System.out.println( "Exception e=" + e);}
        return null;
    }
    private static String getJournal(String pubmedAbstract){// in what journal that was published
        System.out.println("<PopulatePubMed.java getJournal> Entering..." );
        String journal = null;
        String year = null;
        String yearString = null;
        int j= 0;
        
        try {
        if(pubmedAbstract != null){
            if(pubmedAbstract.indexOf("[epub ahead of print]")==-1){
                year = getPublicationYear(pubmedAbstract);                
                if(year!=null) {
                    
                    j= pubmedAbstract.indexOf(year);
                    yearString = pubmedAbstract.substring(0, j+6).trim();
                }//end of if
            } else{
                j = pubmedAbstract.indexOf(";");
                yearString = pubmedAbstract.substring(0, j);
            }
            
            year = getPublicationYear(pubmedAbstract);
            if(yearString!=null) {
                int k = yearString.indexOf(year);
                journal = yearString.substring(0,(k-2));
            }//end of if
            
        }
        } catch (Exception e ) { System.out.println( "Exception e=" + e);}
        return journal;
    }
    
    
	public static void main(String[] args) {
		System.out.println("debug");
		Publication pub = new Publication();
		//XMLUtil.populatePumMedRecord(Long.valueOf("17784955"), pub);
		XMLUtil.populatePumMedRecord(Long.valueOf("18294836"), pub);
	}
}
