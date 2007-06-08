/*
The caArray Software License, Version 1.2

Copyright 2004 SAIC. This software was developed in conjunction with the National 
Cancer Institute, and so to the extent government employees are co-authors, any 
rights in such works shall be subject to Title 17 of the United States Code, 
section 105.

Redistribution and use in source and binary forms, with or without modification, 
are permitted provided that the following conditions are met:

1. Redistributions of source code must retain the above copyright notice, this 
list of conditions and the disclaimer of Article 3, below. Redistributions in 
binary form must reproduce the above copyright notice, this list of conditions 
and the following disclaimer in the documentation and/or other materials 
provided with the distribution.

2. Affymetrix Pure Java run time library needs to be downloaded from  
(http://www.affymetrix.com/support/developer/runtime_libraries/index.affx) 
after agreeing to the licensing terms from the Affymetrix. 

3. The end-user documentation included with the redistribution, if any, must 
include the following acknowledgment:

"This product includes software developed by the Science Applications International 
Corporation (SAIC) and the National Cancer Institute (NCI).”

If no such end-user documentation is to be included, this acknowledgment shall 
appear in the software itself, wherever such third-party acknowledgments 
normally appear.

4. The names "The National Cancer Institute", "NCI", 
“Science Applications International Corporation”, and "SAIC" must not be used to 
endorse or promote products derived from this software.

5. This license does not authorize the incorporation of this software into any 
proprietary programs. This license does not authorize the recipient to use any 
trademarks owned by either NCI or SAIC.

6. THIS SOFTWARE IS PROVIDED "AS IS," AND ANY EXPRESSED OR IMPLIED WARRANTIES, 
(INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND 
FITNESS FOR A PARTICULAR PURPOSE) ARE DISCLAIMED. IN NO EVENT SHALL THE NATIONAL 
CANCER INSTITUTE, SAIC, OR THEIR AFFILIATES BE LIABLE FOR ANY DIRECT, INDIRECT, 
INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; 
 OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, 
 WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY
  OF SUCH DAMAGE.
*/

package gov.nih.nci.calab.service.workflow.httpfileuploadapplet;



import java.applet.AppletContext;
import java.awt.BorderLayout;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.io.File;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.BoxLayout;
import javax.swing.JApplet;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;

