package com.xpg.gokit.utils;

public class CRCUtils {
	/**
	 * 
	 * 校验和处理工具
	 * 
	 * @author Sunny Ding
	 * */
	public static long CalculateCRC(String produckkey ,byte[]data){
		if(produckkey.equals("50e9de5e08ae11e4b96f001ec9b6dcfe")){
			return qianming(data);
		}else if(produckkey.equals("98623034d36011e38be7001ec9b6dcfe")){
			return ouyangfeng(data);
		}else {
			return youao(data);
		}
	}
	/**
	 * 
	 * 乾铭的校验和计算
	 * 
	 * @param data
	 * @return checksum
	 * 
	 * */
	public static long qianming(byte[]data){
		 //乾明
        long checksum = 0;
        for (int i=1; i<data.length-1; i++)
            checksum += data[i];
        return checksum;
	}
	/**
	 * 
	 * 欧阳锋的校验和计算
	 * 
	 * @param data
	 * @return checksum
	 * 
	 * */
	public static long ouyangfeng(byte []data){
		long checksum = 0;
        for (int i=0; i<data.length; i++)
            checksum +=data[i];
        return checksum;
	}
	/**
	 * 
	 * 友奥的校验和计算
	 * 
	 * @param data
	 * @return checksum
	 * 
	 * */
	public static long youao(byte []data){
		long checksum = 0;
        for (int i=0; i<data.length; i++)
            checksum +=data[i];
        return checksum;
	}
	
	
}
