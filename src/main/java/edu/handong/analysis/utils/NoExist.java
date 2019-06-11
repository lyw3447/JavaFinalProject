package edu.handong.analysis.utils;

public class NoExist extends Exception {
	public NoExist(String fileName){
		super("In zipfile: " + fileName + ", Dont exist Excel file!");
	}
}
