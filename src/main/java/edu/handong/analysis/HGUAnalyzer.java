package edu.handong.analysis;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import edu.handong.analysis.datamodel.Zip;
import edu.handong.analysis.utils.Utils;

public class HGUAnalyzer {

	private ArrayList<Zip> zips;
	
	public void run(String[] args) {
		
		CommandLineParser parser = new DefaultParser();

		String path = null;
		String directory = null;
		Options options = createOptions();
		
		try {
			CommandLine cmd = parser.parse(options, args);

			File file = new File(cmd.getOptionValue("i"));
			path = cmd.getOptionValue("o");
			
			String name = file.getName();
			String fullPath = file.getCanonicalPath();
			
			if (file.isFile() && Utils.getExtension(name).equals("zip")) {
				Utils.unzip(file, file.getParentFile());

				directory = Utils.getPathNoExt(fullPath);
			}
			else if (file.isDirectory()) 
				directory = fullPath;

			else {
				throw new Exception("Occur error from this file --> " + name);
			}
			
		} catch (ParseException e) {
			printHelp(options);
			System.exit(-1);
			
		} catch (IOException e) {
			System.out.println(e.getMessage());
			System.exit(-1);
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		
		File root = new File(directory);	//zip file들을 압축해재. Excel 파일들 불러옴.
		File[] dirList = root.listFiles();
		zips = new ArrayList<Zip>();
		
		for (File each : dirList) {	
	    	if (Utils.getExtension(each.getName()).equals("zip")) {
	    		try {
					zips.add(new Zip(each.getCanonicalPath()));
				} catch (IOException e) {
					e.printStackTrace();
				}
	    	}
	    }
		
	    zips.sort(new Comparator<Zip>() {
	    	public int compare(Zip object1, Zip object2) {
	    			String s1 = object1.getFile().getName();
	    			String s2 = object2.getFile().getName();
	    			return s1.compareTo(s2);
	    		}
	    });
	    		
	    		ArrayList<Thread> threadsForZipFile = new ArrayList<Thread>();
	    		
	    		for(Zip runner:zips) {				
	    			Thread thread = new Thread(runner);
	    			//System.out.println(runner.getFile().getName());
	    			thread.start();
	    			threadsForZipFile.add(thread);
	    		}
	    		
	    		try {
	    			for(Thread runner:threadsForZipFile) {
	    				runner.join();
	    			}
	    		} catch (InterruptedException e) {
	    			System.out.println(e.getMessage());
	    			System.exit(-1);
	    		}
	
	    		save(path);
				
	}
	
	void save(String targetFile) {
		Path path = Paths.get(targetFile);
		File parentDir = path.toFile().getParentFile();
		
		if (!parentDir.exists()) {
			try {
				parentDir.mkdirs();
			}
			catch(Exception e) {
				System.out.println(e.getMessage());
			}
		}
		
		try {
			PrintWriter pw1 = new PrintWriter(new FileOutputStream(Utils.getPathNoExt(targetFile) + 1 + ".csv"));
			PrintWriter pw2 = new PrintWriter(new FileOutputStream(Utils.getPathNoExt(targetFile) + 2 + ".csv"));
			
			pw1.println("zip number, 제목, 요약문 (300자 내외), \"핵심어 (keyword,쉽표로 구분)\", 조회날짜, 실제자료조회 출처 (웹자료링크), 원출처 (기관명 등), 제작자 (Copyright 소유처)");
			pw2.println("zip number, 제목(반드시 요약문 양식에 입력한 제목과 같아야 함.), 표/그림 일련번호, \"자료유형(표,그림,…)\", 자료에 나온 표나 그림 설명(캡션), 자료가 나온 쪽번호");
			
			for(Zip zipfile:zips) {
				for (String cell:zipfile.getExcelFiles().get(0).getOriginData()) {
					pw1.print(Utils.getPathNoExt(zipfile.getFile().getName()));
					pw1.println(cell);
				}
				for (String cell:zipfile.getExcelFiles().get(1).getOriginData()) {
					pw2.print(Utils.getPathNoExt(zipfile.getFile().getName()));
					pw2.println(cell);
				}
			}
			pw1.close();
			pw2.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	
	private Options createOptions() {
		Options options = new Options();

		options.addOption(Option.builder("i").longOpt("input")
				.desc("Set an input file path")
				.hasArg()
				.argName("Input path")
				.required()
				.build());
		
		options.addOption(Option.builder("o").longOpt("output")
				.desc("Set an input file path")
				.hasArg()
				.argName("Output path")
				.required()
				.build());

		options.addOption(Option.builder("h").longOpt("help")
		        .desc("Help")
		        .build());
		
		return options;
	}
	

	private void printHelp(Options options) {
		HelpFormatter formatter = new HelpFormatter();
		String header = "HGU Analyzer (통일자료 수집)";
		String footer = "";
		formatter.printHelp("HGU Analyzer", header, options, footer, true);
	}
}
