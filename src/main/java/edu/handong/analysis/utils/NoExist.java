package edu.handong.analysis.utils;

public class NoExist extends Exception {
	
	public NoExist(String fileName){
		super(fileName + " Don't exist!");
	}
}