public class HttpFileUploadApplet extends JApplet 
    implements ActionListener, ItemListener
{
    //JPanel contentPane;
    String[] pattern = { " ", "/", "\\", "|", ";", ",", "!", "@", 
            "(", ")", "<", ">",   "\"", "#", "$", 
            "'", "~", "{",  "}", "[", "]", "=",
            "+",  "&", "^",   "\t"};
    
    private final String INSTRUCTION = "Upload Instruction";
    private final String SUMMARY = "Upload Summary";
    private final String SUCCESS = "Files Uploaded";
    private final String FAILURE = "Files Failed";
    private final String TIMEBYTES = "Total Upload Time and Total Bytes Sent";
    
    List chooseLst = new List();

    List selectedLst = new List();

    JButton dataBt = new JButton();

    JButton otherBt = new JButton();

    JButton removeBt = new JButton();

    JButton removeAllBt = new JButton();

    JButton uploadBt = new JButton();
    JButton stopBt = new JButton();
    JButton helpBt = new JButton();
    JButton doneBt = new JButton();

    JButton selectAllBt = new JButton();

    JButton deSelectAllBt = new JButton();

    JProgressBar progressBar = new JProgressBar(0, 100);

    JLabel driveLabel = new JLabel();

    JLabel selectedFiles = new JLabel();

    //JLabel uploadFileName = new JLabel("File Name");
    JLabel uploadFileName = new JLabel("");
    
    
    private File currentDir; // The directory currently listed

    private String[] files; // The directory contents

    private DateFormat dateFormatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
                                                                      DateFormat.SHORT);

    JLabel cDirLabel = new JLabel();

    JLabel directoryDisplayLabel = new JLabel();

    Choice driveChoice = new Choice();

    private Vector selectedFileVec = new Vector();

    private HttpUploadFile uFile;

    //To be implemented:
    private HttpUploadParameters up = null;
    HttpFileUploadThread thread = null;
    private Timer timer;
    //private ArrayList fileList = new ArrayList();
    
    ResultFrame resultWindow = null;
    HelpFrame helpWindow = null;

    String[] extensions = null;
    String module = null;
    
    boolean isSingleFile = false;

        
    /** Component initialization */
    public void init()
    {	
        // get startup parameters
        up = new HttpUploadParameters();
        up.setUploadURL(this.getParameter("uploadURL"));
        up.setNotifyURL(this.getParameter("notifyURL"));
        up.setDefaultURL(this.getParameter("defaultURL"));
        up.setHttpTunnelingURL(this.getParameter("tunnelURL"));
        up.setId(this.getParameter("id"));
        up.setSid(this.getParameter("sid"));
        up.setModule(this.getParameter("module"));
        up.setPermissibleFileExtention(this.getParameter("permissibleFileExtension"));
        if (up.getPermissibleFileExtension()== null 
        		|| up.getPermissibleFileExtension().length() == 0)
        {
        	extensions = new String[0];
        }
        else
        {
            extensions = up.getPermissibleFileExtension().split("_");
        }
        module = up.getModule();
        
        if (module.equalsIgnoreCase("arraydesign"))
        {
        	isSingleFile = true;
        }
        //fileList = getHybridizationFileList(up.getHttpTunnelingURL(), up.getId(), module);
        
        //For array design, only one file is allowed, so 
        //we do not check for duplicate file name


        //Test code
        /*
        up.setUploadURL("http://localhost:8080/caarray/processFileUpload");
        up.setNotifyURL("http://localhost:8080/caarray/hybridizationFileProcessAction.do?method=processFile");
        up.setId("1015897577532015");
        up.setSid("9FFC40669F978DFD6816168EE76050AB");
        up.setModule("hybridization");
        up.setPermissibleFileExtension("TXT");
        extensions = up.getPermissibleFileExtension().split("_");
        fileList.add("design_22.txt");
        fileList.add("desgin_21.txt");
        */
        statusLayout();
        validate();       
    }
    
    private ArrayList getHybridizationFileList(String tunnelURL, String id, String module) {
		ArrayList fileList = new ArrayList();
		
		String command = module + "|" + id;
		
		HttpTunnelingFetchFileList htff = new HttpTunnelingFetchFileList();
		try
		{
			fileList = htff.FetchFileList(tunnelURL, command);
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(this,"Error: \n" + e.toString(), "Error message", JOptionPane.INFORMATION_MESSAGE);
		}
    	
    	return fileList;
	}

	/**
     * method to create the UI for the status monitor)
     *
     */
    private void statusLayout()
    {
        Container contentPane = getContentPane();

        //this.setSize(new Dimension(779, 641));
        
        chooseLst.setFont(new Font("MonoSpaced", Font.PLAIN, 14));
        chooseLst.addActionListener(new UploadFrame_chooseLst_actionAdapter(this));
        chooseLst.addActionListener(this);
        chooseLst.addItemListener(this);
        chooseLst.setMultipleMode(true);
        selectedLst.setFont(new Font("MonoSpaced", Font.PLAIN, 14));
        selectedLst.addActionListener(this);
        selectedLst.addItemListener(this);
        selectedLst.setMultipleMode(true);
        dataBt.setText("Data >>");
        dataBt.addActionListener(new UploadFrame_dataBt_actionAdapter(this));
        
        if ("hybridization".equals(up.getModule()))
        {
            otherBt.setText("Other >>");
            otherBt.addActionListener(new UploadFrame_otherBt_actionAdapter(this));
        }
        removeBt.setText("Remove");
        removeBt.addActionListener(new UploadFrame_removeBt_actionAdapter(this));
        removeAllBt.setText("Remove All");
        removeAllBt.addActionListener(new UploadFrame_removeAllBt_actionAdapter(this));
        uploadBt.setText("Upload Files");
        uploadBt.addActionListener(new UploadFrame_uploadBt_actionAdapter(this));
        
        
        stopBt.setText("Stop");
        stopBt.addActionListener(new UploadFrame_stopBt_actionAdapter(this));
        stopBt.setEnabled(false);

        doneBt.setText("Done");
        doneBt.addActionListener(new UploadFrame_doneBt_actionAdapter(this));
        
        helpBt.setText("Help");
        helpBt.addActionListener(new UploadFrame_helpBt_actionAdapter(this));        
        
        driveLabel.setFont(new java.awt.Font("Dialog", 1, 12));
        driveLabel.setText("Drive:");
        selectedFiles.setFont(new java.awt.Font("Dialog", 1, 12));
        selectedFiles.setText("Selected Files");
        selectAllBt.setText("Select All");
        selectAllBt.addActionListener(new UploadFrame_selectAllBt_actionAdapter(this));
        deSelectAllBt.setText("Clear All");
        deSelectAllBt.addActionListener(new UploadFrame_deSelectAllBt_actionAdapter(this));
        cDirLabel.setFont(new java.awt.Font("Dialog", 1, 12));
        cDirLabel.setText("Current Directory: ");
        directoryDisplayLabel.setText(" ");
        driveChoice.addItemListener(new UploadFrame_DriveChoice_itemAdapter(this));

        contentPane.setLayout(new BorderLayout());

        //create directory display area
        JPanel displayDirectoryPanel = new JPanel();
        displayDirectoryPanel.setLayout(new BorderLayout());

        JPanel labelPane = new JPanel();
        labelPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        labelPane.add(cDirLabel);
        displayDirectoryPanel.add(labelPane, BorderLayout.WEST);

        JPanel displayLabelPane = new JPanel();
        displayLabelPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        displayLabelPane.add(directoryDisplayLabel);
        displayDirectoryPanel.add(displayLabelPane, BorderLayout.CENTER);

        contentPane.add(displayDirectoryPanel, BorderLayout.NORTH);

        //set up center panel --- list directory
        JPanel centerPane = new JPanel();
        centerPane.setLayout(new GridLayout(1, 3));

        JPanel leftPane = new JPanel();
        leftPane.setLayout(new BorderLayout());

        JPanel leftNorthPane = new JPanel();
        leftNorthPane.setLayout(new BorderLayout());

        JPanel driveLabelPane = new JPanel();
        driveLabelPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        driveLabelPane.add(driveLabel);
        leftNorthPane.add(driveLabelPane, BorderLayout.WEST);

        JPanel driveDropDownPane = new JPanel();
        driveDropDownPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        driveDropDownPane.add(driveChoice);
        leftNorthPane.add(driveDropDownPane, BorderLayout.CENTER);

        leftPane.add(leftNorthPane, BorderLayout.NORTH);

        JPanel leftCenterPane = new JPanel();
        leftCenterPane.setSize(200, 400);
        leftCenterPane.setLayout(new GridLayout(1, 1));
        leftCenterPane.add(chooseLst);

        leftPane.add(leftCenterPane, BorderLayout.CENTER);

        JPanel leftSouthPane = new JPanel();
        leftSouthPane.setLayout(new GridLayout(1, 2));

        JPanel selectAllPane = new JPanel();
        selectAllPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        selectAllPane.add(selectAllBt);
        leftSouthPane.add(selectAllPane);

        JPanel deSelectAllPane = new JPanel();
        deSelectAllPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        deSelectAllPane.add(deSelectAllBt);
        leftSouthPane.add(deSelectAllPane);

        leftPane.add(leftSouthPane, BorderLayout.SOUTH);

        centerPane.add(leftPane);

        //set centerCenter panel
        JPanel centerC = new JPanel();
        centerC.setLayout(new GridLayout(3, 1));
        centerC.add(new Label("   "));

        JPanel thirdPane = new JPanel();
        thirdPane.setLayout(new GridLayout(2, 1));
        JPanel upperPane = new JPanel();
        upperPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        JPanel downPane = new JPanel();
        downPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        downPane.add(dataBt);
        thirdPane.add(downPane);

        centerC.add(thirdPane);
        thirdPane.add(upperPane);
        if ("hybridization".equals(up.getModule()))
        {
            upperPane.add(otherBt);
        }
        centerC.add(new Label("    "));
        centerPane.add(centerC);
        //set up center right pane (selected files list)
        JPanel rightPane = new JPanel();
        rightPane.setLayout(new BorderLayout());
        rightPane.setSize(200, 400);
        rightPane.setBackground(Color.red);
        JPanel selectedLabelPane = new JPanel();
        selectedLabelPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        selectedLabelPane.add(selectedFiles);
        rightPane.add(selectedLabelPane, BorderLayout.NORTH);

        //setup center right pane
        JPanel rightCenterPane = new JPanel();
        rightCenterPane.setLayout(new GridLayout());
        rightCenterPane.add(selectedLst);
        rightPane.add(rightCenterPane, BorderLayout.CENTER);

        JPanel rightSouthPane = new JPanel();
        rightSouthPane.setLayout(new GridLayout(1, 2));

        JPanel firstGridPane = new JPanel();
        firstGridPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        firstGridPane.add(removeBt);
        rightSouthPane.add(firstGridPane);

        JPanel secondGridPane = new JPanel();
        secondGridPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        secondGridPane.add(removeAllBt);
        rightSouthPane.add(secondGridPane);

        rightPane.add(rightSouthPane, BorderLayout.SOUTH);

        centerPane.add(rightPane);

        //set up SOUTH pane
        JPanel southPane = new JPanel();
        southPane.setLayout(new GridLayout(2, 1));

        JPanel firstPane = new JPanel();
        firstPane.setLayout(new FlowLayout(FlowLayout.CENTER));
        firstPane.add(uploadBt);
        firstPane.add(stopBt);
        firstPane.add(doneBt);
        firstPane.add(helpBt);
               
        southPane.add(firstPane);

        JPanel secondPane = new JPanel();
        secondPane.setLayout(new GridLayout(2, 1));
        
        JPanel lastPane = new JPanel();
        lastPane.setLayout(new FlowLayout(FlowLayout.LEFT));
        lastPane.add(uploadFileName);
        JPanel uploadFileNamePane = new JPanel();
        uploadFileNamePane.setLayout(new BorderLayout());
        uploadFileNamePane.add(new Label("  "), BorderLayout.WEST);
        uploadFileNamePane.add(new Label("  "), BorderLayout.EAST);
        uploadFileNamePane.add(lastPane);
        secondPane.add(uploadFileNamePane);
        
        JPanel progressbarPanel = new JPanel();
        progressbarPanel.setLayout(new BorderLayout());
        progressbarPanel.add(new Label("  "), BorderLayout.WEST);
        progressbarPanel.add(new Label("  "), BorderLayout.EAST);
        progressbarPanel.add(progressBar);
        secondPane.add(progressbarPanel);

        southPane.add(secondPane);

        contentPane.add(southPane, BorderLayout.SOUTH);

        contentPane.add(new Label("   "), BorderLayout.WEST);
        contentPane.add(new Label("   "), BorderLayout.EAST);
        contentPane.add(centerPane, BorderLayout.CENTER);

        listDirectory("c:\\"); // And now list initial directory.
        File[] roots2 = java.io.File.listRoots();
        for (int j = 0; j < roots2.length; j++)
        {
            driveChoice.add(getRoot(roots2[j].getAbsolutePath()));
        }
        driveChoice.select("C:\\");
        
        resultWindow = new ResultFrame(SUMMARY);
    }

	private String getRoot(String path)
	{
		String token = ":\\";
		String root = "";
		
		int index = path.indexOf(token);
		
		if (index != -1)
		{
			root = path.substring(0, index + 2);
		}
		
		return root;
		
	}

    /**
     * This method uses the list() method to get all entries in a directory and
     * then displays them in the List component.
     */
    public void listDirectory(String directory)
    {

        if (directory.indexOf("[") == 0)
        {
            directory = demodifyName(directory);
        }
        // Convert the string to a File object, and check that the dir exists
        File dir = new File(directory);
        if (!dir.isDirectory()) throw new IllegalArgumentException("FileLister: no such directory");

        files = dir.list(null);
        // Sort the list of filenames.
        java.util.Arrays.sort(files);

        if (directory == null) directory = "c:\\";
        
        ArrayList fileList = new ArrayList();
        ArrayList dirList = new ArrayList();
        
        for (int i = 0; i < files.length; i++)
        {
            File testFile = new File(directory, files[i]);
            if (testFile.isDirectory())
            {
                 files[i] = modifyName(files[i]);
                 dirList.add(files[i]);
            }
            else
            {
                fileList.add(files[i]);
            }
        }
        
        if (!fileList.isEmpty())
        {
            fileList = sortFileByExtension(fileList);
            
            files = new String[fileList.size() + dirList.size()];
            int i = 0;
            for (Iterator it = dirList.iterator(); it.hasNext();)
            {
                files[i] = (String)it.next();
                i++;
            }
            for (Iterator itt = fileList.iterator(); itt.hasNext();)
            {
                files[i] = (String)itt.next();
                i++;
            }
        }

        // Remove any old entries in the list, and add the new ones
        chooseLst.removeAll();
        chooseLst.add("[Up to Parent Directory]"); // A special case entry
        for (int i = 0; i < files.length; i++)
            chooseLst.add(files[i]);
        // Display directory name in window titlebar and in the details box

        //this.setTitle(directory);
        directoryDisplayLabel.setText(directory);

        // Remember this directory for later.
        currentDir = dir;
    }

    private String modifyName(String str)
    {
        str = "[" + str + "]";
        return str;
    }

    private String demodifyName(String str)
    {
        str = str.substring(1, str.length() - 1);
        return str;
    }

    private ArrayList sortFileByExtension(ArrayList fileList)
    {
        ArrayList sortedList = new ArrayList();
        
        ArrayList extList = new ArrayList();
        String file = null;
        int index = -1;
        String ext = null;
        
        for (Iterator it = fileList.iterator(); it.hasNext();)
        {
            file = (String)it.next();
            
            index = file.lastIndexOf(".");
            
            if (index != -1)
            {
                ext = file.substring(index);
                
                //We exclude zip files from this list if module = hybridization.
                if (!extList.contains(ext))
                    
                {
                	if (ext.equalsIgnoreCase(".zip") && module.equalsIgnoreCase("hybridization"))
                		continue;
                	else
                    	extList.add(ext);
                }
            }
        }
        Object[] exts = extList.toArray();
        java.util.Arrays.sort(exts);
        
        for (int i = 0; i < exts.length; i++)
        {
            ext = (String)exts[i];
            for (Iterator itt = fileList.iterator(); itt.hasNext();)
            {
                file = (String)itt.next();
                if (file.lastIndexOf(ext) != -1)
                {
                    sortedList.add(file);
                }
            }
        }
        return sortedList;
        
    }

    /**
     * This ItemListener method uses various File methods to obtain information
     * about a file or directory. Then it displays that info.
     */
    public void itemStateChanged(ItemEvent e)
    {
        int i = chooseLst.getSelectedIndex() - 1; // minus 1 for Up To Parent
                                                  // entry
        if (i < 0) return;
        String filename = files[i]; // Get the selected entry

        if (filename.indexOf("[") == 0)
        {
            filename = demodifyName(filename);
        }

        File f = new File(currentDir, filename); // Convert to a File
        if (!f.exists()) // Confirm that it exists
            throw new IllegalArgumentException("FileLister: " + "no such file or directory");

        // Get the details about the file or directory, concatenate to a string
        String info = filename;
        if (f.isDirectory()) info += File.separator;
        info += " " + f.length() + " bytes ";
        info += dateFormatter.format(new java.util.Date(f.lastModified()));
        if (f.canRead()) info += " Read";
        if (f.canWrite()) info += " Write";

    }

    /**
     * This ActionListener method is invoked when the user double-clicks on an
     * entry or clicks on one of the buttons. If they double-click on a file,
     * create a FileViewer to display that file. If they double-click on a
     * directory, call the listDirectory() method to display that directory
     */
    public void actionPerformed(ActionEvent e)
    {
        if (e.getSource() == chooseLst)
        {
            int i = chooseLst.getSelectedIndex(); // Check which item
            if (i == 0)
            {
                up();
            } // Handle first Up To Parent item
            else if (i > 0)
            { // Otherwise, get filename
                String name = files[i - 1];
                if (name.indexOf("[") == 0)
                {
                    name = demodifyName(name);
                }
                File f = new File(currentDir, name); // Convert to a File
                String fullname = f.getAbsolutePath();
                if (f.isDirectory()) listDirectory(fullname); // List dir
            }
        }
    }

    /** A convenience method to display the contents of the parent directory */
    protected void up()
    {
        String parent = currentDir.getParent();
        if (parent == null) return;
        listDirectory(parent);
    }

    /** Overridden so we can exit when window is closed */
    protected void processWindowEvent(WindowEvent e)
    {
        //super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING)
        {
            System.exit(0);
        }
    }

    void DriveChoice_itemStateChanged(ItemEvent e)
    {
        String itemSelected = driveChoice.getSelectedItem();
        listDirectory(itemSelected);
    }

    void chooseLst_actionPerformed(ActionEvent e)
    {
        //do nothing
    }

    void dataBt_actionPerformed(ActionEvent e)
    {
    	
        // move data file to selected list
        String[] preSelectedFileNames = chooseLst.getSelectedItems();
        
        if (isSingleFile && ((preSelectedFileNames.length > 1) || selectedLst.getItemCount() > 0))
        {
            JOptionPane.showMessageDialog(null, "Only one file is allowed for this array design", "Information", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (selectedFileVec.size() > 0)
        {
            preSelectedFileNames = checkDuplicateName(preSelectedFileNames);
        }
        //preSelectedFileNames = processPreSelectedFileNames(preSelectedFileNames);
        processItems(preSelectedFileNames, "data");
        for (int i = 1; i < files.length + 1; i++)
            chooseLst.deselect(i);
    }
    
    /* No longer check file name crash
    private String[] processPreSelectedFileNames(String[] psFileNames)
    {
    	ArrayList duplicateFileList = new ArrayList();
    	ArrayList notDuplicateFileList = new ArrayList();
    	ArrayList confirmOverwriteFileList = new ArrayList();
    	boolean toAdd = false;
    	String[] preSelectedFileNames = null;
    	
    	preSelectedFileNames = psFileNames;
    	
        if(!module.equalsIgnoreCase("arraydesign"))
        {
            fileList = getHybridizationFileList(up.getHttpTunnelingURL(), up.getId(), module);
        }

        for (int i = 0; i < preSelectedFileNames.length; i++)
        {
           	toAdd = fileList.contains(preSelectedFileNames[i]);
           	if (toAdd)
           	{
           		duplicateFileList.add(preSelectedFileNames[i]);
           	}
           	else
           	{
           		notDuplicateFileList.add(preSelectedFileNames[i]);
           	}
        }	
        
        if(duplicateFileList.size() > 0)
        {
        	String[] fileArray = new String[duplicateFileList.size()];
        	for (int i = 0; i < fileArray.length; i++)
        	{
        		fileArray[i] = (String)duplicateFileList.get(i);
        	}
        	LoadDuplicateFileNameProcessor ldfnp = new LoadDuplicateFileNameProcessor(
        			fileArray, confirmOverwriteFileList);
            
        }
    	
        preSelectedFileNames = new String[notDuplicateFileList.size() + confirmOverwriteFileList.size()];
        
        ArrayList newList = new ArrayList();
        
        for (int i = 0; i < notDuplicateFileList.size(); i ++)
        {
        	newList.add(notDuplicateFileList.get(i));
        }
        
        for (int i = 0; i < confirmOverwriteFileList.size(); i++)
        {
        	newList.add(confirmOverwriteFileList.get(i));
        }
        
        preSelectedFileNames = new String[newList.size()];
        for (int i = 0; i < preSelectedFileNames.length; i++)
        {
        	preSelectedFileNames[i] = (String)newList.get(i);
        }
        
    	return preSelectedFileNames;
    }
    */

    void selectAllBt_actionPerformed(ActionEvent e)
    {
        for (int i = 1; i < files.length + 1; i++)
            chooseLst.select(i);
    }

    void deSelectAllBt_actionPerformed(ActionEvent e)
    {
        for (int i = 1; i < files.length + 1; i++)
            chooseLst.deselect(i);
    }

    void otherBt_actionPerformed(ActionEvent e)
    {
        //move other file to selected list
        String[] preSelectedFileNames = chooseLst.getSelectedItems();
        if (selectedFileVec.size() > 0)
        {
            preSelectedFileNames = checkDuplicateName(preSelectedFileNames);
        }
        //preSelectedFileNames = processPreSelectedFileNames(preSelectedFileNames);
        processItems(preSelectedFileNames, "other");
        for (int i = 1; i < files.length + 1; i++)
            chooseLst.deselect(i);
    }

    void processItems(String[] preSelectedFileNames, String type)
    {
        Vector tempHolderVec = new Vector();
        boolean popup = false;
        boolean illegalType = false;
        for (int i = 0; i < preSelectedFileNames.length; i++)
        {
            if (!popup)
            {
                popup = isNameIllegal(preSelectedFileNames[i]);
            }
            
            //File filtering was disabled for now. 
//            if ("Data".equalsIgnoreCase(type) && !illegalType)
//            {
//                illegalType = isExtensionIllegal(preSelectedFileNames[i]);
//            }
            
            File pFile = new File(directoryDisplayLabel.getText() + File.separator
                    + preSelectedFileNames[i]);
            if (!pFile.isDirectory()
                    && !pFile.getName().equalsIgnoreCase("[Up to Parent Directory]")
                    && pFile.exists())
            {
                uFile = new HttpUploadFile();
                uFile.setAbsoluteFilePath(pFile.getAbsolutePath());
                uFile.setFileType(type);
                tempHolderVec.addElement(uFile);
            }
        }
        if (illegalType)
        {
            StringBuffer sb = new StringBuffer();
            if ("hybridization".equalsIgnoreCase(module))
            {
            	sb.append("The selected file names contain file extensions that are not\n");
            	sb.append("permissible to the hybridization data file type you have selected.\n");
            	sb.append("Only the following file extension(s) allowed:  ");
            }
            else if ("arraydesign".equalsIgnoreCase(module))
            {
            	sb.append("The selected file names contain file extensions that are not\n");
            	sb.append("permissible to the array design type you have selected.\n");
            	sb.append("Only the following file extension(s) allowed:  ");
            }
            for (int m = 0; m < extensions.length; m++)
            {
                if (m != 0)
                {
                    sb.append(", " + extensions[m]);
                }
                else
                {
                    sb.append(extensions[m]);
                }
            }
            sb.append(".");
            
            JOptionPane.showMessageDialog(null, sb.toString(), "Information", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (popup)
        {
            int userChoice = JOptionPane.showConfirmDialog(this,
                    "The selected file name cannot contain the following characters:\n"+ 
                    "|;,!@#$()<>/\\\"'~{}[]=+&^,space and tab." + 
                    "\nPress OK to automatically replace the above invalid characters with '_'",
                    "Invalid Character in File Name",
                    JOptionPane.OK_CANCEL_OPTION);
                
            if (userChoice != JOptionPane.OK_OPTION)
               return;
        }
        String[] populateItems = new String[tempHolderVec.size()];

        for (int i = 0; i < populateItems.length; i++)
        {
            selectedFileVec.addElement((HttpUploadFile)tempHolderVec.elementAt(i));
            selectedLst.add(((HttpUploadFile)tempHolderVec.elementAt(i)).toString());
        }
    }
    
    //To check the file name from the window system contaons any 
    //character that is illegal to the Unit system.
    private boolean isNameIllegal(String name)
    {
        int index = -1;                    
        for (int i = 0; i < pattern.length; i++)
        {
            index = name.indexOf(pattern[i]);
            if (index != -1)
               return true;
        }   
        return false;
    }

    //To check the file name has the permissible extention.
    //Yes return false, otherwise return true;
    
    private boolean isExtensionIllegal(String name)
    {
        int index = -1;
        String fileName = name.toUpperCase();
        for (int i = 0; i < extensions.length; i++)
        {
            index = fileName.lastIndexOf("." + extensions[i]);
            
            if (index != -1)
            {
                return false;
            }
        }
        return true;
    }
    
    String[] checkDuplicateName(String[] names)
    {
        Vector tempHolderVec = new Vector();
        boolean cannotAdd = false;
        for (int i = 0; i < names.length; i++)
        {
            cannotAdd = false;
            File tFile = new File(directoryDisplayLabel.getText() + File.separator + names[i]);
            String fullFileName = tFile.getAbsolutePath();
            if (!tFile.isDirectory())
            {
                for (int j = 0; j < selectedFileVec.size(); j++)
                {
                    if (fullFileName.equalsIgnoreCase(((HttpUploadFile)selectedFileVec.elementAt(j))
                            .getAbsoluteFilePath()))
                    {
                        cannotAdd = true;
                        break;
                    }
                }
                if (!cannotAdd)
                {
                    tempHolderVec.addElement(names[i]);
                }
            }
        }

        String[] returnArray = new String[tempHolderVec.size()];
        for (int i = 0; i < returnArray.length; i++)
        {
            returnArray[i] = (String)tempHolderVec.elementAt(i);
        }

        return returnArray;
    }

    void removeBt_actionPerformed(ActionEvent e)
    {
        //remove selected items
        int[] indexes = selectedLst.getSelectedIndexes();
        String[] deletingItems = selectedLst.getSelectedItems();
        for (int i = 0; i < indexes.length; i++)
        {
            selectedLst.remove(deletingItems[i]);
            selectedFileVec.setElementAt(null, indexes[i]);
        }
        Vector tempVec = new Vector();
        for (int i = 0; i < selectedFileVec.size(); i++)
        {
            if (selectedFileVec.elementAt(i) != null)
            {
                tempVec.addElement(selectedFileVec.elementAt(i));
            }
        }
        selectedFileVec = tempVec;

        for (int i = 0; i < selectedLst.getItemCount(); i++)
        {
            selectedLst.deselect(i);
        }
    }

    void removeAllBt_actionPerformed(ActionEvent e)
    {
        // remove all selected items
        selectedLst.removeAll();
        selectedFileVec.removeAllElements();
        selectedFileVec = new Vector();
    }

    /**
     * To start and monitor upload process.
     */
    void uploadBt_actionPerformed(ActionEvent e)
    {
    	//Object []buttons = {"Yes", "Cancel"};
    	int i = JOptionPane.showConfirmDialog(this,
			"Please ensure the selected files are correct! \nPress Ok to proceed, Cancel to edit selections.", "File Upload Confirmation",
			JOptionPane.OK_CANCEL_OPTION);
    		
        //upload files, make sure progress bar start working here
        if (selectedFileVec.size() != 0 && i == JOptionPane.OK_OPTION)
        {
        	updateBar(0);
            uploadFileName.setText("Applet is preparing for upload...");
            
            changeButtonOn(false, true);      
            
            thread = new HttpFileUploadThread(selectedFileVec, up);
            thread.startUpload();
            
              //Create a timer.
            timer = new Timer(200, new ActionListener() 
            {
                public void actionPerformed(ActionEvent evt) 
                {
                    //...Update the progress bar...
                    if ( thread == null )
                    {
                        timer.stop();
                        //Do we need to report it to the server?
                        uploadFileName.setText("Upload thread is not running, the submission has been aborted.");
                    }
                    
                    //The chance to catch this flag is very low.
                    if (!thread.isUploadPreparationDone() && !thread.isFailed())
                    {
                        uploadFileName.setText("Preparing for file upload, please wait...");
                    }
                    //In this case, no files are zipped, or socket connection failed
                    //and user has to try again.
                    else if ( thread.isFailed() )
                    {
                        uploadFileName.setText("File upload failed...");
                        timer.stop();
                        String message = thread.getFailureMessage();
                        
                        if (message != null && message.indexOf("expired") != -1)
                        {
    	                    //Object []buttons = {"Yes", "Cancel"};
    	                    JOptionPane.showMessageDialog(null, message, "Information", JOptionPane.ERROR_MESSAGE);
                            forwardToDefaultPage();
                        }
                        else
                        {
                        	uploadFileName.setText(message);
                            resultWindow.displayResults(thread.getOkFiles(), thread.getFailFiles(), "0 byte uploaded due to zipping process failure");
                            changeButtonOn(true, false);
                        }
                    }
                    else if (!thread.isUploadComplete())
                    {
                        uploadFileName.setText("Uploading file: " + thread.getCurrentFileName() + "...");
                        updateBar(thread.getCompletionPercent());
                    }
                    else if (thread.isUploadComplete())
                    {
                        timer.stop();
                        updateBar(100);
                        uploadFileName.setText("File upload complete...");
                        
                        //Only okFiles is not empty, we send a success signal to the server.
                        if (thread.getOkFiles().length > 0 && thread.notifyServer("success"))
                        {
                            uploadFileName.setText(thread.getNotifyReply());
                        }
                        else
                        {
                            uploadFileName.setText("Your file upload process failed. Please try again.");
                        }
                        
                        if ("arraydesign".equalsIgnoreCase(up.getModule()))
                        {
                        	disableUpload();
                        }
                        else
                        {
                        	changeButtonOn(true, false);
                        }
                        //Display upload summary to the user
                        resultWindow.displayResults(thread.getOkFiles(), thread.getFailFiles(), thread.getTimeBytes()); //thread.getFailureMessage()); //);
                        
                        thread.cleanUp();
                        selectedFileVec.clear();
                        selectedLst.removeAll();
                    }
                }
            });
            timer.start();
        }
    }

    /**
     * To stop upload process.
     */
    void stopBt_actionPerformed(ActionEvent e)
    {
        uploadFileName.setText("Is stopping...");
    	timer.stop();        
        if (thread.stopUpload())
        {
        	//Notify server only stopUpload returns.
        	thread.notifyServer("stop");
        	thread.cleanUp();
        }

        changeButtonOn(true, false);

        uploadFileName.setText("Upload stopped...");
        //selectedFileVec.clear();
    }

    /**
     * When user presses done button, all buutons except help Button 
     * are disabled, and user will be forwarded to a pre-determined page.
     */
    void doneBt_actionPerformed(ActionEvent e)
    {
    	if (thread == null)
    	{
    		thread = new HttpFileUploadThread(selectedFileVec, up);
    	}
        thread.notifyServer("done");
        
        changeButtonOn(false, false);

        uploadFileName.setText("Upload is done...");
        
        forwardToDefaultPage();
    }

    /**
     * This method is simply to forward user to a pre-set jsp page
     * when user presses "Done" button.
     */
    void forwardToDefaultPage()
    {
        AppletContext context = this.getAppletContext();
        
        try
        {
            URL goToUrl = new URL(up.getDefaultURLWithSid());
            URLConnection uc=goToUrl.openConnection();
            context.showDocument(goToUrl);
        }
        catch (Exception e)
        {
            uploadFileName.setText(" Sorry, I was not able to command the browser to\n" +
				               "automatically jump back to the caArray experiment search page. Please" +
				               "perform that manually \n"+
				               "You can cut & paste:\n " + up.getDefaultURL());
				
            uploadFileName.setEnabled(false);
        }
    }
    
    /**
     * To display the help text through the help popup window.
     */
    void helpBt_actionPerformed(ActionEvent e)
    {
        helpWindow = new HelpFrame(INSTRUCTION);
        helpWindow.displayInstruction();
    }

    public void updateBar(int newValue)
    {
        progressBar.setValue(newValue);
    }

    private void printUploadedFiles(String path)
    {
        System.out.println("====" + path);
    }
    
    public void changeButtonOn(boolean on1, boolean on2)
    {
        dataBt.setEnabled(on1);
        otherBt.setEnabled(on1);
        removeBt.setEnabled(on1);
        removeAllBt.setEnabled(on1);
        uploadBt.setEnabled(on1);
        doneBt.setEnabled(on1);
        
        stopBt.setEnabled(on2);
    }
    
    public void disableUpload()
    {
        dataBt.setEnabled(false);
        otherBt.setEnabled(false);
        removeBt.setEnabled(false);
        removeAllBt.setEnabled(false);
        uploadBt.setEnabled(false);
        doneBt.setEnabled(true);
        
        stopBt.setEnabled(false);	
    }
    /** 
     * A popup window to display to the user upload summary that includes
     * files uploaded, files failed to upload, time span and total bytes uploaded.
     */
    public class ResultFrame extends JFrame implements ActionListener
    { 
        JButton close = null;
        JLabel successLabel = null;
        JLabel failLabel = null;
        JLabel timeLabel = null;
        JTextArea successText = null;
        JTextArea failText = null;
        JTextArea timeText = null;
        JScrollPane successPane = null;
        JScrollPane failPane = null;
        Container contentPane;
    
        ResultFrame(String title) 
        { 
            super(title); 
            contentPane = getContentPane();

            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS)); 
                
            successLabel = new JLabel(SUCCESS);
            successLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPane.add(successLabel);
            
            successText = new JTextArea(10, 50);
            successText.setAlignmentX(Component.CENTER_ALIGNMENT);
            
            successPane = new JScrollPane(successText);
            successPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            successPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            successPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            contentPane.add(successPane);            
            
            failLabel = new JLabel(FAILURE);
            failLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPane.add(failLabel);

            failText = new JTextArea(2, 50);
            failText.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPane.add(failText);    
            
            failPane = new JScrollPane(failText);
            failPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            failPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            failPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            contentPane.add(failPane);
            
             timeLabel = new JLabel(TIMEBYTES);
            timeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPane.add(timeLabel);

            timeText = new JTextArea(2, 50);
            timeText.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPane.add(timeText);           
            
            close = new JButton("Close");
            close.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPane.add(close);
            close.addActionListener(this);            
            
            setSize(500, 300); 
            hide(); 
        } 

        public void displayResults(Object[] success, Object[] failed, String timeBytes)
        {
            if (success == null || success.length == 0)
            {
                successText.setText("No file uploaded");
            }
            else
            {
                String line = null;
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < success.length; i++)
                {
                    line = (String)success[i];
                    sb.append(line);
                    sb.append("\n");
                }
                successText.setText(sb.toString());
            }
            if (failed == null || failed.length == 0)
            {
                failText.setText("No file failed upload");
            }
            else
            {
                String line = null;
                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < failed.length; i++)
                {
                    line = (String)failed[i];
                    sb.append(line);
                    sb.append("\n");
                }
                failText.setText(sb.toString());
            }
            
            timeText.setText(timeBytes);
            
            show();
        }
        public void actionPerformed(ActionEvent e) 
        {
            String cmd = e.getActionCommand();
            if (cmd.equals("Close")) 
            {
                dispose();
            }
        }
    } 

    /** 
     * A popup window to display help text for the use of this upload applet.
     */
    public class HelpFrame extends JFrame implements ActionListener
    { 
        JButton close = null;
        JLabel helpLabel = null;

        JTextArea helpText = null;
        JScrollPane helpPane = null;

        Container contentPane;
    
        HelpFrame(String title) 
        { 
            super(title); 
            contentPane = getContentPane();
            contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS)); 
            
            helpLabel = new JLabel("Upload Instruction");
            helpLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPane.add(helpLabel);

            helpText = new JTextArea(5, 50);
            helpText.setAlignmentX(Component.CENTER_ALIGNMENT);
            helpText.setEditable(false);
            helpPane = new JScrollPane(helpText);
            helpPane.setAlignmentX(Component.CENTER_ALIGNMENT);
            //helpPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            helpPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
            helpPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
            contentPane.add(helpPane);        
            
            close = new JButton("Close");
            close.setAlignmentX(Component.CENTER_ALIGNMENT);
            contentPane.add(close);
            close.addActionListener(this);            
            
            setSize(500, 300); 
            hide(); 
        } 

        public void displayInstruction()
        {    
            helpText.setText(composeInstruction());
            
            show();
        }
        public void actionPerformed(ActionEvent e) 
        {
            String cmd = e.getActionCommand();
            if (cmd.equals("Close")) 
            {
                dispose();
            }
        }
        
        private String composeInstruction()
        {
            StringBuffer sb = new StringBuffer();
            sb.append("File names can be up to 256 characters long.\n");
            sb.append("Following characters you should not use in filenames:\n");
            sb.append("      | ; , ! @ # $ ( ) < > / \\ \" \' ` ~ { } [ ] = + & ^" + " space and tab\n");
            sb.append("The windows directory names should not contain space.");
            return sb.toString();
        }
    }
    /**
    * Test driver.
    *
    * @param args - Not used.
    */
    public static void main (String args[])
    {
        System.out.println("Starting applet...");
        HttpFileUploadApplet applet = new HttpFileUploadApplet();
        applet.setVisible(true);
        applet.init();
    }
}

