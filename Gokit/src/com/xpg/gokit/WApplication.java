package com.xpg.gokit;

import java.io.IOException;

import org.orman.dbms.Database;
import org.orman.dbms.sqliteandroid.SQLiteAndroid;
import org.orman.mapper.MappingSession;

import com.xpg.gokit.utils.AssertsUtils;

import android.app.Application;

//import com.xpg.gokit.database.PassCode;

public class WApplication  extends Application{
	public void onCreate(){
		super.onCreate();
//		Database db = new SQLiteAndroid(this, "wifi.db");
//		MappingSession.registerDatabase(db);
//		MappingSession.registerEntity(PassCode.class);
//		MappingSession.start();
		try {
			AssertsUtils.copyAllAssertToCacheFolder(this.getApplicationContext());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
