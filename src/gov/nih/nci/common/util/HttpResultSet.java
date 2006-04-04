package gov.nih.nci.common.util;
import java.util.*;

/*
 * Shaziya Muhsin 
 */

public class HttpResultSet {
    private List results = new ArrayList();
    private HashMap nextList = new HashMap();
    private HashMap preList = new HashMap();
    private HashMap firstList = new HashMap();
    
    private int start = 0;
    private int end = 0;
    private int next = 0;
    private int previous = 0;
    private int counter =0 ;
    private int rowCounter = 500;
    private String servletName = null;
    private String query = null;
    private String packageName = null;
    
    
    public HttpResultSet(List resultList, int rowCounter){
        this.rowCounter = rowCounter;        
        results = new ArrayList();
        for(int i=0; i< resultList.size(); i++){
            results.add(resultList.get(i));       
        }       
        counter = results.size();        
        setFirst();
    }
    
    
    public void setFirst(){        
        start = 0;        
        if(counter < rowCounter){
            end = counter -1;
        }
        else{
            end = rowCounter - 1;
            next = rowCounter;
            previous = 0;
        }
    }
        
    public HashMap getFirstSet() throws Exception{
        setFirst();
        for(int i=start; i<= end; i++){
            String key = String.valueOf(i +1);
            firstList.put(key,results.get(i));
        }      
        
        return firstList;
    }
    public HashMap getNext(){
        nextList = new HashMap();
        if(next < counter){
            start = next;
            end = next + rowCounter -1;
            if(end == (counter -1)){
                next = 0;                
            }
            else if(end < counter){
                next = end +1;
            }
            else{
                end = counter -1;
                next = 0;
            }
            if(((start >0) && (start - rowCounter)>=0)){
                previous = start - rowCounter;
            }
            else{
                previous = 0;
            }
           
        } for(int i=start; i<= end; i++){
            String key = String.valueOf(i +1);
            nextList.put(key,results.get(i));
        }
        
        return nextList;
    }
    
    public HashMap getPrevious(){
        
        preList = new HashMap();
        if((start - rowCounter)>= 0){
            previous = start - rowCounter;
        }
       
            start = previous;
            end = start + rowCounter - 1;
            next = end + 1;
       
        for(int i=start; i<= end; i++){
            String key = String.valueOf(i +1);
            preList.put(key,results.get(i));
        }
        
        return preList;
    }
    
    public HashMap getResults(){
        HashMap resultMap = new HashMap();
        for(int i=0; i< counter; i++){
            String key = String.valueOf(i +1);
            resultMap.put(key,results.get(i));
        }
        return resultMap;
    }
    public int getCounter(){
        return counter;
    }
    
    public void setServletName(String name){
        this.servletName = name;
    }
    public String getServletName(){
        return servletName;
    }
    public void setQuery(String q){
        this.query = q;
    }
    public String getQuery(){
        return query;
    }
    public void setPackageName(String name){
        packageName = name;
    }
    public String getPackageName(){
        return packageName;
    }
}