class UploadFrame_DriveChoice_itemAdapter implements java.awt.event.ItemListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_DriveChoice_itemAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void itemStateChanged(ItemEvent e)
    {
        adaptee.DriveChoice_itemStateChanged(e);
    }
}


class UploadFrame_chooseLst_actionAdapter implements java.awt.event.ActionListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_chooseLst_actionAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.chooseLst_actionPerformed(e);
    }
}

class UploadFrame_dataBt_actionAdapter implements java.awt.event.ActionListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_dataBt_actionAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.dataBt_actionPerformed(e);
    }
}

class UploadFrame_selectAllBt_actionAdapter implements java.awt.event.ActionListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_selectAllBt_actionAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.selectAllBt_actionPerformed(e);
    }
}

class UploadFrame_deSelectAllBt_actionAdapter implements java.awt.event.ActionListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_deSelectAllBt_actionAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.deSelectAllBt_actionPerformed(e);
    }
}

class UploadFrame_otherBt_actionAdapter implements java.awt.event.ActionListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_otherBt_actionAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.otherBt_actionPerformed(e);
    }
}

class UploadFrame_removeBt_actionAdapter implements java.awt.event.ActionListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_removeBt_actionAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.removeBt_actionPerformed(e);
    }
}

class UploadFrame_removeAllBt_actionAdapter implements java.awt.event.ActionListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_removeAllBt_actionAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.removeAllBt_actionPerformed(e);
    }
}

class UploadFrame_uploadBt_actionAdapter implements java.awt.event.ActionListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_uploadBt_actionAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.uploadBt_actionPerformed(e);
    }
}

class UploadFrame_stopBt_actionAdapter implements java.awt.event.ActionListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_stopBt_actionAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.stopBt_actionPerformed(e);
    }
}

class UploadFrame_helpBt_actionAdapter implements java.awt.event.ActionListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_helpBt_actionAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.helpBt_actionPerformed(e);
    }
}

class UploadFrame_doneBt_actionAdapter implements java.awt.event.ActionListener
{
    HttpFileUploadApplet adaptee;

    UploadFrame_doneBt_actionAdapter(HttpFileUploadApplet adaptee)
    {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e)
    {
        adaptee.doneBt_actionPerformed(e);
    }
}