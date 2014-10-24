package com.xpg.gokit.setting;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings.Secure;
import android.util.Log;

public class SettingManager {
	SharedPreferences spf ;
	private Context c;

	public SettingManager(Context c){
		this.c = c;
		spf = c.getSharedPreferences("set", Context.MODE_PRIVATE);
	}
	public void setUserName(String name){
		spf.edit().putString("username", name).commit();
		
	}
	public String getUserName(){
		return spf.getString("username", "");
	}
	public void setPhoneNumber(String phoneNumber){
		spf.edit().putString("phonenumber", phoneNumber).commit();
	}
	public String getPhoneNumber(){
		return spf.getString("phonenumber", "");
	}
	public void setHideUid(String uid){
		spf.edit().putString("hideuid", uid).commit();
	}
	public String  getHideUid(){
		return spf.getString("hideuid", "");
	}
	public void setHideToken(String token){
		spf.edit().putString("hidetoken", token).commit();
	}
	public String  getHideToken(){
		return spf.getString("hidetoken", "");
	}
	
	public String getPhoneId(){
		String android_id = Secure.getString(c.getContentResolver(), Secure.ANDROID_ID);
		return android_id;
	}
	public void setPassword(String psw){
		spf.edit().putString("password", psw).commit();
	}
	public String getPassword(){
		return spf.getString("password", "");
	}
	public void setToken(String token){
		spf.edit().putString("token", token).commit();
	}
	public String getToken(){
		return spf.getString("token", "");
	}
	public void setUid(String uid){
		spf.edit().putString("uid", uid).commit();
	}
	public String getUid(){
		return spf.getString("uid", "");
	}
	
	
	static String filter = "=====";
	public void DownLoadProduct_key(String produck_key){
		String allkeys = spf.getString("keys", "");
		if(allkeys.contains(produck_key)){
			return;
		}else{
			synchronized (spf) {
			Log.i("add_poduct_key_in", produck_key);
			spf.edit().putString("keys", allkeys+produck_key+filter).commit();
			}
		}
		
	}
	public String getServerName(){
		return spf.getString("server", "api.gizwits.com");
	}
	public void setServerName(String server){
		spf.edit().putString("server", server).commit();
	}
	public String  getDownLoadProduct_key(){
		String allkeys = spf.getString("keys", "");
		String [] keys = allkeys.split(filter);
		if(!keys[0].equals("")){
			String newkeys = allkeys.replace(keys[0]+filter, "");
			synchronized (spf) {
				Log.i("add_poduct_key", keys[0]);
				spf.edit().putString("keys", newkeys).commit();
			}
			
			return keys[0];
		}else{
			return null;
		}
	}
	public void clean(){
		setHideToken("");
		setHideUid("");
		setUid("");
		setToken("");
		setPhoneNumber("");
		setPassword("");
		setUserName("");
	}
	
	

}
