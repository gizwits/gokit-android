package com.xpg.gokit.utils;

public class ByteUtils{
	static public String BytesToString(byte []bs){
		StringBuilder sb = new StringBuilder();
		for(byte b:bs){
			int i = b;
			if(i<0){
				i+=256;
			}
			
			sb.append((i<=0xf? "0"+Integer.toHexString(i):Integer.toHexString(i)));
		}
		return sb.toString();
	}
	static public byte[] StringToBytes(String str){
		int lengh = str.length()/2;
		byte []bs = new byte[lengh];
		for(int i = 0;i<str.length();i+=2){
				String info = str.substring(i, i+2);
				bs[i/2]=(byte)Integer.valueOf(info,16).intValue();
		}
		return bs;
	}
	
	static public byte[] ChangeIntToByte(int value,int count){
		byte[]bytes = new byte[count];
		for(int i = 0;i<count;i++){
			bytes[count-1-i] = (byte)((value>>(i*8)&0xff));
		}
		return bytes;
	}
}


