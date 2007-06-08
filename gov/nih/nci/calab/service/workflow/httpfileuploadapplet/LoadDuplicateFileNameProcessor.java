package gov.nih.nci.calab.service.workflow.httpfileuploadapplet;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.util.ArrayList;

import javax.swing.UIManager;

public class LoadDuplicateFileNameProcessor 
{

	  public LoadDuplicateFileNameProcessor(String[] duplicateNames, ArrayList confirmList)
	  {
	    boolean packFrame = false;
	     try
	     {
	      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
	     }
	     catch(Exception e)
	     {
	         e.printStackTrace();
	     }

	      ProcessDuplicatedFileName frame = new ProcessDuplicatedFileName(duplicateNames, confirmList);
	      //Validate frames that have preset sizes
	      //Pack frames that have useful preferred size info, e.g. from their layout
	      if (packFrame) {
	        frame.pack();
	      }
	      else {
	        frame.validate();
	      }
	      //Center the window
	      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
	      Dimension frameSize = frame.getSize();
	      if (frameSize.height > screenSize.height) {
	        frameSize.height = screenSize.height;
	      }
	      if (frameSize.width > screenSize.width) {
	        frameSize.width = screenSize.width;
	      }
	      frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
	      frame.setVisible(true);
	      frame.setResizable(false);
	  }
}
