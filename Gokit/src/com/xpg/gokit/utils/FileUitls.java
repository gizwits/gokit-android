package com.xpg.gokit.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 文件工具类
 * @author Sunny Ding
 * */
public class FileUitls {
	/**
	 * 
	 * 读取文件内的所有的内容
	 * 
	 * @param file 文件名
	 * 
	 * @return filetext 文件内容
	 * */
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
