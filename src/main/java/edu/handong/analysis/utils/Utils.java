package edu.handong.analysis.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class Utils {
	public static void writeFile(ArrayList<String> lines, String fileName)
	{
		Path path = Paths.get(fileName);
		File parent = path.toFile().getParentFile();
		if (!parent.exists()) {
			try {
				parent.mkdirs();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}

		File resultFile = new File(fileName);
		
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(resultFile));
			resultFile.createNewFile();
			pw.println("StudentID, TotalNumberOfSemestersRegistered, Semester, NumCoursesTakenInTheSemester");
			for (String line : lines)
				pw.println(line);
			pw.close();
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} 
	}	
	
	public static void writeFile2(ArrayList<String> lines, String fileName) {
		Path path = Paths.get(fileName);
		File parentDir = path.toFile().getParentFile();
		if (!parentDir.exists()) {
			try {
				parentDir.mkdirs();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		File resultFile = new File(fileName);
		
		try {
			PrintWriter pw = new PrintWriter(new FileOutputStream(resultFile));
			resultFile.createNewFile();
			pw.println("Year,Semester,CouseCode, CourseName,TotalStudents,StudentsTaken,Rate");
			for (String str : lines)
				pw.println(str);
			pw.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
