Please follow these steps to run the grid client that generates the output file for Thomson Reutuers:

1.  Install JDK 6 and set the JAVA_HOME environment variable to point to he JDK 6 installation directory
2.  Install Apache Ant 1.7.x and add Ant bin directory to your path
3.  Extract the cananolab-client-TR.zip to a directory, e.g. C:\
4.  Go to extracted directory (e.g. c:\cananolab\cananolab-client-TR) and edit the file "build.properties" and 
    overwrite the values for properties "output.directory" and "output.filename" with values of your choice.
    
    Note: You don't need to change the value for the property "cananolab.grid.service.url" unless you want 
          to query against a different caNanoLab grid service
     
5.  At the command prompt, execute "ant" to start running the client

The client shall take about 3 hours to complete for all caNanoLab samples.