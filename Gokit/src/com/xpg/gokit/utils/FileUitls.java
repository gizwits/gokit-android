package com.xpg.gokit.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class FileUitls {
	static public String readFileAsText(String file) throws IOException{
		
	BufferedReader reader = new BufferedReader(new  InputStreamReader(new  FileInputStream(new File(file))));
	String line = "";
	String filetext = "";
	while(( line = reader.readLine())!=null){
		filetext+=line;
	}
	return filetext;
	}
}
