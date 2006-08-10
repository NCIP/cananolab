package gov.nih.nci.calab.service.submit;

import gov.nih.nci.calab.dto.workflow.FileBean;

import java.util.ArrayList;
import java.util.List;

public class SubmitAssayResultService {
	public void saveAssayResult(String particleName, String fileName, String title,
			String description, String comments, String[] keywords) {
		
	}
	
	public List<FileBean> getAllRunFiles(String particleName) {
		List<FileBean>runFiles=new ArrayList<FileBean>();
		//TODO fill in the database query code
		FileBean file=new FileBean();
		file.setId("1");
		file.setShortFilename("NCL_3_distri.jpg");
		
		runFiles.add(file);
		return runFiles;
	}
}
