*** UPDATE PASSWORD in caNanoLab UPT

Open caNanoLab UPT (https://cananolab-stage.nci.nih.gov/uptlogin)
Search for user 'canano_curator'
Update password: Quality@3


*** UPDATE CONFIG FILES in the JAR file with DB Authentication Information

1. EXTRACT TWO CONFIG FILES FROM THE JAR FILE
jar xvf endNoteParser.jar hibernate.cfg.xml
jar xvf endNoteParser.jar caNanoLab.csm.hibernate.cfg.xml

2. UPDATE THE FOLLOWING PROPERTIES TO THE APPROPRIATE DATABASE IN BOTH THE XML FILES

<property name="connection.url">jdbc:mysql://cbiodb580:3634/canano</property>
<property name="connection.username">nanouser</property>
<property name="connection.password">naNo#212</property>

3. UPDATE THE JAR FILE WITH THE UPDATED CONFIG FILES
jar uvf endNoteParser.jar hibernate.cfg.xml
jar uvf endNoteParser.jar caNanoLab.csm.hibernate.cfg.xml

Please make sure JAVA is in the class path and Run from the command Prompt: endNoteParser.bat

This script might take a couple of hours to load the data. Approximate number of records: 800