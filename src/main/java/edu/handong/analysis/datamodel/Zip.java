package edu.handong.analysis.datamodel;

import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipFile;
import edu.handong.analysis.utils.NoExist;
import edu.handong.analysis.utils.Utils;

public class Zip extends Thread {
	private ArrayList<Excel> excels;
	private File file;
	
	public Zip(String path) {
		file = new File(path);
	}
	
	public void run() {
		excels = new ArrayList<Excel>(2);
		
		try {
			ZipFile zipFile;
			
			try {
				zipFile = new ZipFile(file, "EUC-KR", true);
				
				Enumeration<?extends ZipArchiveEntry> entries = zipFile.getEntries();

			    while(entries.hasMoreElements()){
			    	ZipArchiveEntry entry = entries.nextElement();
			    	String fileName = entry.getName();
			    	
			    	if (Utils.getExtension(fileName).equals("xlsx")) {
				    	InputStream stream = zipFile.getInputStream(entry);
				        
				        int cellCount = 0;
				        int type = 0;
				        
				        if (fileName.contains("(요약문)")) cellCount = 7;
				        else if (fileName.contains("(표.그림)")) {cellCount = 5; type=1;}	
				        
				        Excel myExcel = new Excel(stream, cellCount);
				        excels.add(type, myExcel);
			    	}
			    }
			    if (excels.isEmpty()) {
			    	throw new NoExist("");
			    }
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		catch(NoExist e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
	}
	
	public File getFile() {
		return file;
	}
	
	public ArrayList<Excel> getExcelFiles() {
		return excels;
	}
	
}
