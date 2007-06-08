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

/**
 * This Frame is for file upload applet to check duplicate file names. 1) Applet
 * fecthes previous uploaded file names from server side 2) Users select files
 * that he/she wants to upload 3) Compare these file names that users want to
 * upload with previous upload file names. If there are any duplcate file names
 * occurring, the window will popup to let users make a decision to overwrite
 * them or rename them
 * 
 * @author Jim Zhou
 * @version 1.0 Date : 01/31/06
 * 
 */
import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class ProcessDuplicatedFileName extends JDialog
{
    JPanel contentPane;

    List duplicatedFileList = new List();

    JButton overwriteBt = new JButton();

    JLabel message = new JLabel();

    JButton cancelBt = new JButton();

    // for test only
    String directory = " ";

    JLabel noteLb = new JLabel();

    JLabel jLabel1 = new JLabel();

    JLabel emptyLb = new JLabel();

    JButton selectAllBt = new JButton();

    GridBagLayout gridBagLayout1 = new GridBagLayout();

    ArrayList cList = null;

    
    /** Construct the frame */
    public ProcessDuplicatedFileName(String[] list, ArrayList confirmList)
    {
        super(new Frame(), "Duplicate File names", true);
        cList = confirmList;
        enableEvents(AWTEvent.WINDOW_EVENT_MASK);
        try
        {
            loadList(list);
            jbInit();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Component initialization, before call jbInit, the method loadlist must be
     * called to initialize duplicate file names.
     */
    private void jbInit() throws Exception
    {
        // setIconImage(Toolkit.getDefaultToolkit().createImage(PromptWindow.class.getResource("[Your
        // Icon]")));
        contentPane = (JPanel) this.getContentPane();
        contentPane.setLayout(gridBagLayout1);
        this.setSize(new Dimension(403, 455));
        this.setTitle("Confirm Overwriting");
        overwriteBt.setText("Overwrite");
        overwriteBt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                overwriteBt_actionPerformed(e);
            }
        });
        message.setFont(new java.awt.Font("Dialog", 1, 12));
        message.setText("Duplicated file name(s) with previously uploaded file ");
        cancelBt.setText("Cancel");
        cancelBt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                cancelBt_actionPerformed(e);
            }
        });
        duplicatedFileList.setMultipleMode(true);

        noteLb.setFont(new java.awt.Font("Dialog", 3, 12));
        noteLb.setForeground(Color.blue);
        noteLb.setText("Note: ");
        jLabel1.setFont(new java.awt.Font("Dialog", 3, 12));
        jLabel1.setForeground(Color.blue);
        jLabel1.setText("You can rename your local files to avoid duplicate file names");
        selectAllBt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                selectAllBt_actionPerformed(e);
            }
        });
        selectAllBt.setText("Select All");
        selectAllBt.addActionListener(new java.awt.event.ActionListener()
        {
            public void actionPerformed(ActionEvent e)
            {
                selectAllBt_actionPerformed(e);
            }
        });
        contentPane.add(duplicatedFileList, new GridBagConstraints(0, 1, 3, 1,
                1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 28, 0, 22), 353, 277));
        contentPane.add(message, new GridBagConstraints(0, 0, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        21, 29, 0, 78), 0, 13));
        contentPane.add(emptyLb, new GridBagConstraints(0, 5, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
                        20, 11, 39), 344, 19));
        contentPane.add(jLabel1, new GridBagConstraints(0, 4, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(0,
                        20, 0, 28), 11, 8));
        contentPane.add(noteLb, new GridBagConstraints(0, 3, 3, 1, 0.0, 0.0,
                GridBagConstraints.WEST, GridBagConstraints.NONE, new Insets(
                        19, 20, 0, 22), 326, 3));
        contentPane.add(cancelBt, new GridBagConstraints(2, 2, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(
                        7, 37, 0, 22), 13, -1));
        contentPane.add(selectAllBt, new GridBagConstraints(0, 2, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(8, 27, 0, 0), 13, -1));
        contentPane.add(overwriteBt, new GridBagConstraints(1, 2, 1, 1, 0.0,
                0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(7, 34, 0, 0), 13, -1));
    }

    /** Overridden so we can exit when window is closed */
    protected void processWindowEvent(WindowEvent e)
    {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING)
        {
            ProcessDuplicatedFileName.this.setVisible(false);
            ProcessDuplicatedFileName.this.dispose();
            
        }
    }

    void overwriteBt_actionPerformed(ActionEvent e)
    {
        if (!checkSelectedItem())
        {
            String[] overwriteList = duplicatedFileList.getSelectedItems();
            for (int i = 0; i < overwriteList.length; i++)
            {
                cList.add(overwriteList[i]);
            }
            ProcessDuplicatedFileName.this.setVisible(false);
            ProcessDuplicatedFileName.this.dispose();
        }
    }

     public boolean checkSelectedItem()
    {
        boolean isEmpty = false;

        String[] items = duplicatedFileList.getSelectedItems();
        if (items.length == 0)
        {
            JOptionPane.showMessageDialog(this, "You must select one item",
                    "Warning message", JOptionPane.INFORMATION_MESSAGE);
            isEmpty = true;
        }

        return isEmpty;
    }

    public void loadList(String[] list)
    {
        // for test only
        // File ff = new File(directory);
        // String[] list = ff.list();
        duplicatedFileList.removeAll();
        for (int i = 0; i < list.length; i++)
        {
            duplicatedFileList.add(list[i]);
        }
    }

    void cancelBt_actionPerformed(ActionEvent e)
    {
        for (int i = 0; i < cList.size(); i++)
        {
            cList.remove(i);
        }
        ProcessDuplicatedFileName.this.setVisible(false);
        ProcessDuplicatedFileName.this.dispose();
    }

    void selectAllBt_actionPerformed(ActionEvent e)
    {
        int length = duplicatedFileList.getItemCount();
        for (int i = 0; i < length; i++)
        {
            duplicatedFileList.select(i);
        }
    }

}
